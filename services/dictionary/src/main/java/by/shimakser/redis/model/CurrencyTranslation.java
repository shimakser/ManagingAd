package by.shimakser.redis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Data
@Document(collection = "CurrencyTranslation")
@AllArgsConstructor
public class CurrencyTranslation implements Serializable {

    @MongoId
    @JsonIgnore
    private Long id;

    @Field(value = "char_code")
    private String charCode;

    @Field(value = "translation")
    private String translation;
}
