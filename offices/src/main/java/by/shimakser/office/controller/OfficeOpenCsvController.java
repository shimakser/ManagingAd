package by.shimakser.office.controller;

import by.shimakser.office.model.OfficeRequest;
import by.shimakser.office.service.csv.OfficeCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/offices/opencsv")
public class OfficeOpenCsvController {

    private final OfficeCsvService officeCsvService;

    @Autowired
    public OfficeOpenCsvController(@Qualifier("officeOpenCsvService") OfficeCsvService officeCsvService) {
        this.officeCsvService = officeCsvService;
    }

    @PostMapping("/export")
    public ResponseEntity<Long> exportFile(@RequestBody OfficeRequest officeRequest) throws FileNotFoundException {
        return new ResponseEntity<>(officeCsvService.exportFromFile(officeRequest), HttpStatus.CREATED);
    }

    @PostMapping("/import")
    public ResponseEntity<Long> importFile(@RequestBody OfficeRequest officeRequest) {
        return new ResponseEntity<>(officeCsvService.importToFile(officeRequest), HttpStatus.CREATED);
    }
}
