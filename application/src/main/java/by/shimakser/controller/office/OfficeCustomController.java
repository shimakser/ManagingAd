package by.shimakser.controller.office;

import by.shimakser.dto.CSVRequest;
import by.shimakser.model.office.Status;
import by.shimakser.service.office.OfficeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/offices")
public class OfficeCustomController {

    private final OfficeService officeService;

    @Autowired
    public OfficeCustomController(@Qualifier("officeCustomService") OfficeService officeService) {
        this.officeService = officeService;
    }

    @PostMapping("/export")
    public Long exportFile(@RequestBody CSVRequest csvRequest) throws FileNotFoundException {
        return officeService.exportFromFile(csvRequest);
    }

    @PostMapping("/import")
    public Long importFile(@RequestBody CSVRequest csvRequest) {
        return officeService.importToFile(csvRequest);
    }

    @GetMapping("/import/{id}")
    public Status getStatusOfImport(@PathVariable Long id) throws NotFoundException {
        return officeService.getStatusOfImportById(id);
    }

    @GetMapping("/export/{id}")
    public Status getStatusOfExport(@PathVariable Long id) throws NotFoundException {
        return officeService.getStatusOfExportById(id);
    }

    @GetMapping("/import/{id}/file")
    public String getImportedFile(@PathVariable Long id) throws NotFoundException {
        return officeService.getImportedFileById(id);
    }
}
