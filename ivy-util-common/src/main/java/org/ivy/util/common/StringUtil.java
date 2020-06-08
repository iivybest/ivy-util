package org.ivy.util.common;


import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * <br>---------------------------------------------------------
 * <br> descrption: String 工具类
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2014/6/13 19:10
 */
public class StringUtil {
    /**
     * 默认断句分割符
     */
    private static final String DEFAULT_SPLIT_MARK = "\r\n";

    private static final String DEFAULT_SPLIT_CHARSET = "GBK";

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


    public static Object[] breakLineSequenceByByteUseLoop(String data, int dataByteLen, int lineByteLen, String mark, String charset) throws UnsupportedEncodingException {
        if (isBlank(data)) {
            return null;
        }
        if (lineByteLen >= dataByteLen) {
            Object[] result = {1, data};
            return result;
        }
        StringBuilder builder = new StringBuilder(data);
        // -----断句下标结果数组
        int[] tIdxArr = new int[dataByteLen / lineByteLen + 1];
        int cLine = 0, cLineCursor = 0;
        int tIdx = -1;
        int dataEndIdx = data.length() - 1;
        int offset;
        while (tIdx < dataEndIdx) {
            offset = tIdx + 1;
            tIdx = getCurrentLineEndIndexUseLoop(data, lineByteLen, charset, dataEndIdx, offset);
            tIdxArr[(cLine = cLineCursor++)] = tIdx;
        }
        int cIdx;
        for (int i = cLine; i >= 0; i--) {
            cIdx = tIdxArr[i];
            if (cIdx == dataEndIdx) {
                continue;
            }
            builder.insert(cIdx + 1, mark);
        }

        Object[] result = {cLine + 1, builder.toString()};
        return result;
    }


    /**
     * 找到当前行的结束坐标
     *
     * @param data
     * @param lineByteLen
     * @param charset
     * @param dataEndIdx
     * @param offset
     * @return
     * @throws UnsupportedEncodingException
     */
    private static int getCurrentLineEndIndexUseLoop(String data, int lineByteLen, String charset, int dataEndIdx, int offset) throws UnsupportedEncodingException {
        // ----处理游标----游标起始位置为从包含 offset 起向右移 step 位的下标。
        int cursor = offset;
        // ----判断游标是否越界，若越界直接返回最右下标
        if (cursor >= dataEndIdx) {
            return dataEndIdx;
        }
        // -----当前行真实的字节数 currentLineByteLength，初始值为粗略截取序列的字节长度
        int cLineByteLen = 0;

        // ========精细化匹配过程
        // ----目标游标值
        int tIdx;
        // ----是否还需要继续寻找
        boolean seekAble = true;
        // ----精准截取步长，用于对粗略截取部分的精度补偿运算
        int scaleStep = 1;
        String scaleSplit;
        int scaleLen;
        do {
            scaleSplit = data.substring(cursor, cursor + scaleStep);
            scaleLen = scaleSplit.getBytes(charset).length;
            cLineByteLen += scaleLen;
            if (cLineByteLen == lineByteLen) {
                seekAble = false;
                break;
            } else if (cLineByteLen > lineByteLen) {
                seekAble = false;
                cursor -= scaleStep;
                break;
            }
            if (cursor == dataEndIdx) {
                seekAble = false;
                break;
            }
            cursor += scaleStep;
        } while (seekAble);
        tIdx = cursor;
        return tIdx;
    }

    /**
     * 找到当前行的结束坐标
     *
     * @param data
     * @param lineByteLen
     * @param charset
     * @param dataEndIdx
     * @param offset
     * @param step
     * @return
     * @throws UnsupportedEncodingException
     */
    private static int getCurrentLineEndIndex(String data, int lineByteLen, String charset, int dataEndIdx, int offset, int step) throws UnsupportedEncodingException {
        // ----处理游标----游标起始位置为从包含 offset 起向右移 step 位的下标。
        int cursor = offset + step - 1;
        // ----判断游标是否越界，若越界直接返回最右下标
        if (cursor >= dataEndIdx) {
            return dataEndIdx;
        }
        // -----粗略截取 offset 至 cursor 的字符序列
        String routghCutSeq = data.substring(offset, cursor + 1);
        // -----当前行真实的字节数 currentLineByteLength，初始值为粗略截取序列的字节长度
        int cLineByteLen = routghCutSeq.getBytes(charset).length;
        // -----若粗略截取长度，符合要求，直接返回该游标值，不再进行精细化匹配
        if (cLineByteLen == lineByteLen) {
            return cursor;
        }

        // ========精细化匹配过程
        // ----目标游标值
        int tIdx;
        // ----是否还需要继续寻找
        boolean seekAble = true;
        // ----精准截取步长，用于对粗略截取部分的精度补偿运算
        int scaleStep = 1;
        String scaleSplit;
        int scaleLen;
        do {
            cursor += scaleStep;
            scaleSplit = data.substring(cursor, cursor + scaleStep);
            scaleLen = scaleSplit.getBytes(charset).length;
            cLineByteLen += scaleLen;

            // ----精准适配结束条件
            if (cLineByteLen >= lineByteLen || cursor == dataEndIdx) {
                seekAble = false;
                if (cLineByteLen > lineByteLen) {
                    cursor -= scaleStep;
                }
            }
        } while (seekAble);
        tIdx = cursor;
        return tIdx;
    }

    /**
     * 按字节长度进行断句
     *
     * @param data
     * @param dataByteLen
     * @param lineByteLen
     * @param mark
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Object[] breakLineSequenceByByte(String data, int dataByteLen, int lineByteLen, String mark, String charset) throws UnsupportedEncodingException {
        if (isBlank(data)) {
            return null;
        }
        if (lineByteLen >= dataByteLen) {
            Object[] result = {1, data};
            return result;
        }
        StringBuilder builder = new StringBuilder(data);
        // -----断句下标结果数组
        int[] tIdxArr = new int[dataByteLen / lineByteLen + 1];
        // ----字符序列最右下标
        int dataEndIdx = data.length() - 1;
        // ----当前行标记----行标从 0 开始
        int cLine = 0, cLineCursor = 0;
        // ----粗略位移步长----步长单位为字符长度1
        int step = lineByteLen / 2;
        // ----当前行结束下标，在该坐标后插入 mark 即可完成指定长度的标记工作
        int tIdx = -1;
        // -----当前行起始位置相对 sequence 的偏移量
        int offset;
        while (tIdx < dataEndIdx) {
            offset = tIdx + 1;
            tIdx = getCurrentLineEndIndex(data, lineByteLen, charset, dataEndIdx, offset, step);
            tIdxArr[(cLine = cLineCursor++)] = tIdx;
        }

        // ----数组排序
//        Arrays.sort(tIdxArr);
        // ----倒序处理
        int cIdx;
        for (int i = cLine; i >= 0; i--) {
            cIdx = tIdxArr[i];
            if (cIdx == dataEndIdx) {
                continue;
            }
            builder.insert(cIdx + 1, mark);
        }

        Object[] result = {cLine + 1, builder.toString()};
        return result;
    }

    public static Object[] breakLineSequenceByByte(String data, int lineByteLen, String mark, String charset) throws UnsupportedEncodingException {
        if (isBlank(data)) {
            return null;
        }
        return breakLineSequenceByByte(data, data.getBytes(charset).length, lineByteLen, mark, charset);
    }

    public static Object[] breakLineSequenceByByte(String data, int lineByteLen, String charset) throws UnsupportedEncodingException {
        if (isBlank(data)) {
            return null;
        }
        return breakLineSequenceByByte(data, data.getBytes(charset).length, lineByteLen, DEFAULT_SPLIT_MARK, charset);
    }

    public static Object[] breakLineSequenceByByte(String data, int lineByteLen) throws UnsupportedEncodingException {
        if (isBlank(data)) {
            return null;
        }
        return breakLineSequenceByByte(data, data.getBytes(DEFAULT_SPLIT_CHARSET).length, lineByteLen, DEFAULT_SPLIT_MARK, DEFAULT_SPLIT_CHARSET);
    }


    public static Object[] breakLineSequenceByChar(String data, int dataLen, int lineLen, String mark) {
        return null;
    }

    public static Object[] breakLineSequenceByChar(String data, int lineLen, String mark) {
        if (isBlank(data)) {
            return null;
        }
        return breakLineSequenceByChar(data, data.length(), lineLen, mark);
    }

    public static Object[] breakLineSequenceByChar(String data, int lineLen) {
        if (isBlank(data)) {
            return null;
        }
        return breakLineSequenceByChar(data, data.length(), lineLen, DEFAULT_SPLIT_MARK);
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
     * 获取 val 值，若 val 为空 返回 default val
     *
     * @param val        val
     * @param defaultVal defaultVal
     * @return val
     */
    public static String get(String val, String defaultVal) {
        if (isNonBlank(val)) {
            return val;
        }
        return defaultVal;
    }

    public static String getNonBlank(String val, String... vals) {
        if (isNonBlank(val)) {
            return val;
        } else {
            for (String e : vals) {
                if (isNonBlank(e)) {
                    return e;
                }
            }
        }
        return null;
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

    public static String getFixedByteLengthString(String data, int length, String pad, int pos, String charset) throws UnsupportedEncodingException {
        if (length < 0) {
            return null;
        }
        String result = null;
        int dLen = isBlank(data) ? 0 : data.getBytes(charset).length;
        int offset = length - dLen;
        if (offset == 0) {
            result = data;
        } else if (offset < 0) {
        } else {
        }
        return result;
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
