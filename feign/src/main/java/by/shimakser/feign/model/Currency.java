package by.shimakser.feign.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Currency")
public class Currency {

    @Field(value = "_id")
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

    public String getId() {
        return id;
    }

    public String getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPrevious() {
        return previous;
    }

    public String getNum() {
        return num;
    }
}
