package by.shimakser.office.service.dispatcher;

import by.shimakser.dto.EntityType;
import by.shimakser.office.config.DispatcherTestContextConfiguration;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import by.shimakser.office.service.ExportService;
import by.shimakser.office.service.impl.pdf.ExportOfficePdfService;
import by.shimakser.office.service.impl.xls.ExportOfficeXlsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import(DispatcherTestContextConfiguration.class)
class DispatcherTest {

    @Autowired
    @Qualifier("testDispatcher")
    Dispatcher<EntityType, FileType, ExportService<?>> dispatcher;

    @Autowired
    @Qualifier("testXls")
    ExportOfficeXlsService exportOfficeXlsService;

    @Autowired
    @Qualifier("testPdf")
    ExportOfficePdfService exportOfficePdfService;

    @Test
    void getByEntityAndExportType_WithCorrectArguments() throws IOException {

        ExportService<?> xlsService = dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.XLS);
        ExportService<?> pdfService = dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.PDF);

        assertEquals(xlsService, exportOfficeXlsService);
        assertEquals(pdfService, exportOfficePdfService);

        assertDoesNotThrow(() -> dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.XLS)
                .exportToFile(new ExportRequest(FileType.XLS, EntityType.OFFICE)));
        assertDoesNotThrow(() -> dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.PDF)
                .exportToFile(new ExportRequest(FileType.PDF, EntityType.OFFICE)));
    }

    @Test
    void getByEntityAndExportType_WithNotCorrectArguments() {
        assertThrows(NoSuchElementException.class, () -> dispatcher.getByEntityAndExportType(EntityType.CONTACT, FileType.XLS));
    }

}