package by.shimakser.office.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldNameAnalyzer {

    public static List<String> checkFieldsNames(Object object) {
        Class<?> clazz = object.getClass();
        List<String> columnNames = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExportField.class)) {
                String annotArg = field.getAnnotation(ExportField.class).name();
                String name = annotArg.equals("")
                        ? field.getName()
                        : annotArg;
                columnNames.add(name);
            }
        }
        return columnNames;
    }
}
