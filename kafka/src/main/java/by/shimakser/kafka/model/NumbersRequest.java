package by.shimakser.kafka.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumbersRequest {

    private Integer firstNumber;
    private Integer secondNumber;
    private Integer sum = 0;
}
