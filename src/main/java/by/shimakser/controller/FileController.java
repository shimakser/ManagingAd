package by.shimakser.controller;

import by.shimakser.model.FileRequest;
import by.shimakser.model.Status;
import by.shimakser.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/offices")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/export")
    public Long exportFile(@RequestBody FileRequest fileRequest) throws FileNotFoundException {
        return fileService.exportFromFile(fileRequest);
    }

    @PostMapping("/import")
    public Long importFile(@RequestBody FileRequest fileRequest) {
        return fileService.importToFile(fileRequest);
    }

    @GetMapping("/import/{id}")
    public Status getStatusOfImport(@PathVariable Long id) {
        return fileService.getStatusOfImportById(id);
    }

    @GetMapping("/export/{id}")
    public Status getStatusOfExport(@PathVariable Long id) {
        return fileService.getStatusOfExportById(id);
    }
}
