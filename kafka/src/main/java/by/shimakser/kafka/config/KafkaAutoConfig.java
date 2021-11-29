package by.shimakser.kafka.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan(basePackages = "by.shimakser.kafka.config")
@Configuration
public class KafkaAutoConfig {

}
