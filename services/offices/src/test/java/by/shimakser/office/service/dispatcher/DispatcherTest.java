package by.shimakser.office.service.dispatcher;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import by.shimakser.office.service.ExportService;
import by.shimakser.office.service.impl.pdf.ExportOfficePdfService;
import by.shimakser.office.service.impl.xls.ExportContactXlsService;
import by.shimakser.office.service.impl.xls.ExportOfficeXlsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DispatcherTest {

    @Autowired
    Dispatcher<EntityType, FileType, ExportService<?>> dispatcher;

    @Test
    void getByEntityAndExportType_WithCorrectArguments() {

        ExportService<?> xlsOfficeService = dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.XLS);
        ExportService<?> xlsContactService = dispatcher.getByEntityAndExportType(EntityType.CONTACT, FileType.XLS);
        ExportService<?> pdfOfficeService = dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.PDF);

        assertEquals(xlsOfficeService.getClass(), ExportOfficeXlsService.class);
        assertEquals(xlsContactService.getClass(), ExportContactXlsService.class);
        assertEquals(pdfOfficeService.getClass(), ExportOfficePdfService.class);

        assertDoesNotThrow(() -> dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.XLS)
                .exportToFile(new ExportRequest(FileType.XLS, EntityType.OFFICE)));
        assertDoesNotThrow(() -> dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.PDF)
                .exportToFile(new ExportRequest(FileType.PDF, EntityType.OFFICE)));
        assertDoesNotThrow(() -> dispatcher.getByEntityAndExportType(EntityType.CONTACT, FileType.XLS));
    }

    @Test
    void getByEntityAndExportType_WithNotCorrectArguments() {
        assertThrows(NoSuchElementException.class, () -> dispatcher.getByEntityAndExportType(EntityType.CONTACT, FileType.PDF));
    }
}