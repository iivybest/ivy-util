package org.ivy.util.annotation;

import java.lang.annotation.*;

/**
 * <p> description: 线程安全注解
 * <br>--------------------------------------------------------
 * <br> 标明类、方法是否线程安全。
 * <br> 主要用于学习、笔记、教学
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className ThreadSafe
 * @date 2019/12/18 14:53
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@Documented
@Inherited
public @interface ThreadSafe {

    /**
     * 线程是否安全
     *
     * @return boolean
     */
    public boolean value() default true;

    /**
     * 使用注解类关于线程安全的描述
     *
     * @return String
     */
    public String[] msg();

}
