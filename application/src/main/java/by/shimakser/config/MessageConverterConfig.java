package by.shimakser.config;

import by.shimakser.feign.CurrencyFeignClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = CurrencyFeignClient.class)
public class MessageConverterConfig {
}
