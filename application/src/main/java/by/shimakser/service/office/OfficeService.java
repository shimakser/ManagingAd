package by.shimakser.service.office;

import by.shimakser.dto.CSVRequest;
import by.shimakser.model.office.OfficeOperationInfo;
import by.shimakser.model.office.Status;
import javassist.NotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public interface OfficeService {

    ConcurrentMap<Long, OfficeOperationInfo> statusOfImport = new ConcurrentHashMap<>();
    ConcurrentMap<Long, OfficeOperationInfo> statusOfExport = new ConcurrentHashMap<>();
    AtomicLong ID_OF_OPERATION = new AtomicLong(0);

    Long exportFromFile(CSVRequest csvRequest) throws FileNotFoundException;

    Long importToFile(CSVRequest csvRequest);

    Status getStatusOfImportById(Long id) throws NotFoundException;

    Status getStatusOfExportById(Long id) throws NotFoundException;

    FileSystemResource getExportedFileById(Long id) throws NotFoundException;
}
