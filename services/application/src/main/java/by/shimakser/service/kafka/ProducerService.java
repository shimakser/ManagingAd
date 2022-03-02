package by.shimakser.service.kafka;

import by.shimakser.dto.NumbersRequest;
import by.shimakser.dto.UserDto;
import by.shimakser.mapper.UserMapper;
import by.shimakser.model.ad.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class ProducerService {

    private final ReplyingKafkaTemplate<String, NumbersRequest, NumbersRequest> requestReplyKafkaTemplate;
    private final KafkaTemplate<String, Object> userKafkaTemplate;
    private final UserMapper userMapper;

    @Value(value = "${spring.kafka.topic.registration}")
    private String registrationTopic;

    @Value(value = "${spring.kafka.topic.request}")
    private String requestTopic;

    @Value(value = "${spring.kafka.topic.requestreply}")
    private String requestReplyTopic;

    @Autowired
    public ProducerService(ReplyingKafkaTemplate<String, NumbersRequest, NumbersRequest> requestReplyKafkaTemplate,
                           KafkaTemplate<String, Object> userKafkaTemplate, UserMapper userMapper) {
        this.requestReplyKafkaTemplate = requestReplyKafkaTemplate;
        this.userKafkaTemplate = userKafkaTemplate;
        this.userMapper = userMapper;
    }

    public void sendUser(User user) {
        UserDto userDto = userMapper.mapToDto(user);
        userKafkaTemplate.send(registrationTopic, userDto);

        log.info("KafkaProducer send user {}.", userDto.getId());
    }

    public NumbersRequest sendNumbers(NumbersRequest numbersRequest) throws InterruptedException, ExecutionException {
        ProducerRecord<String, NumbersRequest> producerRecord = new ProducerRecord<>(requestTopic, numbersRequest);
        producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, requestReplyTopic.getBytes()));
        RequestReplyFuture<String, NumbersRequest, NumbersRequest> sendAndReceive = requestReplyKafkaTemplate.sendAndReceive(producerRecord);

        ConsumerRecord<String, NumbersRequest> consumerRecord = sendAndReceive.get();
        return consumerRecord.value();
    }
}
