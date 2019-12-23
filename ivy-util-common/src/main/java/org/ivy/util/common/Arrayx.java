package org.ivy.util.common;

import org.ivy.util.annotation.Description;
import org.ivy.util.annotation.Recommend;

import java.security.SecureRandom;
import java.util.Random;

/**
 * <p>  classname: Arrayx
 * <br> description: 数组工具类
 * <br>---------------------------------------------------------
 * <br> outline：
 * <br> 1、derange   数组乱序
 * <br> 2、reverse   数组倒序
 * <br> 3、shuffle   数组洗牌
 * <br> 4、swap      数组元素交换
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/16 11:06
 */
public class Arrayx {

    @Description("安全的随机数生成对象")
    private static final Random SECURE_RANDOM = new SecureRandom();

    /**
     * 数组乱序算法
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    @Description("数组乱序算法")
    public static void derange(byte[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(data, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(short[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(data, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(int[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(data, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(long[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(data, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(char[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2 + 1;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(data, x, y);
        }
    }

    public static void derange(char[] data) {
        derange(data, 0, data.length - 1);
    }

    /**
     * 数组乱序算法
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(float[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(data, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void derange(double[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;
        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(data, x, y);
        }
    }

    /**
     * 数组乱序算法
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static <T> void derange(T[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int count = len / 2;
        int x, y;
        for (int i = 0; i < count; i++) {
            x = SECURE_RANDOM.nextInt(len) + beginIdx;
            y = SECURE_RANDOM.nextInt(len) + beginIdx;
            swap(data, x, y);
        }
    }

    /**
     * 打印数组
     *
     * @param data 数组
     * @return String
     */
    @Recommend(value = false, msg = "测试用，不推荐用于生产环境")
    public static String printArray(byte[] data, int beginIdx, int endIdx) {
        if (null == data) return null;
        if (beginIdx > data.length - 1) return null;
        if (endIdx > data.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (data.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(data[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }
    }

    public static String printArray(byte[] data) {
        return printArray(data, 0, data.length - 1);
    }

    /**
     * 打印数组
     *
     * @param data 数组
     * @return String
     */
    public static String printArray(short[] data, int beginIdx, int endIdx) {
        if (null == data) return null;
        if (beginIdx > data.length - 1) return null;
        if (endIdx > data.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (data.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(data[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(short[] data) {
        return printArray(data, 0, data.length - 1);
    }

    /**
     * 打印数组
     *
     * @param data 数组
     * @return String
     */
    public static String printArray(int[] data, int beginIdx, int endIdx) {
        if (null == data) return null;
        if (beginIdx > data.length - 1) return null;
        if (endIdx > data.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (data.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(data[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(int[] data) {
        return printArray(data, 0, data.length - 1);
    }

    /**
     * 打印数组
     *
     * @param data 数组
     * @return String
     */
    public static String printArray(long[] data, int beginIdx, int endIdx) {
        if (null == data) return null;
        if (beginIdx > data.length - 1) return null;
        if (endIdx > data.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (data.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(data[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(long[] data) {
        return printArray(data, 0, data.length - 1);
    }

    /**
     * 打印数组
     *
     * @param data 数组
     * @return String
     */
    public static String printArray(float[] data, int beginIdx, int endIdx) {
        if (null == data) return null;
        if (beginIdx > data.length - 1) return null;
        if (endIdx > data.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (data.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(data[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(float[] data) {
        return printArray(data, 0, data.length - 1);
    }

    /**
     * 打印数组
     *
     * @param data 数组
     * @return String
     */
    public static String printArray(double[] data, int beginIdx, int endIdx) {
        if (null == data) return null;
        if (beginIdx > data.length - 1) return null;
        if (endIdx > data.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (data.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(data[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }
    }

    public static String printArray(double[] data) {
        return printArray(data, 0, data.length - 1);
    }

    /**
     * 打印数组
     *
     * @param data 数组
     * @return String
     */
    public static String printArray(char[] data, int beginIdx, int endIdx) {
        if (null == data) return null;
        if (beginIdx > data.length - 1) return null;
        if (endIdx > data.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (data.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(data[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static String printArray(char[] data) {
        return printArray(data, 0, data.length - 1);
    }

    /**
     * 打印数组
     *
     * @param data 数组
     * @return String
     */
    public static <T> String printArray(T[] data, int beginIdx, int endIdx) {
        if (null == data) return null;
        if (beginIdx > data.length - 1) return null;
        if (endIdx > data.length - 1) return null;
        if (endIdx < beginIdx) return null;
        if (data.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int cursor = beginIdx; ; ) {
            sb.append(data[cursor]);
            if (cursor++ == endIdx) return sb.append("]").toString();
            sb.append(", ");
        }

    }

    public static <T> String printArray(T[] data) {
        return printArray(data, 0, data.length - 1);
    }

    /**
     * reverse char array
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(byte[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(data, l, r);
        }
    }

    public static void reverse(byte[] data) {
        reverse(data, 0, data.length - 1);
    }

    /**
     * reverse char array
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(short[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(data, l, r);
        }
    }

    public static void reverse(short[] data) {
        reverse(data, 0, data.length - 1);
    }

    /**
     * reverse char array
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(int[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(data, l, r);
        }
    }

    public static void reverse(int[] data) {
        reverse(data, 0, data.length - 1);
    }

    /**
     * reverse char array
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(long[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(data, l, r);
        }
    }

    public static void reverse(long[] data) {
        reverse(data, 0, data.length - 1);
    }

    /**
     * reverse char array
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(double[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(data, l, r);
        }
    }

    public static void reverse(double[] data) {
        reverse(data, 0, data.length - 1);
    }

    /**
     * reverse char array
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(float[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(data, l, r);
        }
    }

    public static void reverse(float[] data) {
        reverse(data, 0, data.length - 1);
    }

    /**
     * reverse char array
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void reverse(char[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(data, l, r);
        }
    }

    public static void reverse(char[] data) {
        reverse(data, 0, data.length - 1);
    }

    public static <T> void reverse(T[] data, int beginIdx, int endIdx) {
        int len = endIdx - beginIdx + 1;

        int l, r;
        for (int i = beginIdx; i < endIdx; i++) {
            l = i;
            r = len - 1 - i;

            if (l >= r) break;
            swap(data, l, r);
        }
    }

    public static <T> void reverse(T[] data) {
        reverse(data, 0, data.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>-------------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（derange）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 derange 和 reverse 运算
     * <br>-------------------------------------------------
     * <p/>
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    @Description({"洗牌", "shuffle() = derange() & reverse()"})
    public static void shuffle(byte[] data, int beginIdx, int endIdx) {
        reverse(data, beginIdx, endIdx);
        derange(data, beginIdx, endIdx);
        reverse(data, beginIdx, endIdx);
    }

    public static void shuffle(byte[] data) {
        shuffle(data, 0, data.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>-------------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（derange）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 derange 和 reverse 运算
     * <br>-------------------------------------------------
     * <p/>
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    @Description({"洗牌", "shuffle() = derange() & reverse()"})
    public static void shuffle(short[] data, int beginIdx, int endIdx) {
        reverse(data, beginIdx, endIdx);
        derange(data, beginIdx, endIdx);
        reverse(data, beginIdx, endIdx);
    }

    public static void shuffle(short[] data) {
        shuffle(data, 0, data.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(int[] data, int beginIdx, int endIdx) {
        reverse(data, beginIdx, endIdx);
        derange(data, beginIdx, endIdx);
        reverse(data, beginIdx, endIdx);
    }

    public static void shuffle(int[] data) {
        shuffle(data, 0, data.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(long[] data, int beginIdx, int endIdx) {
        reverse(data, beginIdx, endIdx);
        derange(data, beginIdx, endIdx);
        reverse(data, beginIdx, endIdx);
    }

    public static void shuffle(long[] data) {
        shuffle(data, 0, data.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(double[] data, int beginIdx, int endIdx) {
        reverse(data, beginIdx, endIdx);
        derange(data, beginIdx, endIdx);
        reverse(data, beginIdx, endIdx);
    }

    public static void shuffle(double[] data) {
        shuffle(data, 0, data.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(float[] data, int beginIdx, int endIdx) {
        reverse(data, beginIdx, endIdx);
        derange(data, beginIdx, endIdx);
        reverse(data, beginIdx, endIdx);
    }

    public static void shuffle(float[] data) {
        shuffle(data, 0, data.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static void shuffle(char[] data, int beginIdx, int endIdx) {
        reverse(data, beginIdx, endIdx);
        derange(data, beginIdx, endIdx);
        reverse(data, beginIdx, endIdx);
    }

    public static void shuffle(char[] data) {
        shuffle(data, 0, data.length - 1);
    }

    /**
     * <p> descripiton: 洗牌
     * <br>---------------------------------------------
     * <br> 洗牌（shuffle）算法比混序（mix）算法更复杂
     * <br> 一次 shuffle 运算，经过多次 mix 和 reverse 运算
     * <br>---------------------------------------------
     * <p/>
     *
     * @param data     数组
     * @param beginIdx 开始坐标
     * @param endIdx   结束坐标
     */
    public static <T> void shuffle(T[] data, int beginIdx, int endIdx) {
        reverse(data, beginIdx, endIdx);
        derange(data, beginIdx, endIdx);
        reverse(data, beginIdx, endIdx);
    }

    public static <T> void shuffle(T[] data) {
        shuffle(data, 0, data.length - 1);
    }

    /**
     * 数组元素交换
     *
     * @param data 数组
     * @param x    data 下标 x
     * @param y    data 下标 y
     */
    public static void swap(byte[] data, int x, int y) {
        if (data[x] == data[y]) return;
        data[x] = (byte) (data[x] ^ data[y]);
        data[y] = (byte) (data[x] ^ data[y]);
        data[x] = (byte) (data[x] ^ data[y]);
    }

    /**
     * 数组元素交换
     *
     * @param data 数组
     * @param x    data 下标 x
     * @param y    data 下标 y
     */
    public static void swap(short[] data, int x, int y) {
        if (data[x] == data[y]) return;
        data[x] = (short) (data[x] ^ data[y]);
        data[y] = (short) (data[x] ^ data[y]);
        data[x] = (short) (data[x] ^ data[y]);
    }

    /**
     * 数组元素交换
     *
     * @param data 数组
     * @param x    data 下标 x
     * @param y    data 下标 y
     */
    @Description({
            "使用 ^（异或） 进行数据交换",
            "#:不能自己和自己进行交换，注意入口校验"})
    public static void swap(int[] data, int x, int y) {
        if (data[x] == data[y]) return;
        data[x] = data[x] ^ data[y];
        data[y] = data[x] ^ data[y];
        data[x] = data[x] ^ data[y];
    }

    /**
     * 数组元素交换
     *
     * @param data 数组
     * @param x    data 下标 x
     * @param y    data 下标 y
     */
    public static void swap(long[] data, int x, int y) {
        if (data[x] == data[y]) return;
        data[x] = data[x] ^ data[y];
        data[y] = data[x] ^ data[y];
        data[x] = data[x] ^ data[y];
    }

    /**
     * 数组元素交换
     *
     * @param data 数组
     * @param x    data 下标 x
     * @param y    data 下标 y
     */
    public static void swap(double[] data, int x, int y) {
        if (data[x] == data[y]) return;
        double temp = data[x];
        data[x] = data[y];
        data[y] = temp;
    }

    /**
     * 数组元素交换
     *
     * @param data 数组
     * @param x    data 下标 x
     * @param y    data 下标 y
     */
    public static void swap(float[] data, int x, int y) {
        if (data[x] == data[y]) return;
        float temp = data[x];
        data[x] = data[y];
        data[y] = temp;
    }

    /**
     * 数组元素交换
     *
     * @param data 数组
     * @param x    data 下标 x
     * @param y    data 下标 y
     */
    public static void swap(char[] data, int x, int y) {
        if (data[x] == data[y]) return;
        data[x] = (char) ((int) data[x] ^ (int) data[y]);
        data[y] = (char) ((int) data[x] ^ (int) data[y]);
        data[x] = (char) ((int) data[x] ^ (int) data[y]);
    }

    /**
     * 数组元素交换
     *
     * @param data 数组
     * @param x    data 下标 x
     * @param y    data 下标 y
     */
    public static <T> void swap(T[] data, int x, int y) {
        if (data[x] == data[y]) return;
        T temp = data[x];
        data[x] = data[y];
        data[y] = temp;
    }
}







