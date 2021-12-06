package by.shimakser.service.office;

import by.shimakser.dto.CSVRequest;
import by.shimakser.model.office.Status;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public interface OfficeService {

    Long exportFromFile(CSVRequest csvRequest) throws FileNotFoundException;

    Long importToFile(CSVRequest csvRequest);

    Status getStatusOfImportById(Long id) throws NotFoundException;

    Status getStatusOfExportById(Long id) throws NotFoundException;

    String getImportedFileById(Long id) throws NotFoundException;
}
