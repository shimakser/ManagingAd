package by.shimakser.office.service.impl.pdf;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ExportOfficePdfServiceTest {

    @Autowired
    private ExportOfficePdfService exportOfficePdfService;

    @Test
    void exportToFile() {
        // then
        assertDoesNotThrow(() -> exportOfficePdfService.exportToFile(new ExportRequest(FileType.PDF, EntityType.OFFICE)));
    }
}