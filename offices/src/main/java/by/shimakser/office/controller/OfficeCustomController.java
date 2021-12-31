package by.shimakser.office.controller;

import by.shimakser.dto.OfficeCsvOperationNumber;
import by.shimakser.office.model.OfficeRequest;
import by.shimakser.office.model.Status;
import by.shimakser.office.service.csv.OfficeCsvService;
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

    private final OfficeCsvService officeCsvService;

    @Autowired
    public OfficeCustomController(@Qualifier("officeCustomService") OfficeCsvService officeCsvService) {
        this.officeCsvService = officeCsvService;
    }

    @PostMapping("/csv/export")
    public EntityModel<OfficeCsvOperationNumber> exportFile(@RequestBody OfficeRequest officeRequest) throws IOException, NotFoundException {
        Long numberOfOperation = officeCsvService.exportFromFile(officeRequest);
        Link link = linkTo(methodOn(OfficeCustomController.class).getStatusOfExport(numberOfOperation)).withSelfRel();
        return EntityModel.of(new OfficeCsvOperationNumber(numberOfOperation), link);
    }

    @PostMapping("/csv/import")
    public EntityModel<OfficeCsvOperationNumber> importFile(@RequestBody OfficeRequest officeRequest) throws NotFoundException, IOException {
        Long numberOfOperation = officeCsvService.importToFile(officeRequest);
        Link link = linkTo(methodOn(OfficeCustomController.class).getStatusOfImport(numberOfOperation)).withSelfRel();
        return EntityModel.of(new OfficeCsvOperationNumber(numberOfOperation), link);
    }

    @GetMapping("/import/{id}")
    public ResponseEntity<Status> getStatusOfImport(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(officeCsvService.getStatusOfImportById(id), HttpStatus.OK);
    }

    @GetMapping("/export/{id}")
    public EntityModel<String> getStatusOfExport(@PathVariable Long id) throws IOException, NotFoundException {
        Status status = officeCsvService.getStatusOfExportById(id);

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
                .body(officeCsvService.getExportedFileById(id));
    }
}
