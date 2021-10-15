package by.shimakser.converter;

import org.json.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {

    @Override
    public String convertToDatabaseColumn(JSONObject jsonObject) {
        String json;
        try {
            json = jsonObject.toString();
        } catch (NullPointerException ex) {
            json = "";
        }
        return json;
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonDataAsJson) {
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(jsonDataAsJson);
        } catch (Exception ex) {
            jsonData = null;
        }
        return jsonData;
    }
}
