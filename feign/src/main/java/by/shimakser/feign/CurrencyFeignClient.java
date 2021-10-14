package by.shimakser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "Currencies", url = "localhost:8081/currencies")
public interface CurrencyFeignClient {

    @GetMapping
    List getCurrency();
}