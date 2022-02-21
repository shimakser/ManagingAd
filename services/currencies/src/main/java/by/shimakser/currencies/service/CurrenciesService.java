package by.shimakser.currencies.service;

import by.shimakser.currencies.feign.CurrenciesFeignClient;
import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.repository.CurrenciesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CurrenciesService {

    private final CurrenciesFeignClient currenciesFeignClient;
    private final ConvertService convertService;
    private final CurrenciesRepository currenciesRepository;

    @Autowired
    public CurrenciesService(CurrenciesFeignClient currenciesFeignClient, ConvertService convertService, CurrenciesRepository currenciesRepository) {
        this.currenciesFeignClient = currenciesFeignClient;
        this.convertService = convertService;
        this.currenciesRepository = currenciesRepository;
    }

    @Transactional
    public List<Currency> getCurrencies() {
        List<Currency> currencies = currenciesRepository.findAll();

        log.info("Searched all currencies.");
        return  !currencies.isEmpty()
                ? currencies
                : new ArrayList<>(currenciesFeignClient.getCurrencies().getValute().values());
    }

    public Currency getCurrency(String id) {
        log.info("Search Currency {}", id);
        return convertService.getEntity(currenciesFeignClient.getCurrencies(), id);
    }
}
