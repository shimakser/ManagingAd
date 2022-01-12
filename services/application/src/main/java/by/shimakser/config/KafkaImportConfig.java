package by.shimakser.config;

import by.shimakser.kafka.config.KafkaAutoConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(KafkaAutoConfig.class)
@Configuration
public class KafkaImportConfig {
}
