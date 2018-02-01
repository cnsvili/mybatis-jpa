package com.mybatis.jpa.definition.adaptor;

import com.mybatis.jpa.definition.template.SqlTemplate;
import com.mybatis.jpa.definition.property.AnnotationProperty;
import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.annotation.Annotation;

/**
 * @author svili
 **/
public interface AnnotationAdaptable {

    AnnotationProperty context(Annotation annotation);

    SqlTemplate sqlTemplate(Annotation annotation);

    SqlCommandType sqlCommandType();

}
