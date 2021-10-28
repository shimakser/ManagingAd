package by.shimakser.service.kafka;

import by.shimakser.dto.NumbersRequest;
import by.shimakser.dto.UserDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "${spring.kafka.topic.registration-topic}")
    public void registrationListener(ConsumerRecord<String, UserDto> record) {
        LOG.info("New user: " + record.value().toString());
    }

    @KafkaListener(topics = "${spring.kafka.topic.request-topic}")
    @SendTo
    public NumbersRequest requestListen(NumbersRequest request) {
        Integer sum = request.getFirstNumber() + request.getSecondNumber();
        request.setSum(sum);
        return request;
    }
}
