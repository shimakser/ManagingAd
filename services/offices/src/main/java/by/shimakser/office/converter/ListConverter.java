package by.shimakser.office.converter;

import by.shimakser.office.model.Contact;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ListConverter extends AbstractBeanField<List<Contact>> {

    @Override
    protected List<Contact> convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (value.equals("[]") || (value.equals(""))) {
            return new ArrayList<>();
        }

        String contacts = value.substring(8, value.length() - 1);
        return contacts.contains(", Contact")
                ? contactConverter(contacts.split(", Contact"))
                : contactConverter(new String[]{contacts});
    }

    private List<Contact> contactConverter(String[] contacts) {
        return null;
    }
}