package org.ivy.util.common;

import org.ivy.util.annotation.Description;
import org.ivy.util.annotation.Recommend;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.Random;

/**
 * <p> description: array util
 * <br>---------------------------------------------------------
 * <br> outline：
 * <br> 1、derange   数组乱序
 * <br> 2、reverse   数组倒序
 * <br> 3、shuffle   数组洗牌
 * <br> 4、subarray  数组截取
 * <br> 5、sort      array sort
 * <br> 6、swap      swap array elements
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/16 11:06
 */
public class Arrayx {

    @Description("// secure random number instance")
    private static final Random RANDOM = new SecureRandom();

    /**
     * private constructor for util class
     */
    private Arrayx() {
    }

    /**
     * Array derange(disorder) algorithm
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    @Description("// Array derange(disorder) algorithm")
    public static void derange(byte[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = RANDOM.nextInt(len) + beginIdx;
            y = RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * Array derange(disorder) algorithm
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void derange(short[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = RANDOM.nextInt(len) + beginIdx;
            y = RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * Array derange(disorder) algorithm
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void derange(int[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = RANDOM.nextInt(len) + beginIdx;
            y = RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * Array derange(disorder) algorithm
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void derange(long[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = RANDOM.nextInt(len) + beginIdx;
            y = RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * Array derange(disorder) algorithm
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void derange(char[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2 + 1;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = RANDOM.nextInt(len) + beginIdx;
            y = RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    public static void derange(char[] array) {
        derange(array, 0, array.length - 1);
    }

    /**
     * Array derange(disorder) algorithm
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void derange(float[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = RANDOM.nextInt(len) + beginIdx;
            y = RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * Array derange(disorder) algorithm
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void derange(double[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = RANDOM.nextInt(len) + beginIdx;
            y = RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * Array derange(disorder) algorithm
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     * @param <T>      array type
     */
    public static <T> void derange(T[] array, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = RANDOM.nextInt(len) + beginIdx;
            y = RANDOM.nextInt(len) + beginIdx;
            swap(array, x, y);
        }
    }

    /**
     * new object array
     *
     * @param type the type of array element
     * @param size the size of array
     * @param <T>  class type
     * @return T[]
     */
    public static <T> T[] newArray(Class<T> type, int size) {
        if (size <= 0) size = 0;
        return (T[]) Array.newInstance(type, size);
    }


    /**
     * print array, array to String
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     * @return String
     */
    @Recommend(value = false, msg = {
            "// for testing, do not recommended for production",
            "// recommended Arrays's api toString of JDK"})
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

    /**
     * print array, array to String
     *
     * @param array array
     * @return String
     */
    public static String printArray(byte[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * print array, array to String
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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

    /**
     * print array, array to String
     *
     * @param array array
     * @return String
     */
    public static String printArray(short[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * print array, array to String
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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

    /**
     * print array, array to String
     *
     * @param array array
     * @return String
     */
    public static String printArray(int[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * print array, array to String
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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

    /**
     * print array, array to String
     *
     * @param array array
     * @return String
     */
    public static String printArray(long[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * print array, array to String
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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

    /**
     * print array, array to String
     *
     * @param array array
     * @return String
     */
    public static String printArray(float[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * print array, array to String
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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
     * print array, array to String
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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

    /**
     * print array, array to String
     *
     * @param array array
     * @return String
     */
    public static String printArray(char[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * print array, array to String
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     * @param <T>      array type
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

    /**
     * print array, array to String
     *
     * @param array array
     * @param <T>   array type
     * @return String
     */
    public static <T> String printArray(T[] array) {
        return printArray(array, 0, array.length - 1);
    }

    /**
     * reverse array
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void reverse(byte[] array, int beginIdx, int endIdx) {
        if (null == array || array.length <= 1) return;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length) endIdx = array.length;
        if (endIdx - beginIdx <= 0) return;
        for (int l = beginIdx, r = endIdx; r > l; swap(array, l ++, r --));
    }

    public static void reverse(byte[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse array
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void reverse(short[] array, int beginIdx, int endIdx) {
        if (null == array || array.length <= 1) return;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length) endIdx = array.length;
        if (endIdx - beginIdx <= 0) return;
        for (int l = beginIdx, r = endIdx; r > l; swap(array, l ++, r --));
    }

    public static void reverse(short[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse array
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void reverse(int[] array, int beginIdx, int endIdx) {
        if (null == array || array.length <= 1) return;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length) endIdx = array.length;
        if (endIdx - beginIdx <= 0) return;
        for (int l = beginIdx, r = endIdx; r > l; swap(array, l ++, r --));
    }

    public static void reverse(int[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse array
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void reverse(long[] array, int beginIdx, int endIdx) {
        if (null == array || array.length <= 1) return;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length) endIdx = array.length;
        if (endIdx - beginIdx <= 0) return;
        for (int l = beginIdx, r = endIdx; r > l; swap(array, l ++, r --));
    }

    public static void reverse(long[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse array
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void reverse(double[] array, int beginIdx, int endIdx) {
        if (null == array || array.length <= 1) return;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length) endIdx = array.length;
        if (endIdx - beginIdx <= 0) return;
        for (int l = beginIdx, r = endIdx; r > l; swap(array, l ++, r --));
    }

    public static void reverse(double[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse array
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void reverse(float[] array, int beginIdx, int endIdx) {
        if (null == array || array.length <= 1) return;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length) endIdx = array.length;
        if (endIdx - beginIdx <= 0) return;
        for (int l = beginIdx, r = endIdx; r > l; swap(array, l ++, r --));
    }

    public static void reverse(float[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * reverse array
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    public static void reverse(char[] array, int beginIdx, int endIdx) {
        if (null == array || array.length <= 1) return;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length) endIdx = array.length;
        if (endIdx - beginIdx <= 0) return;
        for (int l = beginIdx, r = endIdx; r > l; swap(array, l ++, r --));
    }

    public static void reverse(char[] array) {
        reverse(array, 0, array.length - 1);
    }

    public static <T> void reverse(T[] array, int beginIdx, int endIdx) {
        if (null == array || array.length <= 1) return;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length) endIdx = array.length;
        if (endIdx - beginIdx <= 0) return;
        for (int l = beginIdx, r = endIdx; r > l; swap(array, l ++, r --));
    }

    public static <T> void reverse(T[] array) {
        reverse(array, 0, array.length - 1);
    }

    /**
     * <p> shuffle the array
     * <br>-------------------------------------------------
     * <br> Shuffling is more complicated than deranging
     * <br> A shuffle operation goes through multiple derange and reverse operations
     * <br>-------------------------------------------------
     * </p>
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    @Description({
            "// shuffle",
            "// shuffle() = derange() & reverse()"})
    public static void shuffle(byte[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(byte[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> shuffle the array
     * <br>-------------------------------------------------
     * <br> Shuffling is more complicated than deranging
     * <br> A shuffle operation goes through multiple derange and reverse operations
     * <br>-------------------------------------------------
     * </p>
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
     */
    @Description("// shuffle() = derange() and reverse()")
    public static void shuffle(short[] array, int beginIdx, int endIdx) {
        reverse(array, beginIdx, endIdx);
        derange(array, beginIdx, endIdx);
        reverse(array, beginIdx, endIdx);
    }

    public static void shuffle(short[] array) {
        shuffle(array, 0, array.length - 1);
    }

    /**
     * <p> shuffle the array
     * <br>-------------------------------------------------
     * <br> Shuffling is more complicated than deranging
     * <br> A shuffle operation goes through multiple derange and reverse operations
     * <br>-------------------------------------------------
     * </p>
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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
     * <p> shuffle the array
     * <br>-------------------------------------------------
     * <br> Shuffling is more complicated than deranging
     * <br> A shuffle operation goes through multiple derange and reverse operations
     * <br>-------------------------------------------------
     * </p>
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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
     * <p> shuffle the array
     * <br>-------------------------------------------------
     * <br> Shuffling is more complicated than deranging
     * <br> A shuffle operation goes through multiple derange and reverse operations
     * <br>-------------------------------------------------
     * </p>
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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
     * <p> shuffle the array
     * <br>-------------------------------------------------
     * <br> Shuffling is more complicated than deranging
     * <br> A shuffle operation goes through multiple derange and reverse operations
     * <br>-------------------------------------------------
     * </p>
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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
     * <p> shuffle the array
     * <br>-------------------------------------------------
     * <br> Shuffling is more complicated than deranging
     * <br> A shuffle operation goes through multiple derange and reverse operations
     * <br>-------------------------------------------------
     * </p>
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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
     * <p> shuffle the array
     * <br>-------------------------------------------------
     * <br> Shuffling is more complicated than deranging
     * <br> A shuffle operation goes through multiple derange and reverse operations
     * <br>-------------------------------------------------
     * </p>
     *
     * @param array    array
     * @param beginIdx array begin index
     * @param endIdx   array end index
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
     * @param <T>      array type
     * @return T[]
     */
    @Recommend
    @Description("// how to new a generic array instance")
    public static <T> T[] subarray(T[] array, int beginIdx, int endIdx) {
        if (array == null) return null;
        if (beginIdx < 0) beginIdx = 0;
        if (endIdx > array.length - 1) endIdx = array.length;
        if (beginIdx > endIdx) beginIdx = endIdx;

        int newSize = endIdx - beginIdx + 1;
        Class<T> type = (Class<T>) array[0].getClass();
        if (newSize <= 0) return newArray(type, 0);

        T[] subArr = newArray(type, newSize);
        System.arraycopy(array, beginIdx, subArr, 0, newSize);
        return subArr;
    }

    /**
     * swap array elements
     *
     * @param array array
     * @param x     array index x
     * @param y     array index y
     */
    public static void swap(byte[] array, int x, int y) {
        if (array[x] == array[y]) {
            return;
        }
        array[x] = (byte) (array[x] ^ array[y]);
        array[y] = (byte) (array[x] ^ array[y]);
        array[x] = (byte) (array[x] ^ array[y]);
    }

    /**
     * swap array elements
     *
     * @param array array
     * @param x     array index x
     * @param y     array index y
     */
    public static void swap(short[] array, int x, int y) {
        if (array[x] == array[y]) {
            return;
        }
        array[x] = (short) (array[x] ^ array[y]);
        array[y] = (short) (array[x] ^ array[y]);
        array[x] = (short) (array[x] ^ array[y]);
    }

    /**
     * swap array elements
     *
     * @param array array
     * @param x     array index x
     * @param y     array index y
     */
    @Description({
            "// swap elements using exclusive or algorithm(^)",
            "// the index of the two elements cannot be the same",
            "// perform safety check at the method entrance"
    })
    public static void swap(int[] array, int x, int y) {
        if (array[x] == array[y]) {
            return;
        }
        array[x] = array[x] ^ array[y];
        array[y] = array[x] ^ array[y];
        array[x] = array[x] ^ array[y];
    }

    /**
     * swap array elements
     *
     * @param array array
     * @param x     array index x
     * @param y     array index y
     */
    public static void swap(long[] array, int x, int y) {
        if (array[x] == array[y]) {
            return;
        }
        array[x] = array[x] ^ array[y];
        array[y] = array[x] ^ array[y];
        array[x] = array[x] ^ array[y];
    }

    /**
     * swap array elements
     *
     * @param array array
     * @param x     array index x
     * @param y     array index y
     */
    public static void swap(double[] array, int x, int y) {
        if (array[x] == array[y]) {
            return;
        }
        double temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }

    /**
     * swap array elements
     *
     * @param array array
     * @param x     array index x
     * @param y     array index y
     */
    public static void swap(float[] array, int x, int y) {
        if (array[x] == array[y]) {
            return;
        }
        float temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }

    /**
     * swap array elements
     *
     * @param array array
     * @param x     array index x
     * @param y     array index y
     */
    public static void swap(char[] array, int x, int y) {
        if (array[x] == array[y]) {
            return;
        }
        array[x] = (char) ((int) array[x] ^ (int) array[y]);
        array[y] = (char) ((int) array[x] ^ (int) array[y]);
        array[x] = (char) ((int) array[x] ^ (int) array[y]);
    }

    /**
     * swap array elements
     *
     * @param array array
     * @param x     array index x
     * @param y     array index y
     * @param <T>   array type
     */
    public static <T> void swap(T[] array, int x, int y) {
        if (array[x] == array[y]) {
            return;
        }
        T temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }
}







