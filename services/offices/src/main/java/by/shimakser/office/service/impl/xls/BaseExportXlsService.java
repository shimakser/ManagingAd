package by.shimakser.office.service.impl.xls;

import by.shimakser.dto.HeaderField;
import by.shimakser.office.annotation.ExportField;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import by.shimakser.office.service.BaseExportService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static by.shimakser.office.annotation.FieldNameAnalyzer.checkFieldsNames;

@Service
public abstract class BaseExportXlsService<T> extends BaseExportService<T> {

    private Workbook workbook;
    private Sheet sheet;

    private static final AtomicInteger FIRST_ROW_NUMBER_FOR_EXPORT = new AtomicInteger(12);
    private static final AtomicInteger FIRST_COLUMN_NUMBER_FOR_EXPORT = new AtomicInteger(1);
    private static final AtomicInteger HEADER_INSERT_COUNTER = new AtomicInteger(0);

    private static final Integer DEFAULT_COLUMN_WIDTH = 5000;

    private static final Integer FIRST_COLUMN_NUMBER = 1;
    private static final Integer TITLE_ROW_NUMBER = 8;
    private static final Integer HEADER_ROW_NUMBER = 10;
    private static final Integer SUB_HEADER_ROW_NUMBER = 11;

    private static final Double IMAGE_WIDTH = 0.036;
    private static final Double IMAGE_HEIGHT = 0.075;

    private static final Short TABLE_FONT_HEIGHT = 14;
    private static final Short TITLE_FONT_HEIGHT = 16;

    protected BaseExportXlsService(JpaRepository<T, Long> repository) {
        super(repository);
    }

    @Override
    public byte[] exportToFile(ExportRequest exportRequest) throws IOException {

        if (getEntity() != exportRequest.getEntityType()) {
            throw new NoSuchElementException();
        }

        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(exportRequest.getEntityType().toString());

        List<T> entities = getDataToExport();
        List<HeaderField> headerFields = getHeaderFieldLists(entities);

        insertImage();
        insertTitle(exportRequest.getFileType());
        insertColumnsHeader(headerFields);

        entities.forEach(entity -> {
            final Row row = sheet.createRow(FIRST_ROW_NUMBER_FOR_EXPORT.getAndIncrement());
            headerFields
                    .forEach(headerField -> {
                        String headerTitle = headerField.getTitle();
                        Field[] fields = entity.getClass().getDeclaredFields();
                        Arrays.stream(fields)
                                .filter(field -> field.isAnnotationPresent(ExportField.class))
                                .filter(field -> getFieldName(field).equals(headerTitle))
                                .forEach(field -> {
                                    setFieldAccessible(field);

                                    if (headerField.getSubFields() == null) {
                                        insertDate(row, field, entity);
                                    } else {
                                        List<?> subNames = Collections.emptyList();
                                        try {
                                            subNames = (List<?>) field.get(entity);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }

                                        insertDateIntoSubColumns(subNames, field, row);
                                    }
                                });
                    });
            FIRST_COLUMN_NUMBER_FOR_EXPORT.set(1);
        });
        FIRST_ROW_NUMBER_FOR_EXPORT.set(12);
        return toBytes(workbook);
    }

    @Override
    public FileType getType() {
        return FileType.XLS;
    }

