package org.ivy.xutil.sec;

import java.util.Base64;

/**
 * <p>  classname: Base64Util
 * <br> description: Base64 Util
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2018/4/19 18:17
 * @since 1.8+
 */
public class Base64Util {


    public static byte[] encode(byte[] data) {
        if (data == null) return null;
        return Base64.getEncoder().encode(data);
    }

    public static byte[] encode(String data) {
        return encode(data.getBytes());
    }

    public static String encodeToString(byte[] data) {
        if (data == null) return null;
        return Base64.getEncoder().encodeToString(data);
    }

    public static String encodeToString(String data) {
        return encodeToString(data.getBytes());
    }

    public static byte[] decode(String data) {
        if (null == data || data.length() == 0) return null;
        return Base64.getDecoder().decode(data);
    }

    public static byte[] decode(byte[] data) {
        if (data == null) return null;
        return Base64.getDecoder().decode(data);
    }

    public static String decodeToString(String data) {
        if (null == data || data.length() == 0) return null;
        return new String(decode(data));
    }

    public static String decodeToString(byte[] data) {
        if (data == null) return null;
        return new String(decode(data));
    }

}


