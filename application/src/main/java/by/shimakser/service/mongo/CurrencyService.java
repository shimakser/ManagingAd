package by.shimakser.service.mongo;

import by.shimakser.feign.CurrencyFeignClient;
import by.shimakser.feign.model.Currency;
import by.shimakser.repository.mongo.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyFeignClient currencyFeignClient;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyFeignClient currencyFeignClient, CurrencyRepository currencyRepository) {
        this.currencyFeignClient = currencyFeignClient;
        this.currencyRepository = currencyRepository;
    }

    @Transactional
    public List<Currency> getChosenCurrencies() {
        List<Currency> currencies = currencyFeignClient.getCurrencies();

        //currencyRepository.insert(currencies);

        return currencies;
    }

    @Cacheable(value = "currency", cacheManager = "compositeCacheManager", key = "#id")
    @Transactional
    public Currency getCurrency(String id) {
        return currencyFeignClient.getCurrency(id);
    }
}
