package org.ivy.xutil.bean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * <p> description: Bean 工具类
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2017/3/28 10:13
 */
public class BeanUtil {

    /**
     * 获取bean对象指定的field值
     *
     * @param bean  bean
     * @param field field
     * @param <T>   bean‘s type
     * @return T
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
                + field.getName().substring(1);
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
     * 通过内省机制获取bean对象中指定field值
     *
     * @param bean  bean
     * @param field field
     * @param <T>   bean type
     * @param <R>   return bean type
     * @return R
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
     * 通过反射获取bean对象指定field值
     *
     * @param bean  bean
     * @param field field
     * @param <T>   bean type
     * @param <R>   return bean type
     * @return R
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R getFieldValueByReflect(T bean, Field field) {
        R value = null;
        try {
            String methodname = "get" + field.getName().substring(0, 1).toUpperCase()
                    + field.getName().substring(1);
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
