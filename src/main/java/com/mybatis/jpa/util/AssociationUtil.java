package com.mybatis.jpa.util;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;

/***
 *
 * @author svili
 *
 */
public class AssociationUtil {

    public static Class<?> getTargetType(Field field) {
        if (field.isAnnotationPresent(OneToOne.class)) {
            OneToOne one = field.getAnnotation(OneToOne.class);
            if (!one.targetEntity().equals(void.class)) {
                return one.targetEntity();
            }
        }
        return void.class;
    }

    public static String getMappedName(Field field) {
        if (field.isAnnotationPresent(OneToOne.class)) {
            OneToOne one = field.getAnnotation(OneToOne.class);
            if (!one.mappedBy().trim().equals("")) {
                return one.mappedBy();
            }
        }
        return null;
    }

    public static boolean isAssociationField(Field field) {
        return field.isAnnotationPresent(OneToOne.class) ||
                field.isAnnotationPresent(OneToMany.class) ||
                field.isAnnotationPresent(ManyToOne.class) ||
                field.isAnnotationPresent(ManyToMany.class);
    }

}
