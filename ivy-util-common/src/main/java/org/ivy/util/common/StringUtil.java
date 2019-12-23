package org.ivy.util.common;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> String 工具类
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className StringUtil
 * @date 2014/6/13 19:10
 */
public class StringUtil {

    private static Collection<String> baseType;
    private static Collection<String> primitive;

    static {
        baseType = new HashSet<String>();
        baseType.add("byte");
        baseType.add("short");
        baseType.add("int");
        baseType.add("long");
        baseType.add("float");
        baseType.add("double");
        baseType.add("char");

        primitive = new HashSet<String>();
        primitive.add("date");
    }

    /**
     * 检查字符串是否非空
     *
     * @param arg0
     * @return boolean
     */
    public static boolean isNonBlank(String arg0) {
        return ! isBlank(arg0.trim());
    }


    /**
     * 检查字符串是否为空
     *
     * @param arg0
     * @return boolean
     */
    public static boolean isBlank(String arg0) {
        return arg0 == null || arg0.length() == 0;
    }

    /**
     * @param args
     * @return boolean
     * @Title: containsBlank
     * @Description: 多个字段中是否包含空字段
     */
    public static boolean containsBlank(String... args) {
        if (args == null) return true;
        for (String e : args) if (isBlank(e)) return true;
        return false;
    }


    /**
     * fist Char Uppercase
     *
     * @param origin
     * @return
     * @author miao.xl
     * @date 2014年11月17日 下午8:20:30
     * @version 1.0
     */
    public static String fistCharUppercase(String origin) {
        return origin.substring(0, 1).toUpperCase() + origin.substring(1, origin.length());
    }

    /**
     * first Char Lowercase
     *
     * @param origin
     * @return
     * @author miao.xl
     * @date 2014年11月18日 上午10:36:44
     * @version 1.0
     */
    public static String firstCharLowerCase(String origin) {
        return origin.substring(0, 1).toLowerCase() + origin.substring(1, origin.length());
    }

    /**
     * 将异常中所有堆栈信息转换为String
     *
     * @param t
     * @return
     */
    public static String getFullStackTrace(Throwable t) {
        String fullStackTraceMessage = null;
        try (StringWriter out = new StringWriter();
             PrintWriter pw = new PrintWriter(out);) {
            t.printStackTrace(pw);
            fullStackTraceMessage = out.getBuffer().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullStackTraceMessage;
    }


    public static String wholeIndentation(String original, int offset) {
        if (original == null || original.length() <= 0) return "";

        StringBuffer result = new StringBuffer();
        String[] items = original.split("\n");
        for (int i = 0; i < items.length; i++) {
            String offsets = "";
            for (int j = 0; j < offset; j++) offsets += "\t";
            result.append(offsets + items[i] + "\n");
        }
        return result.toString().substring(0, result.length() - 1);
    }




    public static String toHexString(String data) {
        return DigitUtil.toHexString(data.getBytes());
    }

    /**
     * 将字符串转换为 Unicode 码
     *
     * @param data
     * @return String
     */
    public static String toUnicodeString(String data) {
        if (data == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char e : data.toCharArray()) {
            sb.append(toUnicodeString(e));
        }
        return sb.toString();
    }

    /**
     * <p>description: 将 char 转为 Unicode 码
     * <br>----------------------------------------------
     * <br> char 长度为 16 bit，转为 Hex 为 4 位。
     * <br> 先处理 char 高 8 位，data >>> 8 | data & 0xFF00，
     * <br>	先处理 char 低 8 位，data & 0xFF | data & 0x00FF，
     * <br> 每 8 位转换为十六进制长度为 2，若长度为 1，前面补 0。
     * <br>----------------------------------------------
     * <p/>
     *
     * @param data
     * @return String
     */
    public static String toUnicodeString(char data) {
        return "\\u" + DigitUtil.toHexString(data);
    }


    public static <T> String collection2String(T t) {
        return "";
    }

    public static <T> String type2String(T t) {
        String str = "";
        if (t instanceof Map<?, ?>) {

        } else if (t instanceof Collection<?>) {

        } else if (t instanceof Enumeration<?>) {

        } else if (t instanceof Iterator<?>) {

        } else if (t.getClass().isArray()) {

        } else if (t instanceof Date) {

        } else {
            str = t.toString();
        }
        return str;
    }


    /**
     * 打印bean类所有信息
     *
     * @param bean
     * @return
     * @author miao.xl
     * @date 2015年10月23日 上午10:04:06
     * @version 1.0
     */
    public static <T> String bean2String(T bean) {
        if (bean == null) {
            return "[]";
        }
        StringBuffer res = new StringBuffer("[");
        try {
            PropertyDescriptor[] pds = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                if (!"class".equals(pd.getName())) {
                    Class<?> type = pd.getPropertyType();
                    String typeName = type.getSimpleName().toLowerCase();
                    Object val = pd.getReadMethod().invoke(bean, new Object[0]);
                    res.append(pd.getName() + "=");
                    if (baseType.contains(typeName) || primitive.contains(typeName)
                            || "string".equals(typeName) || isCollection(type)) {
                        res.append(val + ", ");
                    } else if (isMap(type)) {
                        res.append("" + ", ");
                    } else {
                        res.append(bean2String(val) + ", ");
                    }
                }
            }
            if (res.length() > 1) {
                res = res.delete(res.length() - 2, res.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.append("]");
        return res.toString();
    }


    public static <T> boolean isMap(Class<T> t) {
        if (t == null) return false;
        if (t.equals(Map.class)) return true;

        for (Class<?> i : t.getInterfaces()) {
            if (isMap(i)) return true;
        }
        return false;
    }

    public static <T> boolean isCollection(Class<T> t) {
        if (t == null) return false;
        if (t.equals(Collection.class)) return true;

        for (Class<?> i : t.getInterfaces()) if (isCollection(i)) return true;
        return false;
    }


    public static void main(String[] args) {
        byte[] b = {0, 15, -117, -23, -30, -118, -111, 103, 47, 19, 66, -95, 74, -35, 110, 60, -119, -86};

        Class<?> type = b.getClass();
        // 数组中元素class类型
        Class<?> itemType = Array.get(b, 0).getClass();

        System.out.println("---->" + type.getName());
        System.out.println("---->" + type.isArray());


        Class<?>[] types = {
                String.class,
                HashMap.class,
                HashSet.class,
                ConcurrentHashMap.class,
                Date.class,

                StringUtil.class
        };

        for (Class<?> item : types) {
            System.out.println(item.getName());
            System.out.println(isMap(item));
        }


    }


}
