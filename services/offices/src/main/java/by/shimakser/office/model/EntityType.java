package by.shimakser.office.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityType {
    OFFICE(Office.class), CONTACT(Contact.class);

    private Class<?> clazz;
}
