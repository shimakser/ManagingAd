package by.shimakser.service.kafka;

import by.shimakser.dto.NumbersRequest;
import by.shimakser.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {

    @KafkaListener(topics = "${spring.kafka.topic.registration-topic}")
    public void registrationListener(ConsumerRecord<String, UserDto> consumerRecord) {
        log.info("New user: " + consumerRecord.value().toString());
    }

    @KafkaListener(topics = "${spring.kafka.topic.request-topic}")
    @SendTo
    public NumbersRequest requestListen(NumbersRequest request) {
        Integer sum = request.getFirstNumber() + request.getSecondNumber();
        request.setSum(sum);
        return request;
    }
}
