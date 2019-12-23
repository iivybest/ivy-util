/**
 *
 */
package org.ivy.xutil.bean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Title: BeanUtil
 * @Description: Bean 工具类
 * @author Ares
 * @date 2017年3月28日 上午10:15:06 
 * @version V1.0
 */
public class BeanUtil {

    /**
     * @Title getFieldValue
     * @Description 获取bean对象指定的field值
     * @param bean
     * @param field
     * @return R
     */
    public static <T> Object getFieldValue(T bean, Field field) {
        return getFieldValueByReflect(bean, field);
    }

    public static <T> void setFiledValue(T bean, Field field, Object value) throws
            NoSuchMethodException,
            SecurityException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        String methodname = "set" + field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1, field.getName().length());
        Method method = bean.getClass().getMethod(methodname, field.getType());
        if (method != null) method.invoke(bean, value);
    }

    public static <T> void setFiledValues(T bean, String field, Object value) throws
            NoSuchFieldException,
            SecurityException,
            NoSuchMethodException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        Field _field = bean.getClass().getDeclaredField(field);
        setFiledValue(bean, _field, value);
    }


    /**
     * @Title getFieldValueByIntrospector
     * @Description 通过内省机制获取bean对象中指定field值
     * @param bean
     * @param field
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R getFieldValueByIntrospector(T bean, Field field) {
        R value = null;
        try {
            BeanInfo bif = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pds = bif.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                if (pd.getName().equals(field.getName())) {
                    value = (R) pd.getReadMethod().invoke(bean, new Object[0]);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * @Title getFieldValueByReflect
     * @Description 通过反射获取bean对象指定field值
     * @param bean
     * @param field
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R getFieldValueByReflect(T bean, Field field) {
        R value = null;
        try {
            String methodname = "get" + field.getName().substring(0, 1).toUpperCase()
                    + field.getName().substring(1, field.getName().length());
            Method method = bean.getClass().getMethod(methodname);
            if (method != null) value = (R) method.invoke(bean);
        } catch (NoSuchMethodException
                | SecurityException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }


}
