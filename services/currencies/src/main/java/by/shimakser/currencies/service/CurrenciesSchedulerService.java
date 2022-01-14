package by.shimakser.currencies.service;

import by.shimakser.currencies.feign.CurrenciesFeignClient;
import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.repository.CurrenciesRepository;
import by.shimakser.currencies.service.rabbitmq.CurrenciesSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CurrenciesSchedulerService {

    private final CurrenciesFeignClient currenciesFeignClient;
    private final CurrenciesRepository currenciesRepository;
    private final CurrenciesSender currenciesSender;

    @Autowired
    public CurrenciesSchedulerService(CurrenciesFeignClient currenciesFeignClient,
                                      CurrenciesRepository currenciesRepository,
                                      CurrenciesSender currenciesSender) {
        this.currenciesFeignClient = currenciesFeignClient;
        this.currenciesRepository = currenciesRepository;
        this.currenciesSender = currenciesSender;
    }

    @Scheduled(fixedDelayString = "PT1H")
    public void schedulerCurrencies() {
        LocalDateTime date = LocalDateTime.now();

        new ArrayList<>();

        List<String> currencies = currenciesFeignClient.getCurrencies()
                .getValute().values()
                .stream()
                .peek(currency -> {
                    currency.setUpdDate(date);
                    currenciesRepository.save(currency);
                })
                .map(Objects::toString)
                .collect(Collectors.toList());

        currenciesSender.sendCurrencies(currencies);
    }
}
