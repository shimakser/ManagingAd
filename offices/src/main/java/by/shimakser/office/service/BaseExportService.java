package by.shimakser.office.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseExportService<T> implements ExportService<T> {

    @Autowired
    private JpaRepository<T, Long> repository;

    protected static final String URL_TO_IMAGE = "https://i.redd.it/fsal3ipywty21.png";

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }
}
