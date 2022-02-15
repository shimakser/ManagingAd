package by.shimakser.office.service.impl.csv;

import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@SpringBootTest
class OfficeOpenCsvServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeOpenCsvService officeOpenCsvService;

    @Test
    void containsCorrectDataAfterExport() {
        // given
        Office office = new Office(1L, "title", "address", 1D,
                Collections.emptyList(), "{}");
        String exportedOffice = "\"1\",\"title\",\"address\",\"1.0\",\"[]\",\"{}\"\n";
        given(officeRepository.findAll()).willReturn(List.of(office));

        // when
        byte[] exportsBytes = new byte[0];
        try {
            exportsBytes = officeOpenCsvService.exportToFile(new ExportRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String exportsBytesToString = new String(exportsBytes, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(new StringReader(exportsBytesToString));
        String[] record = null;
        Office officeFromBytes = null;
        while (true) {
            try {
                if ((record = reader.readNext()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            officeFromBytes = new Office(Long.parseLong(record[0]), record[1], record[2],
                    Double.parseDouble(record[3]), Collections.emptyList(), record[5]);
        }

        // then
        assertEquals(officeFromBytes, office);
        assertEquals(exportsBytesToString, exportedOffice);
    }

    @Test
    void importFromFile_WithoutFile() {
        // given
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setPathToFile("/home/");

        // then
        assertThrows(FileNotFoundException.class, () -> officeOpenCsvService.importFromFile(exportRequest));
        then(officeRepository)
                .should(never())
                .save(new Office());
    }
}