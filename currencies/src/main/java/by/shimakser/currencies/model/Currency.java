package by.shimakser.currencies.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

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
