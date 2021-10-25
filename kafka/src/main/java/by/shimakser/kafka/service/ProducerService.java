package by.shimakser.kafka.service;

import by.shimakser.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class ProducerService {

    @Value(value = "${spring.kafka.topic-name}")
    private String topic;

    private final KafkaTemplate<String, UserDto> userKafkaTemplate;

    @Autowired
    public ProducerService(KafkaTemplate<String, UserDto> userKafkaTemplate) {
        this.userKafkaTemplate = userKafkaTemplate;
    }

    public void send(UserDto usr) {
        userKafkaTemplate.send(topic, usr);
    }
}
