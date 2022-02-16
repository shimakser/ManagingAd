package by.shimakser.office.service.impl.csv;

import by.shimakser.office.model.Contact;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.assertj.core.api.BDDAssertions;
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

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class OfficeCustomCsvServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeCustomCsvService officeCustomCsvService;

    /**
     * {@link OfficeCustomCsvService#exportToFile(ExportRequest)}
     */
    @Test
    void Given_SearchOffices_When_ExportToFile_Then_CheckIsExportsBytesContainsCorrectData() {
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

    /**
     * {@link OfficeCustomCsvService#contactConverterForImport(String)}
     */
    @Test
    void Given_SetStringsAndContactsForConvert_When_ConvertStringsToContacts_Then_CheckIsCorrectlyConvertedContacts() {
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

    /**
     * {@link OfficeCustomCsvService#jsonConvertForImport(String)}
     */
    @Test
    void Given_SetStringsForConvert_When_ConvertStringToJson_Then_CheckIsCorrectlyConvertedJson() {
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

    /**
     * {@link OfficeCustomCsvService#importFromFile(ExportRequest)}
     */
    @Test
    void Given_SetRequest_When_ImportFromFile_Then_CatchExceptionByNotExistFile() {
        // given
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setPathToFile("/home/");

        // when
        BDDCatchException.when(() -> officeCustomCsvService.importFromFile(exportRequest));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(FileNotFoundException.class);
    }
}