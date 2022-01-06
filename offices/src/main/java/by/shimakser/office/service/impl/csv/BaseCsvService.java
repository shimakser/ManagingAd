package by.shimakser.office.service.impl.csv;

import by.shimakser.office.exception.ExceptionOfficeText;
import by.shimakser.office.model.ExportOperationInfo;
import by.shimakser.office.model.Status;
import by.shimakser.office.service.BaseExportService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public abstract class BaseCsvService<T> extends BaseExportService<T> implements CsvService<T> {

    protected static ConcurrentMap<Long, ExportOperationInfo> statusOfImport = new ConcurrentHashMap<>();
    protected static ConcurrentMap<Long, ExportOperationInfo> statusOfExport = new ConcurrentHashMap<>();

    @Override
    public Status getStatusOfImportById(Long id) throws NotFoundException {
        ExportOperationInfo operationInfo = Optional.ofNullable(statusOfImport.get(id))
                .orElseThrow(() -> new NotFoundException(ExceptionOfficeText.NOT_FOUND.getExceptionDescription()));
        return operationInfo.getStatus();
    }

    @Override
    public Status getStatusOfExportById(Long id) throws NotFoundException {
        ExportOperationInfo operationInfo = Optional.ofNullable(statusOfExport.get(id))
                .orElseThrow(() -> new NotFoundException(ExceptionOfficeText.NOT_FOUND.getExceptionDescription()));
        return operationInfo.getStatus();
    }

    @Override
    public byte[] getExportedFileById(Long id) throws NotFoundException, IOException {
        ExportOperationInfo operationInfo = Optional.ofNullable(statusOfExport.get(id))
                .filter(info -> info.getStatus().equals(Status.UPLOADED))
                .orElseThrow(() -> new NotFoundException(ExceptionOfficeText.NOT_FOUND.getExceptionDescription()));

        return Files.readAllBytes(Path.of(operationInfo.getPath()));
    }
}
