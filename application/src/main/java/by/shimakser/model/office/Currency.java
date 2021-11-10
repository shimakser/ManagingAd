package by.shimakser.model.office;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "Currency")
public class Currency {

    @Id
    private String id;

    @Field(value = "num_code")
    private String numCode;

    @Field(value = "created_date")
    private String charCode;

    @Field(value = "nominal")
    private String nominal;

    @Field(value = "name")
    private String name;

    @Field(value = "value")
    private String value;

    @Field(value = "previous")
    private String previous;

    @Field(value = "num")
    private String num;
}
