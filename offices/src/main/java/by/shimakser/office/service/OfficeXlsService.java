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
            info.setCellStyle(getStyle(workbook));

            setTableHeader(workbook, sheet);

            List<Office> offices = officeRepository.findAll();
            for (int i = 0; i < offices.size(); i++) {
                Row row = sheet.createRow(i + 12);

                Cell cellId = row.createCell(1);
                cellId.setCellValue(offices.get(i).getId());
                cellId.setCellStyle(getStyle(workbook));

                Cell cellTitle = row.createCell(2);
                cellTitle.setCellValue(offices.get(i).getOfficeTitle());
                cellTitle.setCellStyle(getStyle(workbook));

                Cell cellAddress = row.createCell(3);
                cellAddress.setCellValue(offices.get(i).getOfficeAddress());
                cellAddress.setCellStyle(getStyle(workbook));

                Cell cellPrice = row.createCell(4);
                cellPrice.setCellValue(offices.get(i).getOfficePrice());
                cellPrice.setCellStyle(getStyle(workbook));

                Cell cellContacts = row.createCell(5);
                cellContacts.setCellValue(offices.get(i).getOfficeContacts().toString());
                cellContacts.setCellStyle(getStyle(workbook));

                Cell cellDescription = row.createCell(6);
                cellDescription.setCellValue(offices.get(i).getOfficeDescription());
                cellDescription.setCellStyle(getStyle(workbook));

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
            anchor.setCol1(2);
            anchor.setRow1(1);

            Picture image = drawing.createPicture(anchor, pictureId);
            image.resize(0.036, 0.075);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setColumnsWidths(Sheet sheet) {
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 1600);
        sheet.setColumnWidth(5, 18000);
        sheet.setColumnWidth(6, 15000);
    }

    private void setTableHeader(Workbook workbook, Sheet sheet) {
        Row header = sheet.createRow(10);
        Row contactsHeader = sheet.createRow(11);
        Cell headerCell;
        Cell contactsHeaderCell;

        for (int i = 0; i < officeColumnsNames.size(); i++) {
            headerCell = header.createCell(i + 1);
            headerCell.setCellValue(officeColumnsNames.get(i));
            headerCell.setCellStyle(getStyle(workbook));

            contactsHeaderCell = contactsHeader.createCell(i + 1);
            contactsHeaderCell.setCellStyle(getStyle(workbook));
        }

        CellRangeAddress cellAddressesId = new CellRangeAddress(10, 11, 1, 1);
        sheet.addMergedRegion(cellAddressesId);
        CellRangeAddress cellAddressesTitle = new CellRangeAddress(10, 11, 2, 2);
        sheet.addMergedRegion(cellAddressesTitle);
        CellRangeAddress cellAddressesAddress = new CellRangeAddress(10, 11, 3, 3);
        sheet.addMergedRegion(cellAddressesAddress);
        CellRangeAddress cellAddressesPrice = new CellRangeAddress(10, 11, 4, 4);
        sheet.addMergedRegion(cellAddressesPrice);
        CellRangeAddress cellAddressesDescription = new CellRangeAddress(10, 11, 6, 6);
        sheet.addMergedRegion(cellAddressesDescription);
    }
}
