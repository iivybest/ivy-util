package org.ivy.xutil.bean.convertor.valuehandler.impl;

import org.ivy.util.common.BeanUtil;
import org.ivy.util.common.DateTimeUtil;
import org.ivy.xutil.bean.convertor.valuehandler.BeanMapValueHandler;
import org.ivy.xutil.bean.convertor.valuehandler.annotation.DateFormat;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * DateTypeValueHandler
 * 日期类型数据处理
 *
 * @author Ares miao.xl@live.cn
 * @version V1.0
 * @date 2017年3月28日 上午10:32:49
 */
public class DateTypeValueHandler implements BeanMapValueHandler {

    @Override
    public <T> Object handle(T bean, Field field) throws Exception {
        // 入参为null，直接返回null
        if (null == bean || null == field) {
            return null;
        }
        // field's value 值为 null，直接返回 null。
        if (null == BeanUtil.getFieldValue(bean, field)) {
            return null;
        }
        // field 为非Date类型，直接返回值。
        if (!field.getType().isAssignableFrom(Date.class)) {
            return BeanUtil.getFieldValue(bean, field);
        }
        // 注解 DateFormat 不为 null，且 df.format() is true 按用户定义格式格式化日期，否则返回 field 原始值
        DateFormat df = field.getDeclaredAnnotation(DateFormat.class);
        if (null != df && df.format()) {
            return DateTimeUtil.format((Date) BeanUtil.getFieldValue(bean, field), df.pattern());
        } else {
            return BeanUtil.getFieldValue(bean, field);
        }
    }

}
