package by.shimakser.controller.office;

import by.shimakser.model.office.Currency;
import by.shimakser.service.office.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<Currency> getChosenCurrencyFromCustomExternalService() {
        return currencyService.getChosenCurrency();
    }
}
