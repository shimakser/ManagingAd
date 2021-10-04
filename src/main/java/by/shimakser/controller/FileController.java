package by.shimakser.controller;

import by.shimakser.model.FileRequest;
import by.shimakser.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
