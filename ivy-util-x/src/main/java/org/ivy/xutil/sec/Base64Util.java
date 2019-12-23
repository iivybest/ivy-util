package org.ivy.xutil.sec;

import java.util.Base64;

/** 
 * @Classname	Base64Util
 * @author 		Ivybest imiaodev@163.com
 * @Createdate 	2018年4月19日 下午4:22:44
 * @Version		1.0
 * ------------------------------------------
 * @Description 使用JDK8提供的base64工具
 */
public class Base64Util {



	public static byte[] encode(byte[] data) {
		if(data == null) return null;
		return Base64.getEncoder().encode(data);
	}
	
	public static byte[] encode(String data) {
		return encode(data.getBytes());
	}
	
	public static String encodeToString(byte[] data) {
		if(data == null) return null;
		return Base64.getEncoder().encodeToString(data);
	}
	
	public static String encodeToString(String data) {
		return encodeToString(data.getBytes());
	}
	
	public static byte[] decode(String data) {
		if(null == data || data.length() == 0) return null;
		return Base64.getDecoder().decode(data);
	}
	
	public static byte[] decode(byte[] data) {
		if(data == null) return null;
		return Base64.getDecoder().decode(data);
	}
	
	public static String decodeToString(String data) {
		if(null == data || data.length() == 0) return null;
		return new String(decode(data));
	}
	
	public static String decodeToString(byte[] data) {
		if(data == null) return null;
		return new String(decode(data));
	}
	
}


