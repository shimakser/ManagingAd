package by.shimakser.service;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.File;
import by.shimakser.repository.FileRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public File add(File file) {
        LocalDateTime date = LocalDateTime.now();
        file.setFileCreatedDate(date);

        fileRepository.save(file);
        return file;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public File get(Long id) throws NotFoundException {
        return fileRepository.findFileByIdAndFileDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(ExceptionText.NotFound.getExceptionText()));

    }

    @Transactional
    public List<File> getAll() {
        return fileRepository.findAllByFileDeletedFalse();
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public File update(Long id, File newFile) throws NotFoundException {
        fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionText.NotFound.getExceptionText()));

        newFile.setId(id);
        fileRepository.save(newFile);
        return newFile;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public void delete(Long id) throws NotFoundException {
        File fileById = fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionText.NotFound.getExceptionText()));
        fileById.setFileDeleted(Boolean.TRUE);
        fileRepository.save(fileById);
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public File getDeletedFile(Long id) throws NotFoundException {
        return fileRepository.findFileByIdAndFileDeletedTrue(id)
                .orElseThrow(() -> new NotFoundException(ExceptionText.NotFound.getExceptionText()));
    }

    @Transactional
    public List<File> getDeletedFiles() {
        return fileRepository.findAllByFileDeletedTrue();
    }
}
