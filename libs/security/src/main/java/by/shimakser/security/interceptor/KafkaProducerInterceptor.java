package by.shimakser.security.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class KafkaProducerInterceptor extends BaseInterceptor implements ProducerInterceptor<String, Object> {

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> producerRecord) {

        super.setMdcAndHeader(super.getRandomNumber())
                .forEach((key, value) -> producerRecord
                        .headers()
                        .add(key, value.getBytes(StandardCharsets.UTF_8)));

        log.info("Intercepted kafka message [{}] to topic {}", producerRecord.value(), producerRecord.topic());
        return new ProducerRecord<>(producerRecord.topic(), producerRecord.key(), producerRecord.value());
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
    }

    @Override
    public void configure(Map<String, ?> map) {
    }

    @Override
    public void close() {}
}
