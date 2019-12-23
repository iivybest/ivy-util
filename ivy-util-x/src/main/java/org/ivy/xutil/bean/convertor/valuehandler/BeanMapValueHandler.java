/**
 * 
 */
package org.ivy.xutil.bean.convertor.valuehandler;

import java.lang.reflect.Field;

/**
 * <p>ValueTypeHandlerAble</p>
 *
 * @author miao.xl
 * @date 2016年8月3日-上午10:22:48
 * @version 1.0
 */
public interface BeanMapValueHandler {
	
	/**
	 * <p>处理beanmap中的value</p>
	 * @param value
	 * @return 
	 *
	 * @author miao.xl
	 * @date 2016年8月3日-上午10:30:30
	 */
	public <T> Object handle(T bean, Field field);
		
}
