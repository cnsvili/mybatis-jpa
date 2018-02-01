package com.mybatis.jpa.definition.template;

import com.mybatis.jpa.statement.ColumnMetaResolver;
import com.mybatis.jpa.util.PersistentUtil;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;

/**
 * @author svili
 **/
public class InsertSqlTemplate implements SqlTemplate {

    @Override
    public String parseSQL(final Class<?> type) {
        return new SQL() {
            {
                INSERT_INTO(PersistentUtil.getTableName(type));
                for (Field field : PersistentUtil.getPersistentFields(type)) {
                    if (PersistentUtil.insertable(field)) {
                        VALUES(field.getName(), ColumnMetaResolver.resolveSqlPlaceholder(field));
                    }
                }
            }
        }.toString();
    }

}
