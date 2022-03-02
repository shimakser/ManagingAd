package by.shimakser.config;

import by.shimakser.feign.config.MessageConverterConfig;
import by.shimakser.interceptor.config.InterceptorConfig;
import by.shimakser.kafka.config.KafkaAutoConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({KafkaAutoConfig.class, MessageConverterConfig.class, InterceptorConfig.class})
@Configuration
public class ImportConfig {
}
