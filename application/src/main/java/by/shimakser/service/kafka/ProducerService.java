package by.shimakser.service.kafka;

import by.shimakser.dto.NumbersRequest;
import by.shimakser.dto.UserDto;
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

@Service
public class ProducerService {

    private final ReplyingKafkaTemplate<String, NumbersRequest, NumbersRequest> requestReplyKafkaTemplate;

    private final KafkaTemplate<String, UserDto> userKafkaTemplate;

    @Value(value = "${spring.kafka.topic.registration-topic}")
    private String registrationTopic;

    @Value(value = "${spring.kafka.topic.request-topic}")
    private String requestTopic;

    @Value(value = "${spring.kafka.topic.requestreply-topic}")
    private String requestReplyTopic;

    @Autowired
    public ProducerService(ReplyingKafkaTemplate<String, NumbersRequest, NumbersRequest> requestReplyKafkaTemplate,
                           KafkaTemplate<String, UserDto> userKafkaTemplate) {
        this.requestReplyKafkaTemplate = requestReplyKafkaTemplate;
        this.userKafkaTemplate = userKafkaTemplate;
    }

    public void sendUser(UserDto userDto) {
        userKafkaTemplate.send(registrationTopic, userDto);
    }

    public NumbersRequest sendNumbers(NumbersRequest numbersRequest) throws InterruptedException, ExecutionException {
        ProducerRecord<String, NumbersRequest> producerRecord = new ProducerRecord<>(requestTopic, numbersRequest);
        producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, requestReplyTopic.getBytes()));
        RequestReplyFuture<String, NumbersRequest, NumbersRequest> sendAndReceive = requestReplyKafkaTemplate.sendAndReceive(producerRecord);

        ConsumerRecord<String, NumbersRequest> consumerRecord = sendAndReceive.get();
        return consumerRecord.value();
    }
}
