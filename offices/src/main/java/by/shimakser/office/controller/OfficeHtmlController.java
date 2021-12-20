package by.shimakser.office.controller;

import by.shimakser.dto.OfficeRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.service.OfficeHtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/offices/html")
public class OfficeHtmlController {

    private final OfficeHtmlService officeHtmlService;

    @Autowired
    public OfficeHtmlController(OfficeHtmlService officeHtmlService) {
        this.officeHtmlService = officeHtmlService;
    }

    @GetMapping
    public String getOfficesPage(Model model) {
        List<Office> offices = officeHtmlService.getOffices();
        String header = "Offices import | " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss"));

        model.addAttribute("header", header);
        model.addAttribute("offices", offices);
        return "office";
    }

    @PostMapping("/import")
    public ResponseEntity<HttpStatus> importFromDBToPdf(@RequestBody OfficeRequest officeRequest) {
        officeHtmlService.importToFile(officeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
