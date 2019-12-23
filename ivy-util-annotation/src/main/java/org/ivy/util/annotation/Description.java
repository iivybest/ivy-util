package org.ivy.util.annotation;

import java.lang.annotation.*;

/**
 * <p> description: 注释说明
 * <br>--------------------------------------------------------
 * <br> 给类、方法添加标签
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className Tag
 * @date 2019/12/19 18:32
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
     *
     * 注释内容
     * @return String
     */
    public String[] value();

}

