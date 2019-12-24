package org.ivy.util.annotation;

import java.lang.annotation.*;

/**
 * <p>  classname: Recommend
 * <br> description: 是否为推荐写法注解
 * <br>---------------------------------------------------------
 * <br> 标明一个类、方法、字段、片段的写法是否为推荐写法
 * <br> 主要用用于学习、笔记、教学
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/18 15:12
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface Recommend {

    /**
     * 是否为推荐写法，true：推荐；false：不推荐
     *
     * @return boolean
     */
    boolean value() default true;

    /**
     * 相关描述
     *
     * @return String
     */
    String[] msg() default "";

}
