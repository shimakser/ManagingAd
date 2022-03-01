package by.shimakser.kafka.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

@ComponentScan(basePackages = "by.shimakser.kafka.config")
@Configuration
public class KafkaAutoConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer setKafkaProperties() {
        PropertySourcesPlaceholderConfigurer kafkaConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application-kafka.yml"));
        kafkaConfigurer.setProperties(Objects.requireNonNull(yaml.getObject()));
        kafkaConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return kafkaConfigurer;
    }
}
