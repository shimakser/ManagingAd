package by.shimakser.service.mongo;

import by.shimakser.feign.CurrencyFeignClient;
import by.shimakser.feign.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyFeignClient currencyFeignClient;

    @Autowired
    public CurrencyService(CurrencyFeignClient currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    @Transactional
    public List<Currency> getChosenCurrencies() {
        return currencyFeignClient.getCurrencies();
    }

    @Cacheable(value = "currency", cacheManager = "compositeCacheManager", key = "#id")
    @Transactional
    public Currency getCurrency(String id) {
        return currencyFeignClient.getCurrency(id);
    }
}
