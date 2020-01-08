package org.ivy.util.common;

import org.ivy.util.annotation.Description;

import java.security.SecureRandom;
import java.util.Random;

import static org.ivy.util.common.IvyConstant.*;

/**
 * <p> description: 随机序列工具
 * <br>--------------------------------------------------------
 * <br> 1、生成随机阿拉伯数字、字母、Hex序列
 * <br> 2、生成随机的阿拉伯数字、字母、特殊字符序列
 * <br>
 * <br>
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className RandomSeqUtil
 * @date 2018/12/24 9:30
 */
public class RandomSeqUtil {
    // 奇数数组
//	private static final int[] oddArray = {9, 11, 13, 15};

    @Description("// 各静态数组长度，方便编程使用")
    public static final int DIGIT_LEN = DIGIT_ARR.length;
    public static final int LETTER_LEN = LETTER_ARR.length;
    public static final int HEX_LEN = HEX_ARR.length;
    public static final int SPECIAL_CHAR_LEN = SPECIAL_CHAR_ARR.length;

    @Description("// sucure random instance")
    private static final Random RANDOM = new SecureRandom();

    /**
     * 获取随机的阿拉伯数字
     *
     * @param len
     * @return String
     */
    public static String generateRandomArabic(int len) {
        StringBuffer randomBuffer = new StringBuffer();
        char[] sequence = new char[len];
        for (int i = 0; i < len; i++) {
            sequence[i] = DIGIT_ARR[RANDOM.nextInt(DIGIT_LEN)];
        }
        return new String(sequence);
    }

    /**
     * 获取随机大写字母
     *
     * @param len
     * @return String
     */
    public static String generateRandomCapitalLetter(int len) {
        char[] sequence = new char[len];
        for (int i = 0; i < len; i++) {
            sequence[i] = CAPITAL_LETTER_ARR[RANDOM.nextInt(SPECIAL_CHAR_LEN)];
        }
        return new String(sequence);
    }

    /**
     * 获取随机小写字母
     *
     * @param len
     * @return String
     */
    public static String generateRandomLetter(int len) {
        char[] sequence = new char[len];
        for (int i = 0; i < len; sequence[i++] = LETTER_ARR[RANDOM.nextInt(LETTER_LEN)]) {
        }
        return new String(sequence);
    }

    /**
     * 获取随机十六进制字符串
     *
     * @param len
     * @return String
     */
    public static String generateRandomHex(int len) {
        char[] sequence = new char[len];
        @Description("// create one random Hex Array index once")
        int randomIdx;
        for (int i = 0; i < len; i++) {
            randomIdx = RANDOM.nextInt(HEX_LEN);
            sequence[i] = HEX_ARR[randomIdx];
        }
        return new String(sequence);
    }

    /**
     * 获取随机字符串--含有特殊字符
     *
     * @param len Length greater than 8
     * @return String
     */
    public static String generateRandomSequence(int len) {
        // 结果序列
        char[] sequence = new char[len];
        // 结果序列游标
        int cursor = -1;

        /* 特殊字符位数，[1, 3] */
        final int specialCharCount = RANDOM.nextInt(3) + 1;
        /* 英文字符位数，至少 4 位*/
        final int letterCount = RANDOM.nextInt(len - specialCharCount - 4) + 4;
        /* 阿拉伯数字位数，至少 1 位*/
        final int digitCount = len - letterCount - specialCharCount;

        /*构造随机英文字符，大小写随机
         * 构造随机数 0 和 1；0 随机构造一个小写字母；1 随机构造一个大写字母 */
        for (int i = 0; i < letterCount; i++) {
            if (RANDOM.nextInt(2) == 0) {
                sequence[++cursor] = LETTER_ARR[RANDOM.nextInt(LETTER_LEN)];
            } else {
                sequence[++cursor] = CAPITAL_LETTER_ARR[RANDOM.nextInt(LETTER_LEN)];
            }
        }
        for (int i = 0; i < digitCount; i++) {
            sequence[++cursor] = DIGIT_ARR[RANDOM.nextInt(DIGIT_LEN)];
        }
        for (int i = 0; i < specialCharCount; i++) {
            sequence[++cursor] = SPECIAL_CHAR_ARR[RANDOM.nextInt(SPECIAL_CHAR_LEN)];
        }

//		System.out.println("====> origin: " + new String(sequence));

        /*
         * 对随机序列进行乱序处理
         * ----增加碰撞难度
         * ----首位不能是特殊字符，通过两次乱序来实现
         */
        Arrayx.derange(sequence, 0, len - 3);
        Arrayx.derange(sequence, 1, len - 1);

//		System.out.println("====> format: " + new String(sequence) + "\n");

        return new String(sequence);
    }

}











