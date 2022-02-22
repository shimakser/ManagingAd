package by.shimakser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updDate;
}
