package by.shimakser.controller.office;

import by.shimakser.dto.CSVRequest;
import by.shimakser.service.office.OfficeService;
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
@RequestMapping("/offices")
public class OfficeOpenCsvController {

    private final OfficeService officeService;

    @Autowired
    public OfficeOpenCsvController(@Qualifier("officeOpenCsvService") OfficeService officeService) {
        this.officeService = officeService;
    }

    @PostMapping("/export/opencsv")
    public ResponseEntity<Long> exportFile(@RequestBody CSVRequest csvRequest) throws FileNotFoundException {
        return new ResponseEntity<>(officeService.exportFromFile(csvRequest), HttpStatus.CREATED);
    }

    @PostMapping("/import/opencsv")
    public ResponseEntity<Long> importFile(@RequestBody CSVRequest csvRequest) {
        return new ResponseEntity<>(officeService.importToFile(csvRequest), HttpStatus.CREATED);
    }
}
