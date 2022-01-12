package by.shimakser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyDto {

    @JsonProperty(value = "ID")
    private String id;
    @JsonProperty(value = "NumCode")
    private String numCode;
    @JsonProperty(value = "CharCode")
    private String charCode;
    @JsonProperty(value = "Nominal")
    private String nominal;
    @JsonProperty(value = "Name")
    private String name;
    @JsonProperty(value = "Value")
    private String value;
    @JsonProperty(value = "Previous")
    private String previous;
}
