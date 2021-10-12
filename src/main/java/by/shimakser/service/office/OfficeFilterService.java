package by.shimakser.service.office;

import by.shimakser.feign.CurrencyFeignClient;
import by.shimakser.filter.office.Currency;
import by.shimakser.filter.office.OfficeFilterRequest;
import by.shimakser.filter.office.OfficeFilterResponse;
import by.shimakser.model.office.Office;
import by.shimakser.repository.office.OfficeRepository;
import by.shimakser.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OfficeFilterService extends FilterService {

    private final OfficeRepository officeRepository;

    private final CurrencyFeignClient currencyFeignClient;

    @Autowired
    public OfficeFilterService(OfficeRepository officeRepository, CurrencyFeignClient currencyFeignClient) {
        this.officeRepository = officeRepository;
        this.currencyFeignClient = currencyFeignClient;
    }

    @Transactional
    public List<OfficeFilterResponse> getAllByFilter(OfficeFilterRequest officeFilterRequest) {
        String currency = officeFilterRequest.getCurrency().toString();
        List<Office> list = officeRepository.findAll(
                        buildPageable(officeFilterRequest))
                .stream().collect(Collectors.toList());

        Map<String, Map<String, Object>> map = (LinkedHashMap) currencyFeignClient.getCurrency().get("Valute");
        Double currencyValue = Double.parseDouble(map.get(currency).get("Value").toString());

        Double eurValue = Double.parseDouble(map.get(Currency.EUR.toString()).get("Value").toString());
        List<OfficeFilterResponse> responses = new ArrayList<>();
        if (currency.equals(Currency.EUR.toString())) {
            for (Office office : list) {
                responses.add(new OfficeFilterResponse(office.getOfficePrice(), office));
            }
            return responses;
        } else {
            for (Office office : list) {
                responses.add(new OfficeFilterResponse(office.getOfficePrice() * eurValue / currencyValue, office));
            }
        }
        return responses;
    }
}
