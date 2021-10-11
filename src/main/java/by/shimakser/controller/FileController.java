package by.shimakser.controller;

import by.shimakser.dto.FileDto;
import by.shimakser.mapper.FileMapper;
import by.shimakser.model.File;
import by.shimakser.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    private final FileMapper fileMapper;

    @Autowired
    public FileController(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @PostMapping
    public ResponseEntity<FileDto> addFile(@RequestBody FileDto fileDto) {
        File newFile = fileMapper.mapToEntity(fileDto);
        fileService.add(newFile);
        return new ResponseEntity<>(fileDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public FileDto getFileById(@PathVariable Long id) {
        return fileMapper.mapToDto(fileService.get(id));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping
    public List<FileDto> getAllFiles() {
        return fileMapper.mapToListDto(fileService.getAll());
    }

    @PutMapping(value = "/{id}")
    public FileDto updateFileById(@PathVariable("id") Long id,
                                  @RequestBody FileDto newFileDto) {
        File file = fileMapper.mapToEntity(newFileDto);
        fileService.update(id, file);
        return newFileDto;
    }

    @PreAuthorize("hasAuthority('user:write')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteFileById(@PathVariable("id") Long id) {
        fileService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping(value = "/deleted/{id}")
    public FileDto getDeletedFileById(@PathVariable Long id) {
        return fileMapper.mapToDto(fileService.getDeletedFile(id));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping(value = "/deleted")
    public List<FileDto> getAllDeletedFiles() {
        return fileMapper.mapToListDto(fileService.getDeletedFiles());
    }
}
