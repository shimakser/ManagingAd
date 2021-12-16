package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import org.springframework.stereotype.Service;

@Service
public interface OfficeService {

    String[] OFFICES_FIELDS = new String[]{"id", "officeTitle", "officeAddress", "officePrice", "officeContacts", "officeDescription"};

    void importToFile(OfficeRequest officeRequest);
}
