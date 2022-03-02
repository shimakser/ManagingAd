package by.shimakser.kafka.config;

import by.shimakser.dto.NumbersRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaRequestReplyConfig {

    private final KafkaConfig kafkaConfig;

    @Autowired
    public KafkaRequestReplyConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @Value(value = "${spring.kafka.consumer.requestreply-group}")
    private String consumerRequestReplyGroup;

    @Value(value = "${spring.kafka.topic.requestreply}")
    private String requestReplyTopic;

    @Bean
    public KafkaTemplate<String, NumbersRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, NumbersRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaConfig.producerConfigs());
    }

    @Bean
    public ReplyingKafkaTemplate<String, NumbersRequest, NumbersRequest> replyKafkaTemplate(ProducerFactory<String, NumbersRequest> pf, KafkaMessageListenerContainer<String, NumbersRequest> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public KafkaMessageListenerContainer<String, NumbersRequest> replyContainer(ConsumerFactory<String, NumbersRequest> cf) {
        ContainerProperties containerProperties = new ContainerProperties(requestReplyTopic);
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }


    @Bean
    public ConsumerFactory<String, NumbersRequest> consumerFactory() {
        kafkaConfig.consumerConfigs().put(ConsumerConfig.GROUP_ID_CONFIG, consumerRequestReplyGroup);
        return new DefaultKafkaConsumerFactory<>(kafkaConfig.consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(NumbersRequest.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, NumbersRequest>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NumbersRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }
}
