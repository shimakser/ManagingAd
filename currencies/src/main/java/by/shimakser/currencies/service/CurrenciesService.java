package by.shimakser.currencies.service;

import by.shimakser.currencies.feign.CurrenciesFeignClient;
import by.shimakser.currencies.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrenciesService {

    private final CurrenciesFeignClient currenciesFeignClient;

    private final ConvertService convertService;

    @Autowired
    public CurrenciesService(CurrenciesFeignClient currenciesFeignClient, ConvertService convertService) {
        this.currenciesFeignClient = currenciesFeignClient;
        this.convertService = convertService;
    }

    @Transactional
    public List<Currency> getCurrencies() {
        return convertService.getListEntity(currenciesFeignClient.getCurrencies());
    }

    @Transactional
    public Currency getCurrency(String id) {
        return convertService.getEntity(currenciesFeignClient.getCurrencies(), id);
    }
}
