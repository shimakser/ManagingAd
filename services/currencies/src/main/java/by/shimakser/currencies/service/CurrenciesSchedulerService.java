package by.shimakser.currencies.service;

import by.shimakser.currencies.feign.CurrenciesFeignClient;
import by.shimakser.currencies.repository.CurrenciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CurrenciesSchedulerService {

    private final CurrenciesFeignClient currenciesFeignClient;

    private final CurrenciesRepository currenciesRepository;

    @Autowired
    public CurrenciesSchedulerService(CurrenciesFeignClient currenciesFeignClient, CurrenciesRepository currenciesRepository) {
        this.currenciesFeignClient = currenciesFeignClient;
        this.currenciesRepository = currenciesRepository;
    }

    @Scheduled(fixedDelayString = "PT1H")
    public void tryScheduler() {
        LocalDateTime date = LocalDateTime.now();

        currenciesFeignClient.getCurrencies().getValute().values()
                .stream()
                .peek(currency -> currency.setUpdDate(date))
                .forEach(currenciesRepository::save);
    }
}
