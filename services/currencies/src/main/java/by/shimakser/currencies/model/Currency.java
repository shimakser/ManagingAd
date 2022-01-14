package by.shimakser.currencies.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(includeFieldNames = false)
@Document(collection = "Currencies")
public class Currency {

    @MongoId
    @JsonProperty(value = "ID")
    private String id;

    @Field(value = "num_code")
    @JsonProperty(value = "NumCode")
    private String numCode;

    @Field(value = "char_code")
    @JsonProperty(value = "CharCode")
    private String charCode;

    @Field(value = "nominal")
    @JsonProperty(value = "Nominal")
    private String nominal;

    @Field(value = "name")
    @JsonProperty(value = "Name")
    private String name;

    @Field(value = "value")
    @JsonProperty(value = "Value")
    private String value;

    @Field(value = "previous")
    @JsonProperty(value = "Previous")
    private String previous;

    @Field(value = "upd_date")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updDate;
}
