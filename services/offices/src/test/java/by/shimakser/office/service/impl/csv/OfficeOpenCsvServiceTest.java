package by.shimakser.office.service.impl.csv;

import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class OfficeOpenCsvServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeOpenCsvService officeOpenCsvService;

    @Test
    void containsCorrectDataAfterExport() throws IOException {
        Office office = new Office(1L, "title", "address", 1D,
                Collections.emptyList(), "{}");
        String exportedOffice = "\"1\",\"title\",\"address\",\"1.0\",\"[]\",\"{}\"\n";

        given(officeRepository.findAll()).willReturn(List.of(office));

        byte[] exportsBytes = officeOpenCsvService.exportToFile(new ExportRequest());

        String exportsBytesToString = new String(exportsBytes, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(new StringReader(exportsBytesToString));
        String[] record = null;
        Office officeFromBytes = null;
        while ((record = reader.readNext()) != null) {
            officeFromBytes = new Office(Long.parseLong(record[0]), record[1], record[2],
                    Double.parseDouble(record[3]), Collections.emptyList(), record[5]);
        }

        assertEquals(officeFromBytes, office);
        assertEquals(exportsBytesToString, exportedOffice);
    }
}