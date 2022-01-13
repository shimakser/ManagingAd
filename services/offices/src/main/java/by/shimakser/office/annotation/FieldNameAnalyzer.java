package by.shimakser.office.annotation;

import by.shimakser.dto.HeaderField;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldNameAnalyzer {

    public static List<HeaderField> checkFieldsNames(Class<?> clazz) {
        List<HeaderField> headColumns = new ArrayList<>();
        HeaderField headerField;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExportField.class)) {
                Class<?> fieldType = field.getType();

                if (checkByDefaultTypes(fieldType)) {
                    // check fields by primitive or default type
                    headerField = new HeaderField(getFieldName(field));
                    headColumns.add(headerField);
                } else if (fieldType.isAssignableFrom(List.class)) {
                    // check lists
                    headerField = checkLists(field);
                    headColumns.add(headerField);
                } else {
                    // processes custom entity
                    checkFieldsNames(fieldType);
                }
            }
        }
        return headColumns;
    }

    private static String getFieldName(Field field) {
        String annotArg = field.getAnnotation(ExportField.class).name();
        return annotArg.equals("")
                ? field.getName()
                : annotArg;
    }

    private static boolean checkByDefaultTypes(Class<?> fieldType) {
        AtomicBoolean check = new AtomicBoolean(false);

        List<Class<?>> types = List.of(String.class, Long.class, Double.class, Integer.class,
                Enum.class, Float.class, Short.class, Byte.class);
        types.forEach(type -> {
            if (fieldType.isPrimitive() || fieldType.isAssignableFrom(type)) {
                check.set(true);
            }
        });

        return check.get();
    }

    private static HeaderField checkLists(Field field) {
        ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
        Class<?> collectionGenericType = (Class<?>) collectionType.getActualTypeArguments()[0];

        if (checkByDefaultTypes(collectionGenericType)) {
            // processes list with default generic type
            return new HeaderField(getFieldName(field));
        } else {
            // processes list with custom generic type
            List<HeaderField> subColumns = checkFieldsNames(collectionGenericType);
            return new HeaderField(getFieldName(field), subColumns);
        }
    }
}
