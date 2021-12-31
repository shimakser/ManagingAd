package by.shimakser.office.service;

import by.shimakser.office.model.FileType;
import by.shimakser.office.model.Office;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface OfficeService {

    byte[] exportToFile(FileType fileType) throws IOException;

    List<Office> getAll();

    FileType getType();
}
