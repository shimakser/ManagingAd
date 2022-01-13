package by.shimakser.office.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseExportService<T> implements ExportService<T> {

    private final JpaRepository<T, Long> repository;

    protected BaseExportService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public Resource getImage() {
        return new ClassPathResource("static/logo.png");
    }

    @Override
    public List<T> getDataToExport() {
        return repository.findAll();
    }
}
