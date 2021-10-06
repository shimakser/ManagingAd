package by.shimakser.controller;

import by.shimakser.model.CSVRequest;
import by.shimakser.model.Status;
import by.shimakser.service.CSVService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/offices")
public class CSVController {

    private final CSVService csvService;

    @Autowired
    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/export")
    public Long exportFile(@RequestBody CSVRequest csvRequest) throws FileNotFoundException {
        return csvService.exportFromFile(csvRequest);
    }

    @PostMapping("/import")
    public Long importFile(@RequestBody CSVRequest csvRequest) {
        return csvService.importToFile(csvRequest);
    }

    @GetMapping("/import/{id}")
    public Status getStatusOfImport(@PathVariable Long id) throws NotFoundException {
        return csvService.getStatusOfImportById(id);
    }

    @GetMapping("/export/{id}")
    public Status getStatusOfExport(@PathVariable Long id) throws NotFoundException {
        return csvService.getStatusOfExportById(id);
    }

    @GetMapping("/import/{id}/file")
    public String getImportedFile(@PathVariable Long id) throws NotFoundException {
        return csvService.getImportedFileById(id);
    }
}
