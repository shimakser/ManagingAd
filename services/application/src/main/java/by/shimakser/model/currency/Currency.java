package by.shimakser.model.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Currency")
public class Currency {

    @MongoId
    private String id;

    @Field(value = "num_code")
    private String numCode;

    @Field(value = "char_code")
    private String charCode;

    @Field(value = "nominal")
    private String nominal;

    @Field(value = "name")
    private String name;

    @Field(value = "value")
    private String value;

    @Field(value = "previous")
    private String previous;
}
