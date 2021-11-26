package by.shimakser.feign;

import by.shimakser.dto.CurrencyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "Currencies", url = "localhost:8081/currencies")
public interface CurrencyFeignClient {

    @GetMapping
    List<CurrencyDto> getCurrencies();

    @GetMapping("/{id}")
    CurrencyDto getCurrency(@PathVariable String id);
}
