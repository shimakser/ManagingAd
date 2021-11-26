package by.shimakser.controller.mongo;

import by.shimakser.model.mongo.Currency;
import by.shimakser.service.mongo.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currency")
    public List<Currency> getChosenCurrenciesFromCustomExternalService() {
        return currencyService.getChosenCurrencies();
    }

    @GetMapping("/currency/{id}")
    public Currency getCurrencyByIdFromCustomExternalService(@PathVariable String id) {
        return currencyService.getCurrency(id);
    }
}
