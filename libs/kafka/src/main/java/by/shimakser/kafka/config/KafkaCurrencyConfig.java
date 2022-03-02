package by.shimakser.kafka.config;

import by.shimakser.dto.CurrencyDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
public class KafkaCurrencyConfig {

    @Value(value = "${spring.kafka.consumer.currency-group}")
    private String consumerCurrencyGroup;

    private final KafkaConfig kafkaConfig;

    @Autowired
    public KafkaCurrencyConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @Bean
    public ConsumerFactory<String, CurrencyDto> currencyConsumerFactory() {
        Map<String, Object> currencyProps = kafkaConfig.consumerConfigs();
        currencyProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerCurrencyGroup);

        return new DefaultKafkaConsumerFactory<>(currencyProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CurrencyDto> currencyKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CurrencyDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(currencyConsumerFactory());
        return factory;
    }
}
