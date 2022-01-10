package by.shimakser.office.config;

import by.shimakser.dto.EntityType;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import by.shimakser.office.model.Office;
import by.shimakser.office.service.ExportService;
import by.shimakser.office.service.dispatcher.Dispatcher;
import by.shimakser.office.service.dispatcher.ExportDispatcher;
import by.shimakser.office.service.impl.pdf.ExportOfficePdfService;
import by.shimakser.office.service.impl.xls.ExportOfficeXlsService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@TestConfiguration
public class DispatcherTestContextConfiguration {

    @Bean("testDispatcher")
    @Primary
    public Dispatcher<EntityType, FileType, ExportService> getExportDispatcher() {
        return new ExportDispatcher(List.of(getExportOfficeXlsService(), getExportOfficePdfService()));
    }

    @Bean("testXls")
    public ExportService<Office> getExportOfficeXlsService() {
        return new ExportOfficeXlsService() {

            @Override
            public byte[] exportToFile(ExportRequest exportRequest) throws IOException {
                Workbook workbook = new XSSFWorkbook();
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

            @Override
            public FileType getType() {
                return FileType.XLS;
            }

            @Override
            public EntityType getEntity() {
                return EntityType.OFFICE;
            }
        };
    }

    @Bean("testPdf")
    public ExportService<Office> getExportOfficePdfService() {
        return new ExportOfficePdfService() {
            @Override
            public byte[] exportToFile(ExportRequest exportRequest) throws IOException {
                File file = Files.createTempFile(null, null).toFile();
                Document document = new Document();
                FileOutputStream out = new FileOutputStream(file);
                try {
                    PdfWriter.getInstance(document, out);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                return Files.readAllBytes(file.toPath());
            }

            @Override
            public FileType getType() {
                return FileType.PDF;
            }

            @Override
            public EntityType getEntity() {
                return EntityType.OFFICE;
            }
        };
    }
}
