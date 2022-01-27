package by.shimakser.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    public static final String CURRENCIES_QUEUE = "currencies-queue";
    public static final String EVENT_EXCHANGE = "event-exchange";

    @Bean
    public Exchange eventExchange() {
        return new TopicExchange(EVENT_EXCHANGE);
    }

    @Bean
    public Queue currenciesQueue() {
        return new Queue(CURRENCIES_QUEUE);
    }

    @Bean
    public Binding binding(Queue currenciesQueue, TopicExchange eventExchange) {
        return BindingBuilder.bind(currenciesQueue).to(eventExchange).with(CURRENCIES_QUEUE);
    }
}
