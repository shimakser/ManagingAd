package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import org.springframework.stereotype.Service;

@Service
public interface OfficeService {

    void exportToFile(OfficeRequest officeRequest);
}
