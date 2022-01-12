package by.shimakser.currencies.config;

import by.shimakser.currencies.converter.JavaScriptMessageConverter;
import by.shimakser.currencies.feign.CurrenciesFeignClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = CurrenciesFeignClient.class)
public class MessageConverterConfig {

    @Bean
    public JavaScriptMessageConverter abstractJackson2HttpMessageConverter() {
        return new JavaScriptMessageConverter();
    }
}
