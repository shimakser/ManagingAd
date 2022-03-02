package by.shimakser.currencies.service.kafka;

import by.shimakser.dto.CurrencyDto;
import by.shimakser.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurrencyKafkaService {

    private final KafkaTemplate<String, Object> currencyKafkaTemplate;

    @Value(value = "${spring.kafka.topic.currency}")
    private String currencyTopic;

    @Autowired
    public CurrencyKafkaService(KafkaTemplate<String, Object> currencyKafkaTemplate) {
        this.currencyKafkaTemplate = currencyKafkaTemplate;
    }

    public void sendCurrencyToKafka() {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId("R01235");

        currencyKafkaTemplate.send(currencyTopic, currencyDto);
        log.info("KafkaProducer send currency: {}.", currencyDto);
    }

    @KafkaListener(topics = "${spring.kafka.topic.registration}")
    public void registrationListener(ConsumerRecord<String, UserDto> consumerRecord) {
        log.info("CurrenciesKafkaListener consume new user: " + consumerRecord.value().toString());

        sendCurrencyToKafka();
    }
}
