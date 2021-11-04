package by.shimakser.controller.kafka;

import by.shimakser.dto.NumbersRequest;
import by.shimakser.service.kafka.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    private final ProducerService producerService;

    @Autowired
    public KafkaController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public NumbersRequest sum(@RequestBody NumbersRequest request) throws ExecutionException, InterruptedException {
        return producerService.sendNumbers(request);
    }
}
