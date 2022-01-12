package by.shimakser.office.service;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.ExportRequest;
import by.shimakser.office.model.FileType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ExportService<T> {

    byte[] exportToFile(ExportRequest exportRequest) throws IOException;

    List<T> getDataToExport();

    FileType getType();

    EntityType getEntity();
}
