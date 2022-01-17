package by.shimakser.listener;

import by.shimakser.rabbitmq.config.RabbitMQConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(RabbitMQConfig.class)
@SpringBootApplication
public class Listener {
    public static void main(String[] args) {
        SpringApplication.run(Listener.class, args);
    }
}
