package com.mybatis.jpa.util;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author svili
 */
public class PersistentUtil {

    public static String getTableName(Class<?> clazz) {
        return getTableName(clazz, true);
    }

    public static String getTableName(Class<?> clazz, boolean camelToUnderline) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            if (!table.name().trim().equals("")) {
                return table.name();
            }
        }
        String className = clazz.getSimpleName();

        if (!camelToUnderline) {
            return className;
        } else {
            return ColumnNameUtil.camelToUnderline(className);
        }
    }


    public static String getColumnName(Field field) {
        return getColumnName(field, true);
    }

    public static String getColumnName(Field field, boolean camelToUnderline) {
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            if (!column.name().trim().equals("")) {
                return column.name().toUpperCase();
            }
        }
        if (!camelToUnderline) {
            return field.getName();
        } else {
            return ColumnNameUtil.camelToUnderline(field.getName());
        }
    }

    public static List<Field> getPersistentFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if (isPersistentField(field)) {
                    list.add(field);
                }
            }
            searchType = searchType.getSuperclass();
        }
        return list;
    }

    public static boolean isPersistentField(Field field) {
        return !field.isAnnotationPresent(Transient.class);
    }
}
