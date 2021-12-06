package by.shimakser.service.office;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.office.OfficeOperationInfo;
import by.shimakser.model.office.Status;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public abstract class BaseOfficeService implements OfficeService {

    protected final Map<AtomicLong, OfficeOperationInfo> statusOfImport = new HashMap<>();
    protected final Map<AtomicLong, OfficeOperationInfo> statusOfExport = new HashMap<>();

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Status getStatusOfImportById(Long id) throws NotFoundException {
        Status status = statusOfImport.get(new AtomicLong(id)).getStatus();
        if (status == null) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return status;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Status getStatusOfExportById(Long id) throws NotFoundException {
        Status status = statusOfExport.get(new AtomicLong(id)).getStatus();
        if (status == null) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return status;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public String getImportedFileById(Long id) throws NotFoundException {
        OfficeOperationInfo operationInfo = statusOfImport.get(new AtomicLong(id));
        Status status = operationInfo.getStatus();
        if (status == null || !status.equals(Status.Uploaded)) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return operationInfo.getPath();
    }
}
