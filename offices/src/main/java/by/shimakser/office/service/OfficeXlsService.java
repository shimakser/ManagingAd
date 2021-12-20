package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("officeXlsService")
public class OfficeXlsService implements OfficeService {

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeXlsService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    @Transactional(rollbackFor = {FileNotFoundException.class})
    public void importToFile(OfficeRequest officeRequest) {
        String importFileName = "/offices_import_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HH_mm_ss")) + ".xls";
        String path = officeRequest.getPathToFile() + importFileName;

        try (FileOutputStream out = new FileOutputStream(path);
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Offices");

            setImageToTable(workbook, sheet);

            Cell info = sheet.createRow(10).createCell(1);
            info.setCellValue("Offices import | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss")));
            info.setCellStyle(getStyle(workbook));

            Row header = sheet.createRow(12);
            Cell headerCell;

            for (int i = 0; i < OFFICES_FIELDS.length; i++) {
                headerCell = header.createCell(i + 1);
                headerCell.setCellValue(OFFICES_FIELDS[i]);
                headerCell.setCellStyle(getStyle(workbook));
            }

            List<Office> offices = officeRepository.findAll();
            for (int i = 0; i < offices.size(); i++) {
                Row row = sheet.createRow(i + 13);

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
        return style;
    }

    private void setImageToTable(Workbook workbook, Sheet sheet) {
        try {
            URL imageUrl = new URL("https://i.redd.it/fsal3ipywty21.png");
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
            image.resize(0.1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
