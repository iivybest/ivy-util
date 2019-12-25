package org.ivy.xutil.lang;

/**
 * <p>LangUtil</p>
 *
 * @author miao.xl
 * @date 2016年8月10日-上午8:44:04
 * @version 1.0
 */
public class LangUtil {

    public static boolean isPrimitiveClass(Class<?> type) {
        if (type == null) return false;
        return type.isPrimitive();
    }

    public static boolean isLiteralClass(Class<?> type) {
        if (type == null) return false;
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Long.class
                || type == Double.class
                || type == Float.class
                || type == Short.class
                || type == Character.class
                || type == Byte.class;
    }

    public static void main(String[] args) {
        System.out.println(isPrimitiveClass(Integer.class));
    }

}
