package org.ivy.util.common;


import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

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
     * @param arg0 argument
     * @return boolean result
     */
    public static boolean isNonBlank(String arg0) {
        if (arg0 == null) {
            return false;
        }
        return !isBlank(arg0.trim());
    }


    /**
     * 检查字符串是否为空
     *
     * @param arg0 argument
     * @return boolean result
     */
    public static boolean isBlank(String arg0) {
        return arg0 == null || arg0.length() == 0;
    }


    /**
     * 判断字符串是否为数字类型
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        if (str.startsWith("-")) {
            str = str.substring(1, str.length());
        }
        if (str.contains(".")) {
            int idx = str.indexOf(".");
            if (idx == 0 || idx == str.length() - 1) {
                return false;
            }
            str = str.replaceFirst("\\.", "");
            if (str.contains(".")) {
                return false;
            }
        }
        for (int i = 0, sz = str.length(); i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }


    /**
     * 多个字段中是否包含空字段
     *
     * @param args arguments
     * @return boolean result
     */
    public static boolean containsBlank(String... args) {
        if (args == null) {
            return true;
        }
        for (String e : args) {
            if (isBlank(e)) {
                return true;
            }
        }
        return false;
    }


    /**
     * fist Char Uppercase
     *
     * @param arg0 argument
     * @return String
     */
    public static String firstCharUppercase(String arg0) {
        if (isBlank(arg0)) {
            return arg0;
        }
        return arg0.substring(0, 1).toUpperCase() + arg0.substring(1, arg0.length());
    }

    /**
     * first Char Lowercase
     *
     * @param arg0 argument
     * @return String
     */
    public static String firstCharLowerCase(String arg0) {
        if (isBlank(arg0)) {
            return arg0;
        }
        return arg0.substring(0, 1).toLowerCase() + arg0.substring(1, arg0.length());
    }

    /**
     * 获取定长字符串
     *
     * @param data   data
     * @param length 长度
     * @param pad    填充字符
     * @param pos    填充位置，大于零右侧填充，小于零左侧填充
     * @return 定长字符串
     */
    public static String getFixedLengthString(String data, int length, char pad, int pos) {
        if (length < 0) {
            return null;
        }
        String result;
        int originLen = isBlank(data) ? 0 : data.length();
        int offset = length - originLen;
        if (offset == 0) {
            result = data;
        } else if (offset < 0) {
            result = data.substring(0, length);
        } else {
            char[] sequence = new char[offset];
            Arrays.fill(sequence, pad);
            String pads = new String(sequence);
            result = pos > 0 ? data + pads : pads + data;
        }
        return result;
    }

    public static String getFixedLengthString(String data, int length) {
        return getFixedLengthString(data, length, ' ', 1);
    }


    /**
     * 将异常中所有堆栈信息转换为String
     *
     * @param t throwable
     * @return String String
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

    /**
     * whole indentation
     *
     * @param data   data
     * @param offset indentation offset
     * @return String
     */
    public static String wholeIndentation(String data, int offset) {
        if (isBlank(data)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] items = data.split("\n");
        for (int i = 0, len = items.length; i < len; i++) {
            String step = "";
            for (int j = 0; j < offset; j++) {
                step += "\t";
            }
            result.append(step + items[i] + "\n");
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
     * char to unicode string
     *
     * @param data data
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
     * @return String
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
        if (t == null) {
            return false;
        }
        if (t.equals(Map.class)) {
            return true;
        }
        for (Class<?> i : t.getInterfaces()) {
            if (isMap(i)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean isCollection(Class<T> t) {
        if (t == null) {
            return false;
        }
        if (t.equals(Collection.class)) {
            return true;
        }
        for (Class<?> i : t.getInterfaces()) {
            if (isCollection(i)) {
                return true;
            }
        }
        return false;
    }


}
