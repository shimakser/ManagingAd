package by.shimakser.controller;

import by.shimakser.kafka.model.NumbersRequest;
import by.shimakser.kafka.service.ProducerService;
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
        return producerService.send(request);
    }
}
