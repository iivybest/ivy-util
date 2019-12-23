package org.ivy.xutil.bean.convertor.keyhandler;

/**
 * <p>BeanMapKeyHandler</p>
 *
 * @author miao.xl
 * @date 2016年8月3日-上午10:22:48
 * @version 1.0
 */
public interface BeanMapKeyHandler {
	
	/**
	 * <p>处理beanmap中的key,[由bean 的 field 映射到 map 的 key]</p>
	 * @param value
	 * @return 
	 *
	 * @author miao.xl
	 * @date 2016年8月3日-上午10:30:30
	 */
	public String handle(String key);
		
}
