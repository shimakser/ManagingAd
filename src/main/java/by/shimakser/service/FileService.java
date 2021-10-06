package by.shimakser.service;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.File;
import by.shimakser.repository.FileRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public File get(Long id) throws EntityNotFoundException {
        return fileRepository.findFileByIdAndFileDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));

    }

    @Transactional
    public List<File> getAll() {
        return fileRepository.findAllByFileDeletedFalse();
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public File update(Long id, File newFile) throws EntityNotFoundException {
        fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));

        newFile.setId(id);
        fileRepository.save(newFile);
        return newFile;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void delete(Long id) throws EntityNotFoundException {
        File fileById = fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
        fileById.setFileDeleted(Boolean.TRUE);
        fileRepository.save(fileById);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public File getDeletedFile(Long id) throws EntityNotFoundException {
        return fileRepository.findFileByIdAndFileDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
    }

    @Transactional
    public List<File> getDeletedFiles() {
        return fileRepository.findAllByFileDeletedTrue();
    }
}
