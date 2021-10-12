package by.shimakser.service.office;

import by.shimakser.feign.CurrencyFeignClient;
import by.shimakser.filter.office.Currency;
import by.shimakser.filter.office.OfficeFilterRequest;
import by.shimakser.filter.office.OfficeFilterResponse;
import by.shimakser.model.office.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyConverterService {

    private final CurrencyFeignClient currencyFeignClient;

    @Autowired
    public CurrencyConverterService(CurrencyFeignClient currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    public List<OfficeFilterResponse> currencyConvert(OfficeFilterRequest officeFilterRequest, List<Office> offices) {
        String currency = officeFilterRequest.getCurrency().toString();

        Map<String, Map<String, Object>> map = (LinkedHashMap) currencyFeignClient.getCurrency().get("Valute");
        Double currencyValue = Double.parseDouble(map.get(currency).get("Value").toString());

        Double eurValue = Double.parseDouble(map.get(Currency.EUR.toString()).get("Value").toString());
        List<OfficeFilterResponse> responses = new ArrayList<>();
        if (currency.equals(Currency.EUR.toString())) {
            for (Office office : offices) {
                responses.add(new OfficeFilterResponse(office.getOfficePrice(), office));
            }
            return responses;
        } else {
            for (Office office : offices) {
                responses.add(new OfficeFilterResponse(office.getOfficePrice() * eurValue / currencyValue, office));
            }
        }
        return responses;
    }
}
