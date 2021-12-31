package by.shimakser.office.controller;

import by.shimakser.office.model.FileType;
import by.shimakser.office.model.OfficeRequest;
import by.shimakser.office.service.OfficeService;
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

    private final Dispatcher<FileType, OfficeService> dispatcher;

    @Autowired
    public ExportToFileController(Dispatcher<FileType, OfficeService> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> exportToFile(@RequestBody OfficeRequest officeRequest) throws IOException {
        FileType fileType = officeRequest.getFileType();
        String title = fileType.getFileTitle() + fileType.getFileExtension();
        System.out.println(title);
        return ResponseEntity
                .ok()
                .contentType(officeRequest.getFileType().getMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=%s", title))
                .body(dispatcher.getByName(fileType).exportToFile(fileType));
    }
}
