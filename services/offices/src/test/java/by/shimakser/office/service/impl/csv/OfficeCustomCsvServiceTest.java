package by.shimakser.office.service.impl.csv;

import by.shimakser.office.model.Contact;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@SpringBootTest
class OfficeCustomCsvServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeCustomCsvService officeCustomCsvService;

    @Test
    void containsCorrectDataAfterExport() {
        // given
        Office office = new Office(1L, "title", "address", 1D,
                Collections.emptyList(), "{}");
        String exportedOffice = "Office(1, title, address, 1.0, [], {})\n";
        given(officeRepository.findAll()).willReturn(List.of(office));

        // when
        byte[] exportsBytes = new byte[0];
        try {
            exportsBytes = officeCustomCsvService.exportToFile(new ExportRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String exportsBytesToString = new String(exportsBytes, StandardCharsets.UTF_8);

        // then
        assertEquals(exportsBytesToString, exportedOffice);
    }

    @Test
    void contactConverterForImport() {
        // given
        String emptyInput = "[]";
        String inputContact = "[Contact(1, +375123456789, test1, test1)]";
        String inputContacts = "[Contact(1, +375123456789, test1, test1), Contact(2, +375987654321, test2, test2)]";
        Contact firstOutputContact = new Contact(1L, "+375123456789", "test1", "test1");
        Contact secondOutputContact = new Contact(2L, "+375987654321", "test2", "test2");

        // when
        List<Contact> emptyContactsList = officeCustomCsvService.contactConverterForImport(emptyInput);
        List<Contact> oneContactList = officeCustomCsvService.contactConverterForImport(inputContact);
        List<Contact> contactsList = officeCustomCsvService.contactConverterForImport(inputContacts);

        // then
        assertEquals(Collections.emptyList(), emptyContactsList);
        assertEquals(oneContactList, List.of(firstOutputContact));
        assertEquals(contactsList, List.of(firstOutputContact, secondOutputContact));
    }

    @Test
    void jsonConvertForImport() {
        // given
        String emptyDescription = "null";
        String description = "{\"1\":\"description\"}";

        // when
        JSONObject emptyJsonObject = officeCustomCsvService.jsonConvertForImport(emptyDescription);
        JSONObject jsonObject = officeCustomCsvService.jsonConvertForImport(description);

        // then
        assertEquals("{}", emptyJsonObject.toString());
        assertEquals(jsonObject.toString(), description);
    }

    @Test
    void importFromFile_WithoutFile() {
        // given
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setPathToFile("/home/");

        // then
        assertThrows(FileNotFoundException.class, () -> officeCustomCsvService.importFromFile(exportRequest));
        then(officeRepository)
                .should(never())
                .save(new Office());
    }
}