package by.shimakser.feign;

import by.shimakser.feign.model.Currency;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "Currencies", url = "localhost:8081/currencies")
public interface CurrencyFeignClient {

    @GetMapping
    List<Currency> getCurrencies();

    @GetMapping("/{id}")
    Currency getCurrency(@PathVariable String id);
}
