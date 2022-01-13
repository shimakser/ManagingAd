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
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static by.shimakser.office.annotation.FieldNameAnalyzer.checkFieldsNames;

@Service
public abstract class BaseExportXlsService<T> extends BaseExportService<T> {

    private Workbook workbook;
    private Sheet sheet;

    private final AtomicInteger FIRST_ROW_NUMBER_FOR_EXPORT = new AtomicInteger(12);
    private final AtomicInteger FIRST_COLUMN_NUMBER_FOR_EXPORT = new AtomicInteger(1);
    private final AtomicInteger HEADER_INSERT_COUNTER = new AtomicInteger(0);

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
        List<HeaderField> headerFields;
        if (!entities.isEmpty()) {
            headerFields = checkFieldsNames(entities.get(0).getClass());
        } else {
            headerFields = Collections.emptyList();
        }

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
                                    field.setAccessible(true);
                                    if (headerField.getSubFields() == null) {
                                        insertDate(row, FIRST_COLUMN_NUMBER_FOR_EXPORT, field, entity);
                                    } else {
                                        List<?> subNames = Collections.emptyList();
                                        try {
                                            subNames = (List<?>) field.get(entity);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }

                                        ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
                                        Class<?> collectionGenericType = (Class<?>) collectionType.getActualTypeArguments()[0];
                                        Field[] subFields = collectionGenericType.getDeclaredFields();

                                        insertDateIntoSubColumns(subNames, subFields, row, FIRST_COLUMN_NUMBER_FOR_EXPORT, FIRST_ROW_NUMBER_FOR_EXPORT);
                                    }
                                });
                    });
            FIRST_COLUMN_NUMBER_FOR_EXPORT.set(1);
        });

        return toBytes(workbook);
    }

    @Override
    public FileType getType() {
        return FileType.XLS;
    }

    private byte[] toBytes(Workbook workbook) throws IOException {
        File file = Files.createTempFile(null, null).toFile();

        try (FileOutputStream out = new FileOutputStream(file))  {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Files.readAllBytes(file.toPath());
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
            anchor.setCol1(1);
            anchor.setRow1(1);

            Picture image = drawing.createPicture(anchor, pictureId);
            image.resize(0.036, 0.075);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertTitle(FileType fileType) {
        Cell info = sheet.createRow(8).createCell(1);
        info.setCellValue(fileType.getFileTitle());

        XSSFFont infoFont = ((XSSFWorkbook) workbook).createFont();
        infoFont.setFontName("Times New Roman");
        infoFont.setFontHeightInPoints((short) 16);
        infoFont.setBold(true);

        CellStyle infoStyle = workbook.createCellStyle();
        infoStyle.setAlignment(HorizontalAlignment.CENTER);
        infoStyle.setFont(infoFont);
        info.setCellStyle(infoStyle);

        CellRangeAddress cellAddressesId = new CellRangeAddress(8, 8, 1, 3);
        sheet.addMergedRegion(cellAddressesId);
    }

    private void insertColumnsHeader(List<HeaderField> headerFields) {
        Row header = sheet.createRow(10);
        Row subHead = sheet.createRow(11);
        Cell headerCell;

        CellStyle tableStyle = getStyle();
        int styleCounter = 0;


        for (int i = 0; i < headerFields.size(); i++) {
            HEADER_INSERT_COUNTER.set(HEADER_INSERT_COUNTER.incrementAndGet());

            String columnName = null;
            HeaderField headerField = headerFields.get(i);
            if (headerField.getSubFields() == null) {
                columnName = headerField.getTitle();

                CellRangeAddress cellAddresses = new CellRangeAddress(10, 11, HEADER_INSERT_COUNTER.get(), HEADER_INSERT_COUNTER.get());
                sheet.addMergedRegion(cellAddresses);

                styleCounter = HEADER_INSERT_COUNTER.get();
                headerCell = header.createCell(HEADER_INSERT_COUNTER.get());
            } else {
                columnName = headerField.getTitle();
                headerCell = header.createCell(HEADER_INSERT_COUNTER.get());
                int cellMergingCoord = HEADER_INSERT_COUNTER.get();

                int subHeadCounter = HEADER_INSERT_COUNTER.get();
                Cell subHeaderCell;
                List<HeaderField> headerFieldList = headerField.getSubFields();
                for (int j = 0; j < headerFieldList.size(); j++) {
                    sheet.setColumnWidth(subHeadCounter, 5000);
                    subHeadCounter++;
                    subHeaderCell = subHead.createCell(HEADER_INSERT_COUNTER.get() + j);
                    subHeaderCell.setCellValue(headerFieldList.get(j).getTitle());
                    subHeaderCell.setCellStyle(getStyle());
                }
                HEADER_INSERT_COUNTER.set(HEADER_INSERT_COUNTER.addAndGet(headerFieldList.size() - 1));

                for (int j = cellMergingCoord; j <= HEADER_INSERT_COUNTER.get(); j++) {
                    header.createCell(j).setCellStyle(getStyle());
                }

                CellRangeAddress cellAddresses = new CellRangeAddress(10, 10, cellMergingCoord, HEADER_INSERT_COUNTER.get());
                sheet.addMergedRegion(cellAddresses);
            }
            headerCell.setCellValue(columnName);
            headerCell.setCellStyle(tableStyle);
            subHead.createCell(styleCounter).setCellStyle(tableStyle);
            sheet.setColumnWidth(HEADER_INSERT_COUNTER.get(), 5000);
        }
        sheet.setColumnWidth(8, 15000);
    }

    private void insertDate(Row row, AtomicInteger columnsCounter, Field field, T t) {
        try {
            Cell cell = row.createCell(columnsCounter.getAndIncrement());
            Object name = Optional.ofNullable(field.get(t)).orElseGet(() -> "-");
            cell.setCellValue(name.toString());
            cell.setCellStyle(getStyle());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void insertDateIntoSubColumns(List<?> subNames, Field[] subFields, Row row,
                                            AtomicInteger columnsCounter, AtomicInteger lineCounter) {
        if (subNames.isEmpty()) {
            for (int i = 0; i < subFields.length; i++) {
                Cell cell = row.createCell(columnsCounter.getAndIncrement());
                cell.setCellValue("-");
                cell.setCellStyle(getStyle());
            }
        } else {
            Row newRow = row;
            for (int i = 0; i < subNames.size(); i++) {
                Object subEntity = subNames.get(i);
                for (int j = 0; j < subFields.length; j++) {
                    Field subField = subFields[j];
                    subField.setAccessible(true);
                    Cell cell = newRow.createCell(columnsCounter.getAndIncrement());

                    try {
                        String name = subField.get(subEntity).toString();
                        cell.setCellValue(name);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    cell.setCellStyle(getStyle());
                }
                newRow = sheet.createRow(lineCounter.getAndAdd(1));
                columnsCounter.set(columnsCounter.get() - subFields.length);
            }
            columnsCounter.set(columnsCounter.get() + subFields.length);
            lineCounter.set(lineCounter.decrementAndGet());
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
        font.setFontHeightInPoints((short) 14);

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
