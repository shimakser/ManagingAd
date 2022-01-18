package by.shimakser.controller.currency;

import by.shimakser.model.currency.Currency;
import by.shimakser.service.currency.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public List<Currency> getChosenCurrenciesFromCustomExternalService() {
        return currencyService.getChosenCurrencies();
    }

    @GetMapping("/{id}")
    public Currency getCurrencyByIdFromCustomExternalService(@PathVariable String id) {
        return currencyService.getCurrency(id);
    }
}
