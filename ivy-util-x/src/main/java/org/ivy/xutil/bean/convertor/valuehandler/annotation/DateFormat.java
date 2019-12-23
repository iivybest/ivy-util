/**
 *
 */
package org.ivy.xutil.bean.convertor.valuehandler.annotation;

import java.lang.annotation.*;

/**
 * @Title DateFormat
 * @Description TODO
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
     * @Title format
     * @Description 是否进行格式化
     * @return
     */
    public boolean format() default true;

    /**
     * @Title pattern
     * @Description 日期格式化样式
     * @return
     */
    public String pattern() default "yyyy-MM-dd HH:mm:ss";

}
