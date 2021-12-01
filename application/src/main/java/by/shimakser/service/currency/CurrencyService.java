package by.shimakser.service.currency;

import by.shimakser.feign.CurrencyFeignClient;
import by.shimakser.mapper.CurrencyMapper;
import by.shimakser.model.currency.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyFeignClient currencyFeignClient;

    private final CurrencyMapper currencyMapper;

    @Autowired
    public CurrencyService(CurrencyFeignClient currencyFeignClient, CurrencyMapper currencyMapper) {
        this.currencyFeignClient = currencyFeignClient;
        this.currencyMapper = currencyMapper;
    }

    @Transactional
    public List<Currency> getChosenCurrencies() {
        return currencyMapper.mapToListEntity(currencyFeignClient.getCurrencies());
    }

    @Cacheable(value = "currency", cacheManager = "compositeCacheManager", key = "#id")
    @Transactional
    public Currency getCurrency(String id) {
        return currencyMapper.mapToEntity(currencyFeignClient.getCurrency(id));
    }
}
