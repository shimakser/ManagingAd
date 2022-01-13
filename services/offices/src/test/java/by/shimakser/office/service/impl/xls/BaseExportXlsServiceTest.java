package by.shimakser.office.service.impl.xls;

import by.shimakser.office.model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BaseExportXlsServiceTest {

    @Autowired
    BaseExportXlsService<Office> service;

    @Mock
    JpaRepository<Office, Long> repository;

    ExportRequest officeRequest = new ExportRequest(FileType.XLS, EntityType.OFFICE);

    @Test
    void exportToFile_WithCorrectRequest() throws IOException {

        assertDoesNotThrow(() -> service.exportToFile(officeRequest));

        assertNotNull(service.exportToFile(officeRequest));

        assertEquals(service.exportToFile(officeRequest).getClass(), byte[].class);
    }

    @Test
    void exportToFile_WithNotCorrectRequest() {
        ExportRequest contactRequest = new ExportRequest(FileType.XLS, EntityType.CONTACT);

        assertThrows(NoSuchElementException.class, () -> service.exportToFile(contactRequest));
    }

    @Test
    void exportToFile_WithoutData() {
        BaseExportXlsService<Office> emptyListService = new BaseExportXlsService<Office>(repository) {
            @Override
            public EntityType getEntity() {
                return EntityType.OFFICE;
            }
        };
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), emptyListService.getDataToExport());
    }

    @Test
    void containsCorrectData() throws IOException {
        BaseExportXlsService<Office> emptyListService = new BaseExportXlsService<>(repository) {
            @Override
            public EntityType getEntity() {
                return EntityType.OFFICE;
            }
        };

        Contact testContact = new Contact(1L, "testPhoneNumber", "testEmail", "testSite");
        Office testOffice = new Office(1L, "testTitle", "testAddress", 55.5,
                List.of(testContact), "testDescriptions");

        Mockito.when(repository.findAll()).thenReturn(List.of(testOffice));
        byte[] exportsBytes = emptyListService.exportToFile(officeRequest);

        ByteArrayInputStream bais = new ByteArrayInputStream(exportsBytes);
        Workbook workbook = new XSSFWorkbook(bais);
        Sheet sheet = workbook.getSheet("OFFICE");

        Iterator<Cell> cellIterator = sheet.getRow(12).cellIterator();
        List<String> cellValues = new ArrayList<>();
        while (cellIterator.hasNext()) {
            cellValues.add(cellIterator.next().getStringCellValue());
        }

        String exportInfo = sheet.getRow(8).getCell(1).getStringCellValue();


        assertNotEquals(cellValues.get(0), testOffice.getId().toString());
        assertEquals(exportInfo, officeRequest.getFileType().getFileTitle());

        assertEquals(cellValues.get(0), testOffice.getOfficeTitle());
        assertEquals(cellValues.get(7), testOffice.getOfficeDescription());

        assertEquals(cellValues.get(4), testContact.getContactPhoneNumber());
        assertEquals(cellValues.get(6), testContact.getContactSite());
    }
}