package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

        try(FileOutputStream out = new FileOutputStream(path);
            Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Offices");

            Cell info = sheet.createRow(0).createCell(0);
            info.setCellValue("Offices import | "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss")));
            info.setCellStyle(getStyle(workbook));

            Row header = sheet.createRow(2);

            Cell headerCell;
            for (int i= 0; i < OFFICES_FIELDS.length; i++) {
                headerCell = header.createCell(i);
                headerCell.setCellValue(OFFICES_FIELDS[i]);
                headerCell.setCellStyle(getStyle(workbook));
            }

            List<Office> offices = officeRepository.findAll();
            for (int i = 3; i <= offices.size(); i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(offices.get(i-1).getId());
                row.createCell(0).setCellValue(offices.get(i-1).getId());
                row.createCell(1).setCellValue(offices.get(i-1).getOfficeTitle());
                row.createCell(2).setCellValue(offices.get(i-1).getOfficeAddress());
                row.createCell(3).setCellValue(offices.get(i-1).getOfficePrice());

                row.createCell(5).setCellValue(offices.get(i-1).getOfficeDescription());
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
}
