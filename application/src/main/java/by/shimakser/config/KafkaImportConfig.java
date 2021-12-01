package by.shimakser.config;

import by.shimakser.kafka.config.KafkaConfig;
import by.shimakser.kafka.config.KafkaRegistrationConfig;
import by.shimakser.kafka.config.KafkaRequestReplyConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({KafkaConfig.class, KafkaRegistrationConfig.class, KafkaRequestReplyConfig.class})
@Configuration
public class KafkaImportConfig {
}
