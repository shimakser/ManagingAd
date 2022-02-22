package by.shimakser.currencies.service;

import by.shimakser.currencies.mapper.CurrencyMapper;
import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.model.CurrencyCode;
import by.shimakser.currencies.repository.CurrenciesRepository;
import by.shimakser.dto.Currencies;
import by.shimakser.feign.client.CurrenciesFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrenciesService {

    private final CurrenciesFeignClient currenciesFeignClient;
    private final CurrenciesRepository currenciesRepository;
    private final CurrencyMapper currencyMapper;

    @Autowired
    public CurrenciesService(CurrenciesFeignClient currenciesFeignClient,
                             CurrenciesRepository currenciesRepository,
                             CurrencyMapper currencyMapper) {
        this.currenciesFeignClient = currenciesFeignClient;
        this.currenciesRepository = currenciesRepository;
        this.currencyMapper = currencyMapper;
    }

    @Transactional
    public List<Currency> getCurrencies() {
        List<Currency> currencies = currenciesRepository.findAll();

        log.info("Searched all currencies.");
        return  !currencies.isEmpty()
                ? currencies
                : currenciesFeignClient.getCurrencies().getValute().values()
                .stream()
                .map(currencyMapper::mapToEntity)
                .collect(Collectors.toList());
    }

    public Currency getCurrency(String id) {
        log.info("Search Currency {}", id);
        return getEntity(currenciesFeignClient.getCurrencies(), id);
    }

    public Currency getEntity(Currencies currencies, String id) {
        if (currencies.getValute() == null) {
            throw new NoSuchElementException("Currencies valute list is empty.");
        }
        return currencies.getValute().values()
                .stream()
                .map(currencyMapper::mapToEntity)
                .filter(currency -> currency.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Incorrect id: " + id));
    }

    public List<Currency> getListEntity(Currencies currencies) {
        return currencies.getValute() == null
                ? Collections.emptyList()
                : Arrays.stream(CurrencyCode.values())
                .map(code -> currencies.getValute().get(code.toString()))
                .map(currencyMapper::mapToEntity)
                .collect(Collectors.toList());
    }
}
