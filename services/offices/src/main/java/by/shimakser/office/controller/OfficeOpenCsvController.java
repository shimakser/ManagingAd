package by.shimakser.office.controller;

import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.service.impl.csv.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/offices/opencsv")
public class OfficeOpenCsvController {

    private final CsvService<Office> csvService;

    @Autowired
    public OfficeOpenCsvController(@Qualifier("officeOpenCsvService") CsvService<Office> csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/import")
    public ResponseEntity<Long> importFile(@RequestBody ExportRequest exportRequest) throws FileNotFoundException {
        return new ResponseEntity<>(csvService.importFromFile(exportRequest), HttpStatus.CREATED);
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportFile(@RequestBody ExportRequest exportRequest) throws IOException {
        return new ResponseEntity<>(csvService.exportToFile(exportRequest), HttpStatus.CREATED);
    }
}
