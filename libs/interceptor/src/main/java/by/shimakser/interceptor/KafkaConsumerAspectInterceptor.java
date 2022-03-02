package by.shimakser.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class KafkaConsumerAspectInterceptor extends BaseInterceptor {

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void beforeCallKafkaListenerAnnotation() {
    }

    @Before("execution(* *(..)) && beforeCallKafkaListenerAnnotation()")
    public void beforeAdvice(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        ConsumerRecord<String, Object> consumerRecordArgument = Arrays.stream(methodArgs)
                .filter(ConsumerRecord.class::isInstance)
                .map(arg -> (ConsumerRecord<String, Object>) arg)
                .findAny()
                .orElseThrow(() -> new NotFoundException("No ConsumerRecord argument exist."));

        setKafkaHeaderToMdc(consumerRecordArgument.headers());

        log.info("Intercepted consume kafka message [{}] to topic {}.",
                consumerRecordArgument.value(), consumerRecordArgument.topic());
    }

    private void setKafkaHeaderToMdc(Headers headers) {
        Map<String, String> kafkaHeaders = new HashMap<>();

        headers.forEach(header -> kafkaHeaders.put(header.key(), new String(header.value(), StandardCharsets.UTF_8)));
        kafkaHeaders.put(PARENT_OPERATION_ID, kafkaHeaders.get(OPERATION_ID));
        kafkaHeaders.put(OPERATION_ID, super.getRandomNumber());

        MDC.setContextMap(kafkaHeaders);
    }
}
