package org.ivy.util.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * <p>
 * <br>---------------------------------------------------------
 * <br> description: Bean 工具类
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
     * internal function interface
     * support jdk less then 8
     *
     * @param <T> parameter type
     * @param <R> result type
     */
    public interface Function<T, R> {
        R apply(T t);
    }
    /**
     * internal Predicate interface
     * support jdk less then 8
     *
     * @param <T> parameter type
     */
    public interface Predicate<T> {
        boolean test(T t);
    }
    /**
     * internal Supplier interface
     * support jdk less then 8
     *
     * @param <T> parameter type
     */
    public interface Supplier<T> {
        T get();
    }
    /**
     * class是否包含该名称的 field
     *
     * @param type  类型
     * @param field field name
     * @param <T>   类型
     * @return boolean
     */
    public static <T> boolean containsFiled(Class<T> type, String field) {
        return getField(type, field) != null;
    }

    public static <T> Field getField(Class<T> type, String field) {
        Field cField = null;
        try {
            cField = type.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
        }
        return cField;
    }

    /**
     * 获取bean对象指定的field值
     *
     * @param bean  bean
     * @param field field
     * @param <T>   bean‘s type
     * @return T
     */
    public static <T> Object getFieldValue(T bean, Field field) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getFieldValueByReflect(bean, field);
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
    @Deprecated
    public static <T, R> R getFieldValueByIntrospect(T bean, Field field) {
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
    public static <T, R> R getFieldValueByReflect(T bean, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        R value = null;
        String methodName = "get" + StringUtil.firstCharUppercase(field.getName());
        Method method = bean.getClass().getMethod(methodName);
        if (method != null) {
            value = (R) method.invoke(bean);
        }
        return value;
    }

    public static <T> void setFiledValue(T bean, Field field, Object value) throws
            NoSuchMethodException,
            SecurityException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        String methodName = "set" + StringUtil.firstCharUppercase(field.getName());
        Method method = bean.getClass().getMethod(methodName, field.getType());
        if (method != null) {
            method.invoke(bean, value);
        }
    }

    public static <T> void setFiledValue(T bean, String field, Object value) throws
            NoSuchFieldException,
            SecurityException,
            NoSuchMethodException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        Field cField = bean.getClass().getDeclaredField(field);
        setFiledValue(bean, cField, value);
    }


    public static <S, T> void handleField (S bean, Class<T> type, Predicate<T> predicate, Supplier<T> supplier) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        String name;
        T value;
        for (PropertyDescriptor e : descriptors) {
            name = e.getName();
            if ("class".equals(name)) {
                continue;
            }
            if (e.getPropertyType() != type) {
                continue;
            }
            value = (T) e.getReadMethod().invoke(bean);
            if (predicate.test(value)) {
                setFiledValue(bean, name, supplier.get());
            }
        }
    }

    public static <S, T> void handleField(S bean, Class<T> type, Function<T, T> function) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        String name;
        T oVal, cVal;
        for (PropertyDescriptor e : descriptors) {
            name = e.getName();
            if ("class".equals(name)) {
                continue;
            }
            if (e.getPropertyType() != type) {
                continue;
            }
            oVal = (T) e.getReadMethod().invoke(bean);
            cVal = function.apply(oVal);
            /*
             * ----使用内省方式为 field 赋值，需要注意 setter 方法的返回值，
             * ----setter 返回值若为 void 则可行，否则抛出异常java.lang.NullPointerException
             * ----建议使用反射进行 field 重新赋值
             * ----e.getWriteMethod().invoke(bean, tVal);
             */
            setFiledValue(bean, name, cVal);
        }
    }




}
