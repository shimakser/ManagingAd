package by.shimakser.office.service;

import by.shimakser.dto.EntityType;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import by.shimakser.office.model.Office;
import by.shimakser.office.service.impl.xls.ExportOfficeXlsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExportServiceTest {

    @Autowired
    @InjectMocks
    ExportOfficeXlsService service;

    @Mock
    JpaRepository<Office, Long> repository;

    @Test
    void exportToFile() throws IOException {
        ExportRequest request = new ExportRequest(FileType.XLS, EntityType.OFFICE);

        assertDoesNotThrow(() -> service.exportToFile(request));
        assertNotNull(service.exportToFile(request));
        assertEquals(service.exportToFile(request).getClass(), byte[].class);
    }

    @Test
    void getAll() {

        Mockito.when(repository.findAll()).thenReturn(service.getDataToExport());

        assertDoesNotThrow(() -> service.getDataToExport());
        assertNotNull(service.getDataToExport());
    }
}