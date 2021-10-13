package by.shimakser.service.office;

import by.shimakser.feign.CurrencyFeignClient;
import by.shimakser.model.office.Currency;
import by.shimakser.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyService extends FilterService {

    private final CurrencyFeignClient currencyFeignClient;

    @Autowired
    public CurrencyService(CurrencyFeignClient currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    @Transactional
    public List<Currency> getChosenCurrency() {
        return currencyFeignClient.getCurrency();
    }
}