    private byte[] toBytes(Workbook workbook) throws IOException {
        File file = Files.createTempFile(null, null).toFile();

        try (FileOutputStream out = new FileOutputStream(file)) {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Files.readAllBytes(file.toPath());
    }

    private List<HeaderField> getHeaderFieldLists(List<T> entities) {
        if (!entities.isEmpty()) {
            return checkFieldsNames(entities.get(0).getClass());
        } else {
            return Collections.emptyList();
        }
    }

    private void insertImage() {
        try {
            File file = new File(getImage().getURI());

            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            int pictureId = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            inputStream.close();

            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(FIRST_COLUMN_NUMBER);
            anchor.setRow1(FIRST_COLUMN_NUMBER);

            Picture image = drawing.createPicture(anchor, pictureId);
            image.resize(IMAGE_WIDTH, IMAGE_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertTitle(FileType fileType) {
        Cell info = sheet.createRow(TITLE_ROW_NUMBER).createCell(FIRST_COLUMN_NUMBER);
        info.setCellValue(fileType.getFileTitle());

        XSSFFont infoFont = ((XSSFWorkbook) workbook).createFont();
        infoFont.setFontName("Times New Roman");
        infoFont.setFontHeightInPoints(TITLE_FONT_HEIGHT);
        infoFont.setBold(true);

        CellStyle infoStyle = workbook.createCellStyle();
        infoStyle.setAlignment(HorizontalAlignment.CENTER);
        infoStyle.setFont(infoFont);
        info.setCellStyle(infoStyle);

        CellRangeAddress cellAddressesId
                = new CellRangeAddress(TITLE_ROW_NUMBER, TITLE_ROW_NUMBER, FIRST_COLUMN_NUMBER, FIRST_COLUMN_NUMBER + 3);
        sheet.addMergedRegion(cellAddressesId);
    }

    private void insertColumnsHeader(List<HeaderField> headerFields) {
        Row header = sheet.createRow(HEADER_ROW_NUMBER);
        Row subHead = sheet.createRow(SUB_HEADER_ROW_NUMBER);
        Cell headerCell;

        CellStyle tableStyle = getStyle();

        for (int i = 0; i < headerFields.size(); i++) {
            HEADER_INSERT_COUNTER.set(HEADER_INSERT_COUNTER.incrementAndGet());

            String columnName = null;
            HeaderField headerField = headerFields.get(i);
            if (headerField.getSubFields() == null) {
                columnName = headerField.getTitle();

                CellRangeAddress cellAddresses = new CellRangeAddress(HEADER_ROW_NUMBER, SUB_HEADER_ROW_NUMBER,
                        HEADER_INSERT_COUNTER.get(), HEADER_INSERT_COUNTER.get());
                sheet.addMergedRegion(cellAddresses);

                headerCell = header.createCell(HEADER_INSERT_COUNTER.get());
            } else {
                columnName = headerField.getTitle();
                headerCell = header.createCell(HEADER_INSERT_COUNTER.get());
                int cellMergingCoord = HEADER_INSERT_COUNTER.get();

                int subHeadCounter = HEADER_INSERT_COUNTER.get();
                Cell subHeaderCell;
                List<HeaderField> headerFieldList = headerField.getSubFields();
                for (int j = 0; j < headerFieldList.size(); j++) {
                    sheet.setColumnWidth(subHeadCounter, DEFAULT_COLUMN_WIDTH);
                    subHeadCounter++;
                    subHeaderCell = subHead.createCell(HEADER_INSERT_COUNTER.get() + j);
                    subHeaderCell.setCellValue(headerFieldList.get(j).getTitle());
                    subHeaderCell.setCellStyle(getStyle());
                }
                HEADER_INSERT_COUNTER.set(HEADER_INSERT_COUNTER.addAndGet(headerFieldList.size() - 1));

                for (int j = cellMergingCoord; j <= HEADER_INSERT_COUNTER.get(); j++) {
                    header.createCell(j).setCellStyle(getStyle());
                }

                CellRangeAddress cellAddresses
                        = new CellRangeAddress(HEADER_ROW_NUMBER, HEADER_ROW_NUMBER, cellMergingCoord, HEADER_INSERT_COUNTER.get());
                sheet.addMergedRegion(cellAddresses);
            }
            headerCell.setCellValue(columnName);
            headerCell.setCellStyle(tableStyle);
            subHead.createCell(HEADER_INSERT_COUNTER.get()).setCellStyle(tableStyle);

            sheet.setColumnWidth(HEADER_INSERT_COUNTER.get(), DEFAULT_COLUMN_WIDTH);
        }
        HEADER_INSERT_COUNTER.set(0);
        sheet.setColumnWidth(8, 15000);
    }

    private void insertDate(Row row, Field field, T t) {
        try {
            Cell cell = row.createCell(FIRST_COLUMN_NUMBER_FOR_EXPORT.getAndIncrement());
            Object name = Optional.ofNullable(field.get(t)).orElseGet(() -> "-");
            cell.setCellValue(name.toString());
            cell.setCellStyle(getStyle());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void insertDateIntoSubColumns(List<?> subNames, Field field, Row row) {
        ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
        Class<?> collectionGenericType = (Class<?>) collectionType.getActualTypeArguments()[0];
        Field[] subFields = collectionGenericType.getDeclaredFields();

        if (subNames.isEmpty()) {
            for (int i = 0; i < subFields.length; i++) {
                Cell cell = row.createCell(FIRST_COLUMN_NUMBER_FOR_EXPORT.getAndIncrement());
                cell.setCellValue("-");
                cell.setCellStyle(getStyle());
            }
        } else {
            Row newRow = row;
            for (int i = 0; i < subNames.size(); i++) {
                Object subEntity = subNames.get(i);
                for (int j = 0; j < subFields.length; j++) {
                    Field subField = subFields[j];
                    setFieldAccessible(subField);
                    Cell cell = newRow.createCell(FIRST_COLUMN_NUMBER_FOR_EXPORT.getAndIncrement());

                    try {
                        String name = subField.get(subEntity).toString();
                        cell.setCellValue(name);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    cell.setCellStyle(getStyle());
                }
                newRow = sheet.createRow(FIRST_ROW_NUMBER_FOR_EXPORT.incrementAndGet());
                FIRST_COLUMN_NUMBER_FOR_EXPORT.set(FIRST_COLUMN_NUMBER_FOR_EXPORT.get() - subFields.length);
            }
            FIRST_COLUMN_NUMBER_FOR_EXPORT.set(FIRST_COLUMN_NUMBER_FOR_EXPORT.get() + subFields.length);
            FIRST_ROW_NUMBER_FOR_EXPORT.set(FIRST_ROW_NUMBER_FOR_EXPORT.decrementAndGet());
        }
    }

    private void setFieldAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    private String getFieldName(Field field) {
        String annotArg = field.getAnnotation(ExportField.class).name();
        return annotArg.equals("")
                ? field.getName()
                : annotArg;
    }

    private CellStyle getStyle() {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints(TABLE_FONT_HEIGHT);

        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

}
