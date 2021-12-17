package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import org.springframework.stereotype.Service;

@Service
public interface OfficeService {

    String[] OFFICES_FIELDS = new String[]{"id", "officeTitle", "officeAddress", "officePrice", "officeContacts", "officeDescription"};
    String[] CONTACTS_FIELDS = new String[]{"id", "contactPhoneNumber", "contactEmail", "contactSite"};

    void importToFile(OfficeRequest officeRequest);
}
