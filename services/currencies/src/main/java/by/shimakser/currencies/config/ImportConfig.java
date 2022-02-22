package by.shimakser.currencies.config;


import by.shimakser.feign.config.MessageConverterConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(MessageConverterConfig.class)
@Configuration
public class ImportConfig {
}
