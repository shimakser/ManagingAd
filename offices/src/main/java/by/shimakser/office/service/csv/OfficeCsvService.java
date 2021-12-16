package by.shimakser.office.service.csv;

import by.shimakser.dto.OfficeRequest;
import by.shimakser.office.model.Status;
import javassist.NotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public interface OfficeCsvService {

    AtomicLong ID_OF_OPERATION = new AtomicLong(0);

    Long exportFromFile(OfficeRequest officeRequest) throws FileNotFoundException;

    Long importToFile(OfficeRequest officeRequest);

    Status getStatusOfImportById(Long id) throws NotFoundException;

    Status getStatusOfExportById(Long id) throws NotFoundException;

    FileSystemResource getExportedFileById(Long id) throws NotFoundException;
}
