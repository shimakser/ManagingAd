package by.shimakser.controller.office;

import by.shimakser.model.office.Office;
import by.shimakser.service.office.CurrencyService;
import by.shimakser.service.office.OfficeService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offices")
public class CurrencyController {

    private final OfficeService officeService;

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(OfficeService officeService, CurrencyService currencyService) {
        this.officeService = officeService;
        this.currencyService = currencyService;
    }

    @GetMapping
    public List<Office> getAllOffices() {
        return officeService.getAll();
    }

    @GetMapping(value = "/currencies")
    public JSONObject getCurrenciesByExternalApi() {
        return currencyService.getCurrencies();
    }
}
