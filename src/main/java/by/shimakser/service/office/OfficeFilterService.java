package by.shimakser.service.office;

import by.shimakser.filter.office.OfficeFilterRequest;
import by.shimakser.model.office.Office;
import by.shimakser.repository.office.OfficeRepository;
import by.shimakser.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfficeFilterService extends FilterService {

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeFilterService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Transactional
    public List<Office> getAllByFilter(OfficeFilterRequest officeFilterRequest) {

        return officeRepository.findAll(buildPageable(officeFilterRequest))
                .stream().collect(Collectors.toList());
    }
}
