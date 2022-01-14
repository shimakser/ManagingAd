package by.shimakser.currencies.service.rabbitmq;

import by.shimakser.currencies.model.Currency;
import by.shimakser.rabbitmq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrenciesSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CurrenciesSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCurrencies(List<String> currencies) {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.CURRENCIES_QUEUE, currencies);
    }
}
