package by.shimakser.currencies.controller;

import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.service.CurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrenciesController {

    private final CurrenciesService currenciesService;

    @Autowired
    public CurrenciesController(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @GetMapping(value = "/currencies")
    public List<Currency> getCurrenciesByExternalApi() throws InterruptedException {
        Thread.sleep(5000);
        return currenciesService.getCurrencies();
    }

    @GetMapping(value = "/currencies/{id}")
    public Currency getCurrencyByExternalApi(@PathVariable String id) throws InterruptedException {
        Thread.sleep(5000);
        return currenciesService.getCurrency(id);
    }
}
