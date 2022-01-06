package by.shimakser.office.controller;

import by.shimakser.dto.OfficeCsvOperationNumber;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.Status;
import by.shimakser.office.service.impl.csv.CsvService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/offices")
public class OfficeCustomController {

    private final CsvService csvService;

    @Autowired
    public OfficeCustomController(@Qualifier("officeCustomService") CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/csv/import")
    public EntityModel<OfficeCsvOperationNumber> importFile(@RequestBody ExportRequest exportRequest) throws IOException, NotFoundException {
        Long numberOfOperation = csvService.importFromFile(exportRequest);
        Link link = linkTo(methodOn(OfficeCustomController.class).getStatusOfImport(numberOfOperation)).withSelfRel();
        return EntityModel.of(new OfficeCsvOperationNumber(numberOfOperation), link);
    }

    @PostMapping("/csv/export")
    public ResponseEntity<byte[]> exportFile(@RequestBody ExportRequest exportRequest) throws IOException {
        String title = "OfficeExport" + ".csv";
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=%s", title))
                .body(csvService.exportToFile(exportRequest));
    }

    @GetMapping("/import/{id}")
    public ResponseEntity<Status> getStatusOfImport(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(csvService.getStatusOfImportById(id), HttpStatus.OK);
    }

    @GetMapping("/export/{id}")
    public EntityModel<String> getStatusOfExport(@PathVariable Long id) throws IOException, NotFoundException {
        Status status = csvService.getStatusOfExportById(id);
        Link link = linkTo(methodOn(OfficeCustomController.class).getExportedFile(id)).withSelfRel();
        return EntityModel.of(status.toString(), link);
    }

    @GetMapping("/export/{id}/file")
    public ResponseEntity<byte[]> getExportedFile(@PathVariable Long id) throws NotFoundException, IOException {
        String title = "OfficeExport_" + id + ".csv";
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("application/vnd.ms-excel"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=%s", title))
                .body(csvService.getExportedFileById(id));
    }
}
