package by.shimakser.feign.config;


import by.shimakser.feign.client.CurrenciesFeignClient;
import by.shimakser.feign.client.CurrencyFeignClient;
import by.shimakser.feign.converter.JavaScriptMessageConverter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {CurrenciesFeignClient.class, CurrencyFeignClient.class})
public class MessageConverterConfig {

    @Bean
    public JavaScriptMessageConverter abstractJackson2HttpMessageConverter() {
        return new JavaScriptMessageConverter();
    }
}
