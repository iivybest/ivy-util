package org.ivy.util.annotation;

import java.lang.annotation.*;

/**
 * <p>  classname: Description
 * <br> description: 注释说明
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/24 0:25
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
        ElementType.FIELD,
        ElementType.CONSTRUCTOR,
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.TYPE_PARAMETER,
        ElementType.TYPE_USE
})
@Inherited
@Documented
public @interface Description {

    /**
     * 注释内容
     *
     * @return String
     */
    String[] value() default "";

}

