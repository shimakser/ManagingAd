package by.shimakser.controller.office;

import by.shimakser.filter.office.OfficeFilterRequest;
import by.shimakser.model.office.Office;
import by.shimakser.service.office.CurrencyService;
import by.shimakser.service.office.OfficeFilterService;
import by.shimakser.service.office.OfficeService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offices")
public class CurrencyController {

    private final OfficeFilterService officeFilterService;

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(OfficeFilterService officeFilterService, CurrencyService currencyService) {
        this.officeFilterService = officeFilterService;
        this.currencyService = currencyService;
    }

    @PostMapping
    public List<Office> getAllOffices(@RequestBody OfficeFilterRequest officeFilterRequest) {
        return officeFilterService.getAllByFilter(officeFilterRequest);
    }

    @GetMapping(value = "/currencies")
    public JSONObject getCurrenciesByExternalApi() {
        return currencyService.getCurrencies();
    }
}
