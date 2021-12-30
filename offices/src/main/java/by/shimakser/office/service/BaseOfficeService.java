package by.shimakser.office.service;

import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public abstract class BaseOfficeService implements OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    protected static final String URL_TO_IMAGE = "https://i.redd.it/fsal3ipywty21.png";
    protected static final String FILE_TITLE = "Offices export | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss"));

    @Override
    public List<Office> getAll() {
        return officeRepository.findAll();
    }
}
