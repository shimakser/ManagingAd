package by.shimakser.currencies.service;

import by.shimakser.currencies.mapper.CurrencyMapper;
import by.shimakser.currencies.repository.CurrenciesRepository;
import by.shimakser.currencies.service.rabbitmq.CurrenciesSender;
import by.shimakser.feign.client.CurrenciesFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrenciesSchedulerService {

    private final CurrenciesFeignClient currenciesFeignClient;
    private final CurrenciesRepository currenciesRepository;
    private final CurrenciesSender currenciesSender;
    private final CurrencyMapper currencyMapper;

    @Autowired
    public CurrenciesSchedulerService(CurrenciesFeignClient currenciesFeignClient, CurrenciesRepository currenciesRepository,
                                      CurrenciesSender currenciesSender, CurrencyMapper currencyMapper) {
        this.currenciesFeignClient = currenciesFeignClient;
        this.currenciesRepository = currenciesRepository;
        this.currenciesSender = currenciesSender;
        this.currencyMapper = currencyMapper;
    }

    @Scheduled(fixedRateString = "PT1H")
    public void schedulerCurrencies() {
        LocalDateTime date = LocalDateTime.now();

        List<String> currencies = currenciesFeignClient.getCurrencies()
                .getValute().values()
                .stream()
                .map(currencyMapper::mapToEntity)
                .map(currency -> {
                    currency.setUpdDate(date);
                    currenciesRepository.save(currency);
                    return currency.toString();
                })
                .collect(Collectors.toList());

        currenciesSender.sendCurrencies(currencies);
        log.info("Scheduled Currencies into db at {}", date);
    }
}
