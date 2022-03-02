package by.shimakser.office.service.kafka;

import by.shimakser.dto.CurrencyDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OfficeConsumerService {

    @KafkaListener(topics = "${spring.kafka.topic.currency}")
    public void currencyListener(ConsumerRecord<String, CurrencyDto> consumerRecord) {
        log.info("KafkaListener consume currency: {}", consumerRecord.value());
    }
}
