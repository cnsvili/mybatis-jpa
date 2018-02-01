package com.mybatis.jpa.annotation;

import java.lang.annotation.*;

/**
 * @author svili
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateDefinition {

    boolean selective() default false;

    String where();
}
