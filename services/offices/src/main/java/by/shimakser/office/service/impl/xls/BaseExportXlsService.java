package by.shimakser.office.service.impl.xls;

import by.shimakser.dto.HeaderField;
import by.shimakser.office.annotation.ExportField;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import by.shimakser.office.service.BaseExportService;
import lombok.Getter;
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
@Getter
public abstract class BaseExportXlsService<T> extends BaseExportService<T> {

    private Workbook workbook;
    private Sheet sheet;

    public BaseExportXlsService(JpaRepository<T, Long> repository) {
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

        AtomicInteger lineCounter = new AtomicInteger(12);
        AtomicInteger columnsCounter = new AtomicInteger(1);
        entities.forEach(entity -> {
            final Row row = sheet.createRow(lineCounter.getAndIncrement());
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
                                        insertDate(row, columnsCounter, field, entity);
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

                                        insertDateIntoSubColumns(subNames, subFields, row, columnsCounter, lineCounter);
                                    }
                                });
                    });
            columnsCounter.set(1);
        });

        return toBytes(workbook);
    }

    @Override
    public FileType getType() {
        return FileType.XLS;
    }

    private byte[] toBytes(Workbook workbook) throws IOException {
        File file = null;

        try {
            file = Files.createTempFile(null, null).toFile();
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Files.readAllBytes(file.toPath());
    }

    private void insertImage() {
        try {
            File file = new File(URL_TO_IMAGE);

            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            int pictureId = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            inputStream.close();

            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
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

        AtomicInteger step = new AtomicInteger(0);
        for (int i = 0; i < headerFields.size(); i++) {
            step.set(step.incrementAndGet());

            String columnName = null;
            HeaderField headerField = headerFields.get(i);
            if (headerField.getSubFields() == null) {
                columnName = headerField.getTitle();

                CellRangeAddress cellAddresses = new CellRangeAddress(10, 11, step.get(), step.get());
                sheet.addMergedRegion(cellAddresses);

                styleCounter = step.get();
                headerCell = header.createCell(step.get());
            } else {
                columnName = headerField.getTitle();
                headerCell = header.createCell(step.get());
                int cellMergingCoord = step.get();

                int subHeadCounter = step.get();
                Cell subHeaderCell;
                List<HeaderField> headerFieldList = headerField.getSubFields();
                for (int j = 0; j < headerFieldList.size(); j++) {
                    sheet.setColumnWidth(subHeadCounter, 5000);
                    subHeadCounter++;
                    subHeaderCell = subHead.createCell(step.get() + j);
                    subHeaderCell.setCellValue(headerFieldList.get(j).getTitle());
                    subHeaderCell.setCellStyle(getStyle());
                }
                step.set(step.addAndGet(headerFieldList.size() - 1));

                for (int j = cellMergingCoord; j <= step.get(); j++) {
                    header.createCell(j).setCellStyle(getStyle());
                }

                CellRangeAddress cellAddresses = new CellRangeAddress(10, 10, cellMergingCoord, step.get());
                sheet.addMergedRegion(cellAddresses);
            }
            headerCell.setCellValue(columnName);
            headerCell.setCellStyle(tableStyle);
            subHead.createCell(styleCounter).setCellStyle(tableStyle);
            sheet.setColumnWidth(step.get(), 5000);
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