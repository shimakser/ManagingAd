package by.shimakser.currencies.service;

import by.shimakser.currencies.feign.CurrenciesFeignClient;
import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.repository.CurrenciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return  !currenciesRepository.findAll().isEmpty()
                ? currenciesRepository.findAll()
                : new ArrayList<>(currenciesFeignClient.getCurrencies().getValute().values());
    }

    @Transactional
    public Currency getCurrency(String id) {
        return convertService.getEntity(currenciesFeignClient.getCurrencies(), id);
    }
}
