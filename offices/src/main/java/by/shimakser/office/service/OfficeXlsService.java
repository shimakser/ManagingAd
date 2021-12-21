package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
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
import java.net.URL;
import java.util.List;

@Service("officeXlsService")
public class OfficeXlsService extends BaseOfficeService {

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeXlsService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    public void exportToFile(OfficeRequest officeRequest) {
        try (FileOutputStream out = new FileOutputStream(getExportFilePath(officeRequest) + ".xls");
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Offices");
            setImageToTable(workbook, sheet);
            setColumnsWidths(sheet);

            Cell info = sheet.createRow(8).createCell(5);
            info.setCellValue(FILE_TITLE);
            XSSFFont infoFont = ((XSSFWorkbook) workbook).createFont();
            infoFont.setFontName("Times New Roman");
            infoFont.setFontHeightInPoints((short) 16);
            infoFont.setBold(true);
            CellStyle infoStyle = workbook.createCellStyle();
            infoStyle.setAlignment(HorizontalAlignment.CENTER);
            infoStyle.setFont(infoFont);
            info.setCellStyle(infoStyle);
            CellRangeAddress cellAddressesId = new CellRangeAddress(8, 8, 5, 8);
            sheet.addMergedRegion(cellAddressesId);

            setTableHeader(workbook, sheet);
            int line = 12;
            List<Office> offices = officeRepository.findAll();
            for (int i = 0; i < offices.size(); i++) {
                CellStyle tableStyle = getStyle(workbook);

                Row row = sheet.createRow(i + line);

                Cell cellId = row.createCell(1);
                cellId.setCellValue(offices.get(i).getId());
                cellId.setCellStyle(tableStyle);

                Cell cellTitle = row.createCell(2);
                cellTitle.setCellValue(offices.get(i).getOfficeTitle());
                cellTitle.setCellStyle(tableStyle);

                Cell cellAddress = row.createCell(3);
                cellAddress.setCellValue(offices.get(i).getOfficeAddress());
                cellAddress.setCellStyle(tableStyle);

                Cell cellPrice = row.createCell(4);
                cellPrice.setCellValue(offices.get(i).getOfficePrice());
                cellPrice.setCellStyle(tableStyle);

                Cell cellDescription = row.createCell(9);
                cellDescription.setCellValue(offices.get(i).getOfficeDescription());
                cellDescription.setCellStyle(tableStyle);

                offices.get(i).getOfficeContacts()
                        .forEach(contact -> {
                            Cell cellContactId = row.createCell(5);
                            cellContactId.setCellValue(contact.getId());
                            cellContactId.setCellStyle(tableStyle);

                            Cell cellContactPhone = row.createCell(6);
                            cellContactPhone.setCellValue(contact.getContactPhoneNumber());
                            cellContactPhone.setCellStyle(tableStyle);

                            Cell cellContactEmail = row.createCell(7);
                            cellContactEmail.setCellValue(contact.getContactEmail());
                            cellContactEmail.setCellStyle(tableStyle);

                            Cell cellContactSite = row.createCell(8);
                            cellContactSite.setCellValue(contact.getContactSite());
                            cellContactSite.setCellStyle(tableStyle);
                        });
            }
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CellStyle getStyle(Workbook workbook) {
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

    private void setImageToTable(Workbook workbook, Sheet sheet) {
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

    private void setColumnsWidths(Sheet sheet) {
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 1600);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 15000);
    }

    private void setTableHeader(Workbook workbook, Sheet sheet) {
        Row header = sheet.createRow(10);
        Cell headerCell;
        Row contactsHeader = sheet.createRow(11);
        Cell contactsHeaderCell;

        CellStyle tableStyle = getStyle(workbook);

        for (int i = 0; i < officeColumnsNames.size() - 1; i++) {
            headerCell = header.createCell(i + 1);

            String columnName = officeColumnsNames.get(i);

            contactsHeaderCell = contactsHeader.createCell(i + 1);
            contactsHeaderCell.setCellStyle(tableStyle);

            if (columnName.equals("contacts")) {
                for (int j = 0; j < contactsColumnsNames.size(); j++) {
                    int point = i + j + 1;
                    contactsHeaderCell = contactsHeader.createCell(point);
                    contactsHeaderCell.setCellValue(contactsColumnsNames.get(j));

                    contactsHeaderCell.setCellStyle(tableStyle);
                    header.createCell(point).setCellStyle(tableStyle);
                    headerCell.setCellValue(columnName);
                }

                headerCell = header.createCell(i + contactsColumnsNames.size() + 1);
                headerCell.setCellValue(officeColumnsNames.get(i + 1));

                headerCell.setCellStyle(tableStyle);
                contactsHeader.createCell(9).setCellStyle(tableStyle);
            } else {
                headerCell.setCellValue(columnName);
                headerCell.setCellStyle(tableStyle);
            }
        }

        CellRangeAddress cellAddressesId = new CellRangeAddress(10, 11, 1, 1);
        sheet.addMergedRegion(cellAddressesId);
        CellRangeAddress cellAddressesTitle = new CellRangeAddress(10, 11, 2, 2);
        sheet.addMergedRegion(cellAddressesTitle);
        CellRangeAddress cellAddressesAddress = new CellRangeAddress(10, 11, 3, 3);
        sheet.addMergedRegion(cellAddressesAddress);
        CellRangeAddress cellAddressesPrice = new CellRangeAddress(10, 11, 4, 4);
        sheet.addMergedRegion(cellAddressesPrice);
        CellRangeAddress cellAddressesContacts = new CellRangeAddress(10, 10, 5, 8);
        sheet.addMergedRegion(cellAddressesContacts);
        CellRangeAddress cellAddressesDescription = new CellRangeAddress(10, 11, 9, 9);
        sheet.addMergedRegion(cellAddressesDescription);
    }
}
