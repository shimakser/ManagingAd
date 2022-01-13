package by.shimakser.currencies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Currencies {

    public static void main(String[] args) {
        SpringApplication.run(Currencies.class, args);
    }
}
