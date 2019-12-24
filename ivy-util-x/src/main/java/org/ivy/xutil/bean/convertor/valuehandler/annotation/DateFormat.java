/**
 *
 */
package org.ivy.xutil.bean.convertor.valuehandler.annotation;

import java.lang.annotation.*;

/**
 *  DateFormat
 *
 *
 * @author Ares
 * @date 2017年4月13日 上午9:06:33 
 * @version V1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DateFormat {
    /**
     * whether format
     * @return boolean
     */
    boolean format() default true;

    /**
     * date format pattern
     * @return String
     */
    String pattern() default "yyyy-MM-dd HH:mm:ss";

}
