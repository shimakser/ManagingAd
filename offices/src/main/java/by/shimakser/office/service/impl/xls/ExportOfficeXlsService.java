package by.shimakser.office.service.impl.xls;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.Office;
import org.springframework.stereotype.Service;

@Service
public class ExportOfficeXlsService extends BaseExportXlsService<Office> {

    @Override
    public EntityType getEntity() {
        return EntityType.OFFICE;
    }
}
