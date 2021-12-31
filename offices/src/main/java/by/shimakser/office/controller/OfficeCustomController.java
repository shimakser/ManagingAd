package by.shimakser.office.controller;

import by.shimakser.office.model.OfficeRequest;
import by.shimakser.office.model.Status;
import by.shimakser.office.service.csv.OfficeCsvService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/offices")
public class OfficeCustomController {

    private final OfficeCsvService officeCsvService;

    @Autowired
    public OfficeCustomController(@Qualifier("officeCustomService") OfficeCsvService officeCsvService) {
        this.officeCsvService = officeCsvService;
    }

    @PostMapping("/csv/export")
    public ResponseEntity<Long> exportFile(@RequestBody OfficeRequest officeRequest) throws FileNotFoundException {
        return new ResponseEntity<>(officeCsvService.exportFromFile(officeRequest), HttpStatus.OK);
    }

    @PostMapping("/csv/import")
    public ResponseEntity<Long> importFile(@RequestBody OfficeRequest officeRequest) {
        return new ResponseEntity<>(officeCsvService.importToFile(officeRequest), HttpStatus.CREATED);
    }

    @GetMapping("/import/{id}")
    public ResponseEntity<Status> getStatusOfImport(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(officeCsvService.getStatusOfImportById(id), HttpStatus.OK);
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<Status> getStatusOfExport(@PathVariable Long id) throws NotFoundException {
        Status status = officeCsvService.getStatusOfExportById(id);
        if (status.equals(Status.UPLOADED)) {
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create("/offices/export/" + id + "/file")).build();
        } else return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/export/{id}/file")
    public ResponseEntity<byte[]> getExportedFile(@PathVariable Long id) throws NotFoundException, IOException {
        return new ResponseEntity<>(officeCsvService.getExportedFileById(id), HttpStatus.OK);
    }
}
