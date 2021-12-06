package by.shimakser.converter;

import by.shimakser.model.office.Contact;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ListConverter extends AbstractBeanField {

    @Override
    protected List convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (value.equals("[]") || (value.equals(""))) {
            return new ArrayList<Contact>();
        }

        String contacts = value.substring(1, value.length() - 1);
        return contacts.contains(", ")
                ? contactConverter(contacts.split(", "))
                : contactConverter(new String[]{contacts});
    }

    private List<Contact> contactConverter(String[] contacts) {
        return Arrays.stream(contacts)
                .map(contact -> contact.split(","))
                .map(fields -> new Contact(Long.parseLong(fields[0]), fields[1], fields[2], fields[3]))
                .collect(Collectors.toList());
    }
}