package by.shimakser.currencies.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currencies {

    @JsonProperty(value = "Date")
    private String date;
    @JsonProperty(value = "PreviousDate")
    private String previousDate;
    @JsonProperty(value = "PreviousURL")
    private String previousUrl;
    @JsonProperty(value = "Timestamp")
    private String timestamp;
    @JsonProperty(value = "Valute")
    private Map<String, Currency> valute;
}
