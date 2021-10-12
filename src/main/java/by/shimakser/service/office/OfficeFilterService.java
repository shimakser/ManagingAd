package by.shimakser.service.office;

import by.shimakser.filter.office.OfficeFilterRequest;
import by.shimakser.filter.office.OfficeFilterResponse;
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

    private final CurrencyConverterService currencyConverterService;

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeFilterService(CurrencyConverterService currencyConverterService, OfficeRepository officeRepository) {
        this.currencyConverterService = currencyConverterService;
        this.officeRepository = officeRepository;
    }

    @Transactional
    public List<OfficeFilterResponse> getAllByConverter(OfficeFilterRequest officeFilterRequest) {
        List<Office> offices = officeRepository.findAll(
                        buildPageable(officeFilterRequest))
                .stream().collect(Collectors.toList());
        return currencyConverterService.currencyConvert(officeFilterRequest, offices);
    }
}
