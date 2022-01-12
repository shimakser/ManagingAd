package by.shimakser.office.service.impl.xls;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ExportOfficeXlsService extends BaseExportXlsService<Office> {

    public ExportOfficeXlsService(JpaRepository<Office, Long> repository) {
        super(repository);
    }

    @Override
    public EntityType getEntity() {
        return EntityType.OFFICE;
    }
}
