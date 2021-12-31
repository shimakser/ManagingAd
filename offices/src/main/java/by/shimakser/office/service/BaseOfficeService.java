package by.shimakser.office.service;

import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseOfficeService implements OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    protected static final String URL_TO_IMAGE = "https://i.redd.it/fsal3ipywty21.png";

    @Override
    public List<Office> getAll() {
        return officeRepository.findAll();
    }
}
