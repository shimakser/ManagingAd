package by.shimakser.office.service.impl.csv;

import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OfficeOpenCsvServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeOpenCsvService officeOpenCsvService;

    private Office office;

    @Test
    void containsCorrectDataAfterExport() throws IOException {
        office = new Office(1L, "title", "address", 1D,
                Collections.emptyList(), "{}");
        Mockito.when(officeRepository.findAll()).thenReturn(List.of(office));

        assertDoesNotThrow(() -> officeOpenCsvService.exportToFile(new ExportRequest()));

        byte[] exportsBytes = officeOpenCsvService.exportToFile(new ExportRequest());

        String exportedOffice = "\"1\",\"title\",\"address\",\"1.0\",\"[]\",\"{}\"\n";
        String exportsBytesToString = new String(exportsBytes, StandardCharsets.UTF_8);
        assertEquals(exportsBytesToString, exportedOffice);

        CSVReader reader = new CSVReader(new StringReader(exportsBytesToString));
        String[] record = null;
        Office officeFromBytes = null;
        while ((record = reader.readNext()) != null) {
            officeFromBytes = new Office(Long.parseLong(record[0]), record[1], record[2], Double.parseDouble(record[3]),
                    Collections.emptyList(), record[5]);
        }
        assertEquals(officeFromBytes, office);
    }
}