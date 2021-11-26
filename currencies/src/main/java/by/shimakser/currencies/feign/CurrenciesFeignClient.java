package by.shimakser.currencies.feign;

import by.shimakser.currencies.config.MessageConverterConfig;
import net.minidev.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "cbr-xml-daily", url = "${feign.currencies-url}", configuration = MessageConverterConfig.class)
public interface CurrenciesFeignClient {

    @GetMapping
    JSONObject getCurrencies();
}
