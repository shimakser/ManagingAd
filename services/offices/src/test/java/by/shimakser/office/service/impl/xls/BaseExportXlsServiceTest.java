package by.shimakser.office.service.impl.xls;

import by.shimakser.office.model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class BaseExportXlsServiceTest {

    @Autowired
    private BaseExportXlsService<Office> service;

    @Mock
    private JpaRepository<Office, Long> repository;

    private static final ExportRequest EXPORT_REQUEST = new ExportRequest(FileType.XLS, EntityType.OFFICE);

    @Test
    void exportToFile_WithCorrectRequest() throws IOException {
        byte[] exportBytes = service.exportToFile(EXPORT_REQUEST);

        assertEquals(exportBytes.getClass(), byte[].class);
    }

    @Test
    void exportToFile_WithIncorrectRequest() {
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

        given(repository.findAll()).willReturn(Collections.emptyList());

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

        given(repository.findAll()).willReturn(List.of(testOffice));

        byte[] exportsBytes = emptyListService.exportToFile(EXPORT_REQUEST);

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
        assertEquals(exportInfo, EXPORT_REQUEST.getFileType().getFileTitle());

        assertEquals(cellValues.get(0), testOffice.getOfficeTitle());
        assertEquals(cellValues.get(7), testOffice.getOfficeDescription());

        assertEquals(cellValues.get(4), testContact.getContactPhoneNumber());
        assertEquals(cellValues.get(6), testContact.getContactSite());
    }
}