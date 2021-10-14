package by.shimakser.config;

import by.shimakser.converter.JavaScriptMessageConverter;
import by.shimakser.feign.CurrencyFeignClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableFeignClients(basePackageClasses = CurrencyFeignClient.class)
@PropertySource(value = "file:src/main/resources/application.properties")
public class MessageConverterConfig {

    @Bean
    public JavaScriptMessageConverter abstractJackson2HttpMessageConverter() {
        return new JavaScriptMessageConverter();
    }
}
