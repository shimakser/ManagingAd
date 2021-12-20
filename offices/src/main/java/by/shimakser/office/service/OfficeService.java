package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import org.springframework.stereotype.Service;

@Service
public interface OfficeService {

    String[] OFFICES_FIELDS = new String[]{"id", "title", "address", "price", "contacts", "description"};
    String[] CONTACTS_FIELDS = new String[]{"id", "phoneNumber", "email", "site"};

    void importToFile(OfficeRequest officeRequest);
}
