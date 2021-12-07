package by.shimakser.controller.office;

import by.shimakser.dto.CSVRequest;
import by.shimakser.model.office.Status;
import by.shimakser.service.office.OfficeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.net.URI;

@RestController
@RequestMapping("/offices")
public class OfficeCustomController {

    private final OfficeService officeService;

    @Autowired
    public OfficeCustomController(@Qualifier("officeCustomService") OfficeService officeService) {
        this.officeService = officeService;
    }

    @PostMapping("/export")
    public ResponseEntity<Long> exportFile(@RequestBody CSVRequest csvRequest) throws FileNotFoundException {
        return new ResponseEntity<>(officeService.exportFromFile(csvRequest), HttpStatus.OK);
    }

    @PostMapping("/import")
    public ResponseEntity<Long> importFile(@RequestBody CSVRequest csvRequest) {
        return new ResponseEntity<>(officeService.importToFile(csvRequest), HttpStatus.CREATED);
    }

    @GetMapping("/import/{id}")
    public ResponseEntity<Status> getStatusOfImport(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(officeService.getStatusOfImportById(id), HttpStatus.OK);
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<Status> getStatusOfExport(@PathVariable Long id) throws NotFoundException {
        Status status = officeService.getStatusOfExportById(id);
        if (status.equals(Status.UPLOADED)) {
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create("/managingad/offices/export/" + id + "/file")).build();
        } else return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/export/{id}/file")
    public ResponseEntity<FileSystemResource> getExportedFile(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(officeService.getExportedFileById(id), HttpStatus.OK);
    }
}
