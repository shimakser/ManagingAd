package by.shimakser.listener.service;

import by.shimakser.dto.CurrencyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CurrenciesUpdateListener {

    @RabbitListener(queues="currencies-queue")
    public void receive(List<String> currencyDtos) {
        log.info("Update currency '{}'", currencyDtos);
    }
}
