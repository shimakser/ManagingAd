package by.shimakser.kafka.service;

import by.shimakser.kafka.model.NumbersRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @KafkaListener(topics = "${spring.kafka.topic.request-topic}")
    @SendTo
    public NumbersRequest listen(NumbersRequest request) {

        Integer sum = request.getFirstNumber() + request.getSecondNumber();
        request.setSum(sum);
        return request;
    }
}
