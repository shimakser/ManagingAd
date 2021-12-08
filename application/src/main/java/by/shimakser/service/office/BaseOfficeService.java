package by.shimakser.service.office;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.office.OfficeOperationInfo;
import by.shimakser.model.office.Status;
import javassist.NotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public abstract class BaseOfficeService implements OfficeService {

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public Status getStatusOfImportById(Long id) throws NotFoundException {
        OfficeOperationInfo operationInfo = Optional.ofNullable(statusOfImport.get(id))
                .orElseThrow(() -> new NotFoundException(ExceptionText.NOT_FOUND.getExceptionText()));
        return operationInfo.getStatus();
    }

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public Status getStatusOfExportById(Long id) throws NotFoundException {
        OfficeOperationInfo operationInfo = Optional.ofNullable(statusOfExport.get(id))
                .orElseThrow(() -> new NotFoundException(ExceptionText.NOT_FOUND.getExceptionText()));
        return operationInfo.getStatus();
    }

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public FileSystemResource getExportedFileById(Long id) throws NotFoundException {
        OfficeOperationInfo operationInfo = Optional.ofNullable(statusOfExport.get(id))
                .filter(info -> info.getStatus().equals(Status.UPLOADED))
                .orElseThrow(() -> new NotFoundException(ExceptionText.NOT_FOUND.getExceptionText()));
        return new FileSystemResource(operationInfo.getPath());
    }
}
