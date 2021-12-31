package by.shimakser.office.service.csv;

import by.shimakser.office.exception.ExceptionOfficeText;
import by.shimakser.office.model.OfficeOperationInfo;
import by.shimakser.office.model.Status;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public abstract class BaseOfficeCsvService implements OfficeCsvService {

    protected static ConcurrentMap<Long, OfficeOperationInfo> statusOfImport = new ConcurrentHashMap<>();
    protected static ConcurrentMap<Long, OfficeOperationInfo> statusOfExport = new ConcurrentHashMap<>();

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public Status getStatusOfImportById(Long id) throws NotFoundException {
        OfficeOperationInfo operationInfo = Optional.ofNullable(statusOfImport.get(id))
                .orElseThrow(() -> new NotFoundException(ExceptionOfficeText.NOT_FOUND.getExceptionDescription()));
        return operationInfo.getStatus();
    }

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public Status getStatusOfExportById(Long id) throws NotFoundException {
        OfficeOperationInfo operationInfo = Optional.ofNullable(statusOfExport.get(id))
                .orElseThrow(() -> new NotFoundException(ExceptionOfficeText.NOT_FOUND.getExceptionDescription()));
        return operationInfo.getStatus();
    }

    @Override
    @Transactional(rollbackFor = {NullPointerException.class, NotFoundException.class})
    public byte[] getExportedFileById(Long id) throws NotFoundException, IOException {
        OfficeOperationInfo operationInfo = Optional.ofNullable(statusOfExport.get(id))
                .filter(info -> info.getStatus().equals(Status.UPLOADED))
                .orElseThrow(() -> new NotFoundException(ExceptionOfficeText.NOT_FOUND.getExceptionDescription()));


        return Files.readAllBytes(Path.of(operationInfo.getPath()));
    }
}
