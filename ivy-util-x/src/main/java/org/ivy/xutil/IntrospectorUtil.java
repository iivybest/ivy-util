package org.ivy.xutil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <p>IntrospectorUtil</p>
 * <p>TODO</p>
 *
 * @author miao.xl
 * @date 2016年3月8日-上午10:49:01
 * @version 1.0
 *
 */
public class IntrospectorUtil {
	public <T> String bean2String(T t) {
		StringBuffer sb = new StringBuffer(t.toString());
		sb.append("[");
		try {
			BeanInfo bif = Introspector.getBeanInfo(t.getClass());
			PropertyDescriptor[] pds = bif.getPropertyDescriptors();
			for(PropertyDescriptor pd : pds) {
				if(! "class".equals(pd.getName())) {
					Object val = pd.getReadMethod().invoke(t, new Object[0]);
					sb.append(pd.getName() + ":" + type2String(val) + ",");
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
//		if(sb.toString().endsWith(",")) sb.substring(0, sb.length() - 1);
		sb.deleteCharAt(sb.lastIndexOf(","));
		
		sb.append("]");
		return sb.toString();
	}
	
	private static <T> String type2String(T t) {
		String str = "";
		if(t instanceof Map<?, ?>) {
			
		} else if(t instanceof Collection<?>)  {
			
		} else if(t instanceof Enumeration<?>) {
			
		} else if(t instanceof Iterator<?>) {
			
		} else if(t.getClass().isArray()) {
			
		} else if(t instanceof Date) {
//			str = DateTimeUtil.formateDate((Date)t);
		} else {
			str = t.toString();
		}
		return str;
	}
	
	
}
