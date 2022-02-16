package by.shimakser.office.service.impl.xls;

import by.shimakser.office.model.*;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class BaseExportXlsServiceTest {

    @Autowired
    private BaseExportXlsService<Office> service;

    @Mock
    private JpaRepository<Office, Long> repository;

    private static final ExportRequest EXPORT_REQUEST = new ExportRequest(FileType.XLS, EntityType.OFFICE);
    private static final Contact CONTACT = new Contact(1L, "testPhoneNumber", "testEmail", "testSite");
    private static final Office OFFICE = new Office(1L, "testTitle", "testAddress", 55.5, List.of(CONTACT), "testDescriptions");

    /**
     * {@link BaseExportXlsService#exportToFile(ExportRequest)}
     */
    @Test
    void When_ExportToFile_Then_CheckIsCorrectlyExport() {
        // when
        byte[] exportBytes = new byte[0];
        try {
            exportBytes = service.exportToFile(EXPORT_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // then
        assertEquals(exportBytes.getClass(), byte[].class);
    }

    /**
     * {@link BaseExportXlsService#exportToFile(ExportRequest)}
     */
    @Test
    void Given_SetRequestWithIncorrectEntity_When_ExportToFile_Then_CatchException() {
        // given
        ExportRequest contactRequest = new ExportRequest(FileType.XLS, EntityType.CONTACT);

        // when
        BDDCatchException.when(() -> service.exportToFile(contactRequest));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * {@link BaseExportXlsService#exportToFile(ExportRequest)}
     */
    @Test
    void Given_SearchOffices_When_ExportToFile_Then_CheckIsMethodReturnEmptyList() {
        // given
        BaseExportXlsService<Office> emptyListService = new BaseExportXlsService<Office>(repository) {
            @Override
            public EntityType getEntity() {
                return EntityType.OFFICE;
            }
        };
        given(repository.findAll()).willReturn(Collections.emptyList());

        // when
        assertDoesNotThrow(() -> service.exportToFile(EXPORT_REQUEST));

        // then
        assertEquals(Collections.emptyList(), emptyListService.getDataToExport());
    }

    /**
     * {@link BaseExportXlsService#exportToFile(ExportRequest)}
     */
    @Test
    void Given_SearchOffices_Then_ExportToFile_CheckIsExportsBytesContainsCorrectData() {
        // given
        BaseExportXlsService<Office> emptyListService = new BaseExportXlsService<>(repository) {
            @Override
            public EntityType getEntity() {
                return EntityType.OFFICE;
            }
        };
        given(repository.findAll()).willReturn(List.of(OFFICE));

        // when
        byte[] exportsBytes = new byte[0];
        Workbook workbook = null;
        try {
            exportsBytes = emptyListService.exportToFile(EXPORT_REQUEST);
            ByteArrayInputStream bais = new ByteArrayInputStream(exportsBytes);
            workbook = new XSSFWorkbook(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet("OFFICE");

        Iterator<Cell> cellIterator = sheet.getRow(12).cellIterator();
        List<String> cellValues = new ArrayList<>();
        while (cellIterator.hasNext()) {
            cellValues.add(cellIterator.next().getStringCellValue());
        }
        String exportInfo = sheet.getRow(8).getCell(1).getStringCellValue();

        // then
        assertNotEquals(cellValues.get(0), OFFICE.getId().toString());
        assertEquals(exportInfo, EXPORT_REQUEST.getFileType().getFileTitle());

        assertEquals(cellValues.get(0), OFFICE.getOfficeTitle());
        assertEquals(cellValues.get(7), OFFICE.getOfficeDescription());

        assertEquals(cellValues.get(4), CONTACT.getContactPhoneNumber());
        assertEquals(cellValues.get(6), CONTACT.getContactSite());
    }
}