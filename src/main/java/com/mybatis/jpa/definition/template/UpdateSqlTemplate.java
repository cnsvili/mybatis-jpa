package com.mybatis.jpa.definition.template;

import com.mybatis.jpa.statement.ColumnMetaResolver;
import com.mybatis.jpa.util.PersistentUtil;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;

/**
 * @author svili
 **/
public class UpdateSqlTemplate implements SqlTemplate {

    @Override
    public String parseSQL(final Class<?> type) {
        return new SQL() {
            {
                UPDATE(PersistentUtil.getTableName(type));
                for (Field field : PersistentUtil.getPersistentFields(type)) {
                    if (PersistentUtil.updatable(field)) {
                        SET(field.getName() + " = " + ColumnMetaResolver.resolveSqlPlaceholder(field));
                    }
                }
            }
        }.toString();
    }

}
