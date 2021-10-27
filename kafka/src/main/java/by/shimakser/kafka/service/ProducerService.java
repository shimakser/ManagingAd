package by.shimakser.kafka.service;

import by.shimakser.kafka.model.NumbersRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ProducerService {

    private final ReplyingKafkaTemplate<String, NumbersRequest, NumbersRequest> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.request-topic}")
    String requestTopic;

    @Value(value = "${spring.kafka.topic.requestreply-topic}")
    String requestReplyTopic;

    @Autowired
    public ProducerService(ReplyingKafkaTemplate<String, NumbersRequest, NumbersRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public NumbersRequest send(NumbersRequest numbersRequest) throws InterruptedException, ExecutionException {
        ProducerRecord<String, NumbersRequest> record = new ProducerRecord<>(requestTopic, numbersRequest);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, requestReplyTopic.getBytes()));
        RequestReplyFuture<String, NumbersRequest, NumbersRequest> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        ConsumerRecord<String, NumbersRequest> consumerRecord = sendAndReceive.get();
        return consumerRecord.value();
    }
}
