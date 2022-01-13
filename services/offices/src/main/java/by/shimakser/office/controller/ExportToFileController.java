package by.shimakser.office.controller;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import by.shimakser.office.service.ExportService;
import by.shimakser.office.service.dispatcher.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/offices/export")
public class ExportToFileController {

    private final Dispatcher<EntityType, FileType, ExportService<?>> dispatcher;

    @Autowired
    public ExportToFileController(Dispatcher<EntityType, FileType, ExportService<?>> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> exportToFile(@RequestBody ExportRequest exportRequest) throws IOException {
        FileType fileType = exportRequest.getFileType();
        EntityType entityType = exportRequest.getEntityType();
        String title = entityType.getClazz().getSimpleName() + fileType.getFileTitle() + fileType.getFileExtension();

        return ResponseEntity
                .ok()
                .contentType(exportRequest.getFileType().getMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=%s", title))
                .body(dispatcher
                        .getByEntityAndExportType(entityType, fileType)
                        .exportToFile(exportRequest));
    }
}
