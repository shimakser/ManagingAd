package by.shimakser.office.service.impl.xls;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.Contact;
import org.springframework.stereotype.Service;

@Service
public class ExportContactXlsService extends BaseExportXlsService<Contact> {

    @Override
    public EntityType getEntity() {
        return EntityType.CONTACT;
    }
}
