package by.shimakser.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics="registration")
    public void registrationListener(ConsumerRecord<String, String> record){
        LOG.info("New user: " + record.value());
    }
}
