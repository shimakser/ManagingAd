package by.shimakser.kafka.config;

import by.shimakser.dto.UserDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

@Configuration
public class KafkaRegistrationConfig {

    private final KafkaConfig kafkaConfig;

    @Autowired
    public KafkaRegistrationConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @Bean
    public KafkaTemplate<String, UserDto> registrationKafkaTemplate() {
        return new KafkaTemplate<>(registrationProducerFactory());
    }

    @Bean
    public ProducerFactory<String, UserDto> registrationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaConfig.producerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDto> registrationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(registrationConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, UserDto> registrationConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaConfig.consumerConfigs());
    }
}
