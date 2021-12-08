package by.shimakser.converter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@NoArgsConstructor
public class JsonConverter extends AbstractBeanField<JSONObject> {

    @Override
    protected JSONObject convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return (value.equals("") || value.equals("{}"))
                ? new JSONObject()
                : new JSONObject(value);
    }
}
