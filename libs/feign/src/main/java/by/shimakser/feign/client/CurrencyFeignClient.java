package by.shimakser.feign.client;

import by.shimakser.dto.CurrencyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "Currencies", url = "${feign.application-url}")
public interface CurrencyFeignClient {

    @GetMapping
    List<CurrencyDto> getCurrencies();

    @GetMapping("/{id}")
    CurrencyDto getCurrency(@PathVariable String id);
}
