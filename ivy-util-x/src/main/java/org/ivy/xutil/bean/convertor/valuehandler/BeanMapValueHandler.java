package org.ivy.xutil.bean.convertor.valuehandler;

import java.lang.reflect.Field;

/**
 * ValueTypeHandlerAble
 *
 * @author miao.xl
 * @version 1.0
 * @date 2016年8月3日-上午10:22:48
 */
public interface BeanMapValueHandler {

    /**
     * 处理 beanmap 中的 value
     *
     * @param bean  bean
     * @param field field
     * @param <T>   bean's type
     * @return T
     */
    public <T> Object handle(T bean, Field field);

}
