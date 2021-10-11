package by.shimakser.service.office;

import by.shimakser.converter.JavaScriptMessageConverter;
import by.shimakser.feign.CurrencyFeignClient;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrencyService {

    private final CurrencyFeignClient currencyFeignClient;

    @Autowired
    public CurrencyService(CurrencyFeignClient currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    @Transactional
    public JSONObject getCurrencies() {
        return currencyFeignClient.getCurrency();
    }
}
