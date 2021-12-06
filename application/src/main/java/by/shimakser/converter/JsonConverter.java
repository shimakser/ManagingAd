package by.shimakser.converter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@NoArgsConstructor
public class JsonConverter extends AbstractBeanField {

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

        JSONObject jsonData;
        try {
            jsonData = new JSONObject(value);
        } catch (Exception ex) {
            jsonData = null;
        }
        return jsonData;
    }
}
