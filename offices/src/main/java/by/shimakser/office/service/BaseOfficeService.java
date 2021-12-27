package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public abstract class BaseOfficeService implements OfficeService {

    protected static final String URL_TO_IMAGE = "https://i.redd.it/fsal3ipywty21.png";
    protected static final String FILE_TITLE = "Offices export | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss"));

    protected String getExportFilePath(OfficeRequest officeRequest) {
        String importFileName = "/offices_export_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HH_mm_ss"));
        return officeRequest.getPathToFile() + importFileName;
    }
}
