package by.shimakser.service.office;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.office.OfficeOperationInfo;
import by.shimakser.model.office.Status;
import javassist.NotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class BaseOfficeService implements OfficeService {

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public Status getStatusOfImportById(Long id) throws NotFoundException {
        Status status = statusOfImport.get(id).getStatus();
        if (status == null) {
            throw new NotFoundException(ExceptionText.NOT_FOUND.getExceptionText());
        }
        return status;
    }

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public Status getStatusOfExportById(Long id) throws NotFoundException {
        Status status = statusOfExport.get(id).getStatus();
        if (status == null) {
            throw new NotFoundException(ExceptionText.NOT_FOUND.getExceptionText());
        }
        return status;
    }

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public FileSystemResource getExportedFileById(Long id) throws NotFoundException {
        OfficeOperationInfo operationInfo = statusOfExport.get(id);
        Status status = operationInfo.getStatus();

        System.out.println(statusOfExport);
        if (status == null || !status.equals(Status.UPLOADED)) {
            throw new NotFoundException(ExceptionText.NOT_FOUND.getExceptionText());
        }
        return new FileSystemResource(operationInfo.getPath());
    }
}
