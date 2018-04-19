package com.mybatis.jpa.util;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.type.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author svili
 **/
public class ColumnMetaResolver {

    public static JdbcType resolveJdbcType(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return JdbcType.valueOf(alias);
        } catch (IllegalArgumentException e) {
            throw new BuilderException("Error resolving JdbcType. Cause: " + e, e);
        }
    }

    public static String resolveJdbcAlias(Field field) {

        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            if (column.columnDefinition() != null && !column.columnDefinition().equals("")) {
                return column.columnDefinition();
            }
        }

        Class<?> fieldType = field.getType();
        if (field.getType().isEnum()) {
            if (field.isAnnotationPresent(Enumerated.class)) {
                Enumerated enumerated = field.getAnnotation(Enumerated.class);
                if (Objects.equals(enumerated.value(), EnumType.ORDINAL)) {
                    return "INTEGER";
                }
            }
            return "VARCHAR";
        }
        if (Integer.class.equals(fieldType)) {
            return "INTEGER";
        }
        if (Double.class.equals(fieldType)) {
            return "DOUBLE";
        }
        if (Float.class.equals(fieldType)) {
            return "FLOAT";
        }
        if (String.class.equals(fieldType)) {
            return "VARCHAR";
        }
        if (java.util.Date.class.isAssignableFrom(fieldType)) {
            return "TIMESTAMP";
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Class<? extends TypeHandler<?>> resolveTypeHandler(Field field) {

        if (field.getType().isEnum()) {
            if (field.isAnnotationPresent(Enumerated.class)) {
                Enumerated enumerated = field.getAnnotation(Enumerated.class);
                if (enumerated.value() == EnumType.ORDINAL) {
                    EnumOrdinalTypeHandler<? extends Enum<?>> typeHandler = new EnumOrdinalTypeHandler(
                            field.getType());
                    return (Class<? extends TypeHandler<?>>) typeHandler.getClass();

                }
            }
            EnumTypeHandler<? extends Enum<?>> typeHandler = new EnumTypeHandler(field.getType());
            return (Class<? extends TypeHandler<?>>) typeHandler.getClass();
        }

        if (field.getType().equals(Boolean.class)) {
            return BooleanTypeHandler.class;
        }
        return null;
    }

    /**
     * 装配sql中动态参数的占位符 #{paramterName,jdbcType=,typeHandler=}
     */
    public static String resolveSqlPlaceholder(Field field) {

        return resolveSqlPlaceholder(field, "");
    }

    /**
     * 装配sql中动态参数的占位符 #{alias.parameterName,jdbcType=,typeHandler=}
     */
    public static String resolveSqlPlaceholder(Field field, String alias) {
        StringBuilder sqlParameter = new StringBuilder();
        sqlParameter.append("#{");
        if (alias != null && !"".equals(alias)) {
            sqlParameter.append(alias).append(".");
        }
        sqlParameter.append(field.getName());

        // jdbcType
        String jdbcType = resolveJdbcAlias(field);

        if (jdbcType != null) {
            sqlParameter.append(", jdbcType=").append(jdbcType);
        }
        // typeHandler
        Class<? extends TypeHandler<?>> typeHandler = resolveTypeHandler(field);

        if (typeHandler != null) {
            sqlParameter.append(", typeHandler=").append(typeHandler.getName());
        }
        sqlParameter.append("} ");

        return sqlParameter.toString();
    }
}
