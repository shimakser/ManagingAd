package by.shimakser.office.converter;

import by.shimakser.office.model.Contact;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return Arrays.stream(contacts)
                .map(contact -> contact.substring(1, contact.length() - 1))
                .map(contact -> contact.split(","))
                .map(fields -> new Contact(Long.parseLong(fields[0]), fields[1].trim(), fields[2].trim(), fields[3].trim()))
                .collect(Collectors.toList());
    }
}