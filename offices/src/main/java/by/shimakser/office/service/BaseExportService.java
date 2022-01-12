package by.shimakser.office.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseExportService<T> implements ExportService<T> {

    private final JpaRepository<T, Long> repository;

    public BaseExportService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    protected static final String URL_TO_IMAGE = "offices/src/main/resources/static/logo.png";

    @Override
    public List<T> getDataToExport() {
        return repository.findAll();
    }
}
