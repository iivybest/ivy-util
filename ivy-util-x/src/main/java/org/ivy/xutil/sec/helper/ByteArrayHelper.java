package org.ivy.xutil.sec.helper;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Title: ByteArrayHelper 
 * @Description: Byte数组处理帮助类
 *
 * @author miao.xl  
 * @date 2017年1月10日 下午3:01:11 
 * @version V1.0
 */
public class ByteArrayHelper {
	
	/**
	 * @Title bytes2HexString 
	 * @Description TODO 
	 *
	 * @param bytes
	 * @return String
	 * @throws
	 */
	public static String bytes2HexString(byte[] bytes) {
		if(bytes == null) return null;
		
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i]);
		}
		return null;
	}
	
	/**
	 * @Title byte2String 
	 * @Description TODO 
	 *
	 * @param arg0
	 * @return String
	 * @throws
	 */
	public static String byte2String(byte[] arg0) {
		return null;
	}
	
	/**
	 * @Title bytesString2Bytes 
	 * @Description TODO 
	 *
	 * @param arg0
	 * @return byte[]
	 * @throws
	 */
	public static byte[] bytesString2Bytes(String arg0) {
		return null;
	}
	
	
	public static void main(String[] args) {
		Map<String, String> map = Collections.synchronizedMap(new LinkedHashMap<>());
		
		for(int i = 0; i < 2000000; i++) map.put("" + i, "" + i);
		
		System.out.println("map.size" + map.size());
		
		
		long start_1 = System.currentTimeMillis();
		for(String key : map.keySet()) if(Integer.valueOf(key) % 300000 == 0) System.out.println(key + ", " + map.get(key));
		long end_1 = System.currentTimeMillis();
		System.out.println("keySet　" + (end_1 - start_1));
		
		
		long start_2 = System.currentTimeMillis();
		for(Map.Entry<String, String> entry : map.entrySet()) 
			if(Integer.valueOf(entry.getKey()) % 300000 == 0) 
				System.out.println(entry.getKey() + ", " + entry.getValue());
		long end_2 = System.currentTimeMillis();
		System.out.println("entrySet " + (end_2 - start_2));
		
		long start_3 = System.currentTimeMillis();
		Iterator<String> i = map.keySet().iterator();
		String key = null;
		while(i.hasNext()) {
			key = i.next();
			if(Integer.valueOf(key) % 300000 == 0) 
				System.out.println(key + ", " + map.get(key));
		}
			
		long end_3 = System.currentTimeMillis();
		System.out.println("keySet.Iterator " + (end_3 - start_3));
		
		
	}
	
	
	
	
	
	
}
