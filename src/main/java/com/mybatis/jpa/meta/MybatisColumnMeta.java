package com.mybatis.jpa.meta;

import com.mybatis.jpa.util.PersistentUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;

/**
 *
 * @author svili
 *
 */
public class MybatisColumnMeta {

	/** Java fieldName */
	private String property;

	private String columnName;

	/** fieldType */
	private Class<?> type;

	/** mybatis jdbcTypeAlias */
	private String jdbcTypeAlias;

	/** mybatis jdbcType */
	private JdbcType jdbcType;

	/** mybatis typeHandler */
	private Class<? extends TypeHandler<?>> typeHandlerClass;

	/** persistence field */
	private Field field;

	public MybatisColumnMeta(Field field) {
		this.field = field;
		// 初始化
		this.property = field.getName();
		this.columnName = PersistentUtil.getColumnName(field);
		this.type = field.getType();
		this.jdbcTypeAlias = ColumnMetaResolver.resolveJdbcAlias(field);
		this.jdbcType = ColumnMetaResolver.resolveJdbcType(this.jdbcTypeAlias);
		this.typeHandlerClass = ColumnMetaResolver.resolveTypeHandler(field);
	}

	// getter

	public String getProperty() {
		return property;
	}

	public String getColumnName() {
		return columnName;
	}

	public Class<?> getType() {
		return type;
	}

	public String getJdbcTypeAlias() {
		return jdbcTypeAlias;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public Class<? extends TypeHandler<?>> getTypeHandlerClass() {
		return typeHandlerClass;
	}

	public Field getField() {
		return field;
	}

}
