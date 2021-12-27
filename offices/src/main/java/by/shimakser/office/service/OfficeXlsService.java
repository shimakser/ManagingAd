package by.shimakser.office.service;

import by.shimakser.dto.HeaderField;
import by.shimakser.dto.OfficeRequest;
import by.shimakser.office.annotation.ExportField;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static by.shimakser.office.annotation.FieldNameAnalyzer.checkFieldsNames;

@Service("officeXlsService")
public class OfficeXlsService extends BaseOfficeService {

    private final OfficeRepository officeRepository;

    private Workbook workbook;
    private Sheet sheet;
    private final Class clazz = Office.class;

    @Autowired
    public OfficeXlsService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    public void exportToFile(OfficeRequest officeRequest) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Offices");

        List<Office> offices = officeRepository.findAll();
        List<HeaderField> headerFields = checkFieldsNames(clazz);

        setTitle();
        setImageToTable();
        setTableHeader(headerFields);

        AtomicInteger lineCounter = new AtomicInteger(12);
        AtomicInteger columnsCounter = new AtomicInteger(1);

        offices.forEach(office -> {
            Row row = sheet.createRow(lineCounter.getAndIncrement());
            headerFields.stream()
                    .map(HeaderField::getTitle)
                    .forEach(headerTitle -> {
                        Field[] fields = office.getClass().getDeclaredFields();
                        Arrays.stream(fields)
                                .filter(field -> field.isAnnotationPresent(ExportField.class))
                                .filter(field -> {
                                    String annotationArg = field.getAnnotation(ExportField.class).name();
                                    String name = annotationArg.equals("")
                                            ? field.getName()
                                            : annotationArg;
                                    return name.equals(headerTitle);
                                })
                                .forEach(field -> {
                                    Cell cell = row.createCell(columnsCounter.getAndIncrement());
                                    field.setAccessible(true);
                                    try {
                                        Optional<Object> name = Optional.ofNullable(field.get(office));
                                        Object d = name.orElseGet(() -> "-");
                                        cell.setCellValue(d.toString());
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                });
                    });
            columnsCounter.set(1);
        });

        try (FileOutputStream out = new FileOutputStream(getExportFilePath(officeRequest) + ".xls")) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTitle() {
        Cell info = sheet.createRow(8).createCell(1);
        info.setCellValue(FILE_TITLE);

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

    private void setImageToTable() {
        try {
            URL imageUrl = new URL(URL_TO_IMAGE);
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            File file = new File("/home/shimakser/logo.png");
            ImageIO.write(bufferedImage, "png", file);

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

    private void setTableHeader(List<HeaderField> headerFields) {
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
