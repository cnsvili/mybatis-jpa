package com.mybatis.jpa.statement;

import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * @author svili
 **/
public interface StatementBuildable {

    MappedStatement parseStatement(Method method);
}
