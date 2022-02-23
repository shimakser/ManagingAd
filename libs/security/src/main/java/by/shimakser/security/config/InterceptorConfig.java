package by.shimakser.security.config;

import by.shimakser.dto.UserDto;
import by.shimakser.kafka.config.KafkaConfig;
import by.shimakser.security.interceptor.FeignInterceptor;
import by.shimakser.security.interceptor.HttpInterceptor;
import by.shimakser.security.interceptor.KafkaConsumerInterceptor;
import by.shimakser.security.interceptor.KafkaProducerInterceptor;
import feign.RequestInterceptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Map;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private HttpInterceptor httpInterceptor;

    @Autowired
    private KafkaConfig kafkaConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpInterceptor);
    }

    @Bean
    public RequestInterceptor getFeignInterceptor() {
        return new FeignInterceptor();
    }

    @Primary
    @Bean
    public KafkaTemplate<String, UserDto> interceptorProducerKafkaTemplate() {
        Map<String, Object> props = kafkaConfig.producerConfigs();
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaProducerInterceptor.class.getName());

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    @Primary
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDto> interceptorConsumerKafkaTemplate() {
        Map<String, Object> props = kafkaConfig.consumerConfigs();
        props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaConsumerInterceptor.class.getName());

        ConcurrentKafkaListenerContainerFactory<String, UserDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));

        return factory;
    }
}
