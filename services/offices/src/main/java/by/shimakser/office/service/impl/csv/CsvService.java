package by.shimakser.office.service.impl.csv;

import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.Status;
import by.shimakser.office.service.ExportService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public interface CsvService<T> extends ExportService<T> {

    AtomicLong ID_OF_OPERATION = new AtomicLong(0);

    Long importFromFile(ExportRequest exportRequest) throws FileNotFoundException;

    Status getStatusOfImportById(Long id) throws NotFoundException;

    Status getStatusOfExportById(Long id) throws NotFoundException;

    byte[] getExportedFileById(Long id) throws NotFoundException, IOException;
}
