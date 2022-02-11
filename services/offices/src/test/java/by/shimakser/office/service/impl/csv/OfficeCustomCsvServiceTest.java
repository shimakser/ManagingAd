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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class OfficeCustomCsvServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeCustomCsvService officeCustomCsvService;

    @Test
    void containsCorrectDataAfterExport() throws IOException {
        Office office = new Office(1L, "title", "address", 1D,
                Collections.emptyList(), "{}");
        String exportedOffice = "Office(1, title, address, 1.0, [], {})\n";

        given(officeRepository.findAll()).willReturn(List.of(office));

        byte[] exportsBytes = officeCustomCsvService.exportToFile(new ExportRequest());
        String exportsBytesToString = new String(exportsBytes, StandardCharsets.UTF_8);

        assertEquals(exportsBytesToString, exportedOffice);
    }

    @Test
    void contactConverterForImport() {
        String emptyInput = "[]";
        String inputContact = "[Contact(1, +375123456789, test1, test1)]";
        String inputContacts = "[Contact(1, +375123456789, test1, test1), Contact(2, +375987654321, test2, test2)]";

        Contact firstOutputContact = new Contact(1L, "+375123456789", "test1", "test1");
        Contact secondOutputContact = new Contact(2L, "+375987654321", "test2", "test2");

        List<Contact> emptyContactsList = officeCustomCsvService.contactConverterForImport(emptyInput);
        List<Contact> oneContactList = officeCustomCsvService.contactConverterForImport(inputContact);
        List<Contact> contactsList = officeCustomCsvService.contactConverterForImport(inputContacts);

        assertEquals(emptyContactsList, Collections.emptyList());
        assertEquals(oneContactList, List.of(firstOutputContact));
        assertEquals(contactsList, List.of(firstOutputContact, secondOutputContact));
    }

    @Test
    void jsonConvertForImport() {
        String emptyDescription = "null";
        String description = "{\"1\":\"description\"}";

        JSONObject emptyJsonObject = officeCustomCsvService.jsonConvertForImport(emptyDescription);
        JSONObject jsonObject = officeCustomCsvService.jsonConvertForImport(description);

        assertEquals(emptyJsonObject.toString(), "{}");
        assertEquals(jsonObject.toString(), description);
    }
}