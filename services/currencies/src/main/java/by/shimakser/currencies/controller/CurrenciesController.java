package by.shimakser.currencies.controller;

import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.service.CurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrenciesController {

    private final CurrenciesService currenciesService;

    @Autowired
    public CurrenciesController(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @GetMapping
    public List<Currency> getCurrenciesByExternalApi() throws InterruptedException {
        Thread.sleep(5000);
        return currenciesService.getCurrencies();
    }

    @GetMapping(value = "/{id}")
    public Currency getCurrencyByExternalApi(@PathVariable String id) throws InterruptedException {
        Thread.sleep(5000);
        return currenciesService.getCurrency(id);
    }
}
