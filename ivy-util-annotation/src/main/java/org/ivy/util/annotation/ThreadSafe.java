package org.ivy.util.annotation;

import java.lang.annotation.*;

/**
 * <p> description: the annotation of weather thread safe
 * <br>---------------------------------------------------------
 * <br> # Indicates whether the class or method is thread safe.
 * <br> # Mainly used for learning, note taking and teaching
 * <br> # Not recommended for production environments
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/18 14:53
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ThreadSafe {

    /**
     * Thread Weather Safe
     *
     * @return boolean
     */
    boolean value() default true;

    /**
     * message information
     *
     * @return String
     */
    String[] msg() default "";

}
