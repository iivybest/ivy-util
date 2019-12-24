package org.ivy.xutil.sec.helper;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author miao.xl
 * @version V1.0
 * ByteArrayHelper
 * Byte数组处理帮助类
 * @date 2017年1月10日 下午3:01:11
 */
public class ByteArrayHelper {

    /**
     * @param bytes
     * @return String
     * @throws bytes2HexString
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) return null;

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i]);
        }
        return null;
    }

    /**
     * @param arg0
     * @return String
     * @throws byte2String
     */
    public static String byte2String(byte[] arg0) {
        return null;
    }

    /**
     * @param arg0
     * @return byte[]
     * @throws bytesString2Bytes
     */
    public static byte[] bytesString2Bytes(String arg0) {
        return null;
    }


    public static void main(String[] args) {
        Map<String, String> map = Collections.synchronizedMap(new LinkedHashMap<>());

        for (int i = 0; i < 2000000; i++) map.put("" + i, "" + i);

        System.out.println("map.size" + map.size());


        long start_1 = System.currentTimeMillis();
        for (String key : map.keySet())
            if (Integer.valueOf(key) % 300000 == 0) System.out.println(key + ", " + map.get(key));
        long end_1 = System.currentTimeMillis();
        System.out.println("keySet　" + (end_1 - start_1));


        long start_2 = System.currentTimeMillis();
        for (Map.Entry<String, String> entry : map.entrySet())
            if (Integer.valueOf(entry.getKey()) % 300000 == 0)
                System.out.println(entry.getKey() + ", " + entry.getValue());
        long end_2 = System.currentTimeMillis();
        System.out.println("entrySet " + (end_2 - start_2));

        long start_3 = System.currentTimeMillis();
        Iterator<String> i = map.keySet().iterator();
        String key = null;
        while (i.hasNext()) {
            key = i.next();
            if (Integer.valueOf(key) % 300000 == 0)
                System.out.println(key + ", " + map.get(key));
        }

        long end_3 = System.currentTimeMillis();
        System.out.println("keySet.Iterator " + (end_3 - start_3));


    }


}
