package org.ivy.xutil.bean.convertor.introspector;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * BeanMapConvertor
 * POJO Map 转换器
 *
 * @author Ares miao.xl@live.cn
 * @version V1.0
 * @date 2017年3月28日 上午10:40:39
 */
public class BeanMapConvertor {

    public BeanMapConvertor() {
        super();
    }

    /**
     * handle Value
     *
     * @param value value
     * @param <T>   bean 类型
     * @return T
     */
    private <T> Object handleValue(T value) {
//		if (handlers == null || handlers.size() <= 0) return value;
//
//		Object handledValue = null;
//		for (BeanMapValueHandler handler : handlers) handledValue = handler.handle(value);
//		return handledValue;

        return value;
    }

    /**
     * convert java Bean to Map
     *
     * @param bean 要转化的JavaBean 对象
     * @param <T>  bean 类型
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public <T> Map<String, Object> convertBean2Map(T bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        if (bean == null) {
            return null;
        }
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

        String name;
        Object value;
        for (PropertyDescriptor e : descriptors) {
            name = e.getName();
            if (!"class".equals(name)) {
                value = e.getReadMethod().invoke(bean);
                rtnMap.put(name, (value != null) ? this.handleValue(value) : "");
            }
        }
        return rtnMap;
    }

    /**
     * convertBean2Map
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param type 要转化的类型
     * @param map  包含属性值的 map
     * @param <T>  bean 类型
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public <T> T convertMap2Bean(Map<String, ?> map, Class<T> type)
            throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (map == null) {
            return null;
        }

        // 获取类属性
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        // 创建 JavaBean 对象
        T bean = type.newInstance();
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            String property = descriptor.getName();
            if (map.containsKey(property)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                descriptor.getWriteMethod().invoke(bean, map.get(property));
            }
        }
        return bean;
    }
}
