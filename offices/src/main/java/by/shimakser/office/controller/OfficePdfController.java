package by.shimakser.office.controller;

import by.shimakser.dto.OfficeRequest;
import by.shimakser.office.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offices/pdf")
public class OfficePdfController {

    private final OfficeService officeService;

    @Autowired
    public OfficePdfController(@Qualifier("officePdfService") OfficeService officeService) {
        this.officeService = officeService;
    }

    @PostMapping("/import")
    public ResponseEntity<HttpStatus> exportFromFileToXls(@RequestBody OfficeRequest officeRequest) {
        officeService.importToFile(officeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
