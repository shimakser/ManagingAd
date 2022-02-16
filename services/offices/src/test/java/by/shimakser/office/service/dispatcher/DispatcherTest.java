package by.shimakser.office.service.dispatcher;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.FileType;
import by.shimakser.office.service.ExportService;
import by.shimakser.office.service.impl.pdf.ExportOfficePdfService;
import by.shimakser.office.service.impl.xls.ExportContactXlsService;
import by.shimakser.office.service.impl.xls.ExportOfficeXlsService;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DispatcherTest {

    @Autowired
    private Dispatcher<EntityType, FileType, ExportService<?>> dispatcher;

    /**
     * {@link Dispatcher#getByEntityAndExportType(Object, Object)}
     */
    @Test
    void When_SearchExportServicesByEntityAndFileType_Then_CheckIsCorrectlySearchedServices() {
        // when
        ExportService<?> xlsOfficeService = dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.XLS);
        ExportService<?> xlsContactService = dispatcher.getByEntityAndExportType(EntityType.CONTACT, FileType.XLS);
        ExportService<?> pdfOfficeService = dispatcher.getByEntityAndExportType(EntityType.OFFICE, FileType.PDF);

        // then
        assertEquals(xlsOfficeService.getClass(), ExportOfficeXlsService.class);
        assertEquals(xlsContactService.getClass(), ExportContactXlsService.class);
        assertEquals(pdfOfficeService.getClass(), ExportOfficePdfService.class);
    }

    /**
     * {@link Dispatcher#getByEntityAndExportType(Object, Object)}
     */
    @Test
    void When_SearchExportServiceByEntityAndFileType_Then_CatchException() {
        // when
        BDDCatchException.when(() -> dispatcher.getByEntityAndExportType(EntityType.CONTACT, FileType.PDF));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(NoSuchElementException.class);
    }
}