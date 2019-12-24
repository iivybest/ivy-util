package org.ivy.util.common;

import org.ivy.util.annotation.Description;
import org.ivy.util.annotation.Recommend;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * <p>  classname: Arrayx
 * <br> description: 数组工具类
 * <br>---------------------------------------------------------
 * <br> outline：
 * <br> 1、derange   数组乱序
 * <br> 2、reverse   数组倒序
 * <br> 3、shuffle   数组洗牌
 * <br> 4、subarray  数组截取
 * <br> 5、swap      数组元素交换
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/16 11:06
 */
public class Arrayx {
    /**
     * 工具类，私有化其构造器
     */
    private Arrayx() {
    }


    @Description("安全的随机数生成对象")
    private static final Random SECURE_RANDOM = new SecureRandom();

    /**
     * 数组乱序算法
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    @Description("数组乱序算法")
    public static void derange(byte[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(short[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(int[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(long[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(char[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2 + 1;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    public static void derange(char[] array) {
        derange(array, 0, array.length - 1);
    }

    /**
     * 数组乱序算法
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(float[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(double[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static <T> void derange(T[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * 打印数组
     *
     * @param array 数组
     * @return String
     */
    @Recommend(value = false, msg = "测试用，不推荐用于生产环境")
    public static String printArray(byte[] array, int beginIdx, int endIdx) {
        if (null == array) return null;
        if (beginIdx > array.length - 1) return null;
        if (endIdx > array.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (array.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(array[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }
    }

    public static String printArray(byte[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * 打印数组
     *
     * @param array 数组
     * @return String
     */
    public static String printArray(short[] array, int beginIdx, int endIdx) {
        if (null == array) return null;
        if (beginIdx > array.length - 1) return null;
        if (endIdx > array.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (array.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(array[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(short[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * 打印数组
     *
     * @param array 数组
     * @return String
     */
    public static String printArray(int[] array, int beginIdx, int endIdx) {
        if (null == array) return null;
        if (beginIdx > array.length - 1) return null;
        if (endIdx > array.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (array.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(array[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(int[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * 打印数组
     *
     * @param array 数组
     * @return String
     */
    public static String printArray(long[] array, int beginIdx, int endIdx) {
        if (null == array) return null;
        if (beginIdx > array.length - 1) return null;
        if (endIdx > array.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (array.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(array[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(long[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * 打印数组
     *
     * @param array 数组
     * @return String
     */
    public static String printArray(float[] array, int beginIdx, int endIdx) {
        if (null == array) return null;
        if (beginIdx > array.length - 1) return null;
        if (endIdx > array.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (array.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(array[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(float[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * 打印数组
     *
     * @param array 数组
     * @return String
     */
    public static String printArray(double[] array, int beginIdx, int endIdx) {
        if (null == array) return null;
        if (beginIdx > array.length - 1) return null;
        if (endIdx > array.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (array.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(array[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }
    }

    public static String printArray(double[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * 打印数组
     *
     * @param array 数组
     * @return String
     */
    public static String printArray(char[] array, int beginIdx, int endIdx) {
        if (null == array) return null;
        if (beginIdx > array.length - 1) return null;
        if (endIdx > array.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (array.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(array[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(char[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * 打印数组
     *
     * @param array 数组
     * @return String
     */
    public static <T> String printArray(T[] array, int beginIdx, int endIdx) {
        if (null == array) return null;
        if (beginIdx > array.length - 1) return null;
        if (endIdx > array.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (array.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(array[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static <T> String printArray(T[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * reverse char array
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(byte[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(array, l, r);
        }
    }

    public static void reverse(byte[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse char array
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(short[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(array, l, r);
        }
    }

    public static void reverse(short[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse char array
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(int[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(array, l, r);
        }
    }

    public static void reverse(int[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse char array
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(long[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(array, l, r);
        }
    }

    public static void reverse(long[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse char array
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(double[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(array, l, r);
        }
    }

    public static void reverse(double[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse char array
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(float[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(array, l, r);
        }
    }

    public static void reverse(float[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse char array
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(char[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(array, l, r);
        }
    }

    public static void reverse(char[] array) {
        reverse(array, 0, array.length - 1);
    }

    public static <T> void reverse(T[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(array, l, r);
        }
    }

    public static <T> void reverse(T[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>-------------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（derange）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 derange 和 reverse 运算
     * <br>-------------------------------------------------
     * <p/>
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    @Description({"洗牌", "shuffle() = derange() & reverse()"})
    public static void shuffle(byte[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(byte[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>-------------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（derange）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 derange 和 reverse 运算
     * <br>-------------------------------------------------
     * <p/>
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    @Description({"洗牌", "shuffle() = derange() & reverse()"})
    public static void shuffle(short[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(short[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(int[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(int[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(long[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(long[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(double[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(double[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(float[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(float[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(char[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(char[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param array    数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static <T> void shuffle(T[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static <T> void shuffle(T[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * sub array
     *
     * @param array    array
     * @param beginIdx array being index
     * @param endIdx   array end index
     * @return byte[]
     */
    public static byte[] subarray(byte[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        if (newSize <= 0) return new byte[0];

        byte[] subarray = new byte[newSize];
        System.arraycopy(array, beginIdx, subarray, 0, newSize);
        return subarray;
    }
    /**
     * sub array
     *
     * @param array    array
     * @param beginIdx array being index
     * @param endIdx   array end index
     * @return short[]
     */
    public static short[] subarray(short[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        if (newSize <= 0) return new short[0];

        short[] subarray = new short[newSize];
        System.arraycopy(array, beginIdx, subarray, 0, newSize);
        return subarray;
    }
    /**
     * sub array
     *
     * @param array    array
     * @param beginIdx array being index
     * @param endIdx   array end index
     * @return int[]
     */
    public static int[] subarray(int[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        if (newSize <= 0) return new int[0];

        int[] subarray = new int[newSize];
        System.arraycopy(array, beginIdx, subarray, 0, newSize);
        return subarray;
    }
    /**
     * sub array
     *
     * @param array    array
     * @param beginIdx array being index
     * @param endIdx   array end index
     * @return long[]
     */
    public static long[] subarray(long[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        if (newSize <= 0) return new long[0];

        long[] subarray = new long[newSize];
        System.arraycopy(array, beginIdx, subarray, 0, newSize);
        return subarray;
    }
    /**
     * sub array
     *
     * @param array    array
     * @param beginIdx array being index
     * @param endIdx   array end index
     * @return double[]
     */
    public static double[] subarray(double[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        if (newSize <= 0) return new double[0];

        double[] subarray = new double[newSize];
        System.arraycopy(array, beginIdx, subarray, 0, newSize);
        return subarray;
    }
    /**
     * sub array
     *
     * @param array    array
     * @param beginIdx array being index
     * @param endIdx   array end index
     * @return float[]
     */
    public static float[] subarray(float[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        if (newSize <= 0) return new float[0];

        float[] subarray = new float[newSize];
        System.arraycopy(array, beginIdx, subarray, 0, newSize);
        return subarray;
    }
    /**
     * sub array
     *
     * @param array    array
     * @param beginIdx array being index
     * @param endIdx   array end index
     * @return char[]
     */
    public static char[] subarray(char[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        if (newSize <= 0) return new char[0];

        char[] subarray = new char[newSize];
        System.arraycopy(array, beginIdx, subarray, 0, newSize);
        return subarray;
    }
    /**
     * sub array
     *
     * @param array    array
     * @param beginIdx array being index
     * @param endIdx   array end index
     * @return T[]
     */
    @Description("how to new a generaic array instance")
    public static <T> T[] subarray(T[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        if (newSize <= 0) return (T[]) Array.newInstance(array[0].getClass(), 0);

        T[] subarray = (T[]) Array.newInstance(array[0].getClass(), newSize);;
        System.arraycopy(array, beginIdx, subarray, 0, newSize);
        return subarray;
    }

    /**
     * 数组元素交换
     *
     * @param array 数组
     * @param x     array 下标 x
     * @param y     array 下标 y
     */
    public static void swap(byte[] array, int x, int y) {
        if (array[x] == array[y]) return;
        array[x] = (byte) (array[x] ^ array[y]);
        array[y] = (byte) (array[x] ^ array[y]);
        array[x] = (byte) (array[x] ^ array[y]);
    }

    /**
     * 数组元素交换
     *
     * @param array 数组
     * @param x     array 下标 x
     * @param y     array 下标 y
     */
    public static void swap(short[] array, int x, int y) {
        if (array[x] == array[y]) return;
        array[x] = (short) (array[x] ^ array[y]);
        array[y] = (short) (array[x] ^ array[y]);
        array[x] = (short) (array[x] ^ array[y]);
    }

    /**
     * 数组元素交换
     *
     * @param array 数组
     * @param x     array 下标 x
     * @param y     array 下标 y
     */
    @Description({
            "使用 ^（异或） 进行数据交换",
            "#:不能自己和自己进行交换，注意入口校验"})
    public static void swap(int[] array, int x, int y) {
        if (array[x] == array[y]) return;
        array[x] = array[x] ^ array[y];
        array[y] = array[x] ^ array[y];
        array[x] = array[x] ^ array[y];
    }

    /**
     * 数组元素交换
     *
     * @param array 数组
     * @param x     array 下标 x
     * @param y     array 下标 y
     */
    public static void swap(long[] array, int x, int y) {
        if (array[x] == array[y]) return;
        array[x] = array[x] ^ array[y];
        array[y] = array[x] ^ array[y];
        array[x] = array[x] ^ array[y];
    }

    /**
     * 数组元素交换
     *
     * @param array 数组
     * @param x     array 下标 x
     * @param y     array 下标 y
     */
    public static void swap(double[] array, int x, int y) {
        if (array[x] == array[y]) return;
        double temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }

    /**
     * 数组元素交换
     *
     * @param array 数组
     * @param x     array 下标 x
     * @param y     array 下标 y
     */
    public static void swap(float[] array, int x, int y) {
        if (array[x] == array[y]) return;
        float temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }

    /**
     * 数组元素交换
     *
     * @param array 数组
     * @param x     array 下标 x
     * @param y     array 下标 y
     */
    public static void swap(char[] array, int x, int y) {
        if (array[x] == array[y]) return;
        array[x] = (char) ((int) array[x] ^ (int) array[y]);
        array[y] = (char) ((int) array[x] ^ (int) array[y]);
        array[x] = (char) ((int) array[x] ^ (int) array[y]);
    }

    /**
     * 数组元素交换
     *
     * @param array 数组
     * @param x     array 下标 x
     * @param y     array 下标 y
     */
    public static <T> void swap(T[] array, int x, int y) {
        if (array[x] == array[y]) return;
        T temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }
}







