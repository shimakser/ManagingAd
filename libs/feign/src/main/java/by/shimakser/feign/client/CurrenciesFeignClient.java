package by.shimakser.feign.client;

import by.shimakser.dto.Currencies;
import by.shimakser.feign.config.MessageConverterConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "cbr-xml-daily", url = "${feign.currencies-url}", configuration = MessageConverterConfig.class)
public interface CurrenciesFeignClient {

    @GetMapping
    Currencies getCurrencies();
}
