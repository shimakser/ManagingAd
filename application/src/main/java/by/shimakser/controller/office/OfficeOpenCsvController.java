package by.shimakser.controller.office;

import by.shimakser.model.office.CSVRequest;
import by.shimakser.service.office.OfficeOpenCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offices")
public class OfficeOpenCsvController {

    private final OfficeOpenCsvService officeOpenCsvService;

    @Autowired
    public OfficeOpenCsvController(OfficeOpenCsvService officeOpenCsvService) {
        this.officeOpenCsvService = officeOpenCsvService;
    }

    @PostMapping("/export/opencsv")
    public Long exportFile(@RequestBody CSVRequest csvRequest) {
        return officeOpenCsvService.exportFromFile(csvRequest);
    }

    @PostMapping("/import/opencsv")
    public Long importFile(@RequestBody CSVRequest csvRequest) {
        return officeOpenCsvService.importToFile(csvRequest);
    }
}
