package by.shimakser.interceptor.config;

import by.shimakser.interceptor.FeignInterceptor;
import by.shimakser.interceptor.HttpInterceptor;
import by.shimakser.interceptor.KafkaProducerInterceptor;
import by.shimakser.kafka.config.KafkaAutoConfig;
import by.shimakser.kafka.config.KafkaConfig;
import feign.RequestInterceptor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Map;

@Configuration
@EnableAspectJAutoProxy
@Import(KafkaAutoConfig.class)
@ComponentScan(basePackages = "by.shimakser.interceptor")
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private KafkaConfig kafkaConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor());
    }

    @Bean
    public RequestInterceptor getFeignInterceptor() {
        return new FeignInterceptor();
    }

    @Primary
    @Bean
    public KafkaTemplate<String, Object> interceptorProducerKafkaTemplate() {
        Map<String, Object> props = kafkaConfig.producerConfigs();
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaProducerInterceptor.class.getName());

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }
}
