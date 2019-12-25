package org.ivy.xutil.sec;

import org.ivy.util.common.StringUtil;

/**
 * <p> description: Caesar Code Util
 * <br>---------------------------------------------------------
 * <br> Caesar Code：
 * <br> 1、加密向右移位 step_len
 * <br>     遍历 plain char array，对每个 char 进行加密；
 * <br>     #注：char 加密后值若大于等于 char max length，减去一个 65536
 * <br> 2、解密向左移位 step_len
 * <br>     遍历 ciper char array ，对每个 char 进行机密；
 * <br>     #注：char 解密后值若小于 0，加上一个  65536
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/25 8:46
 */
public class CaesarCodeUtil {

    private static  final int STEP_DEFAULT = 3;
    private static  final int CHAR_LENGTH = 65536;


    /**
     * encrypt
     *
     * @param data data
     * @param step shift step length
     * @return char[]
     */
    public static  char[] encrypt(char[] data, int step) {
        if (data == null || data.length == 0) {
            return new char[0];
        }
        int offset = (step <= 0) ? STEP_DEFAULT : step;
        char[] cipher = data;
        for (int i = 0, len = data.length; i < len; i++) {
            cipher[i] = cipherCode(data[i], offset);
        }
        return cipher;
    }

    /**
     * encrypt
     *
     * @param data data
     * @return char[]
     */
    public static  char[] encrypt(char[] data) {
        return encrypt(data, STEP_DEFAULT);
    }

    /**
     * encrypt
     *
     * @param data data
     * @param step shift step length
     * @return char[]
     */
    public static  char[] encrypt(String data, int step) {
        if (StringUtil.isBlank(data)) {
            return new char[0];
        }
//        char[] dataChar = new char[data.length()];
//        data.getChars(0, data.length(), dataChar, 0);
        return encrypt(data.toCharArray(), step);
    }

    /**
     * encrypt
     *
     * @param data data
     * @return char[]
     */
    public static  char[] encrypt(String data) {
        return encrypt(data, STEP_DEFAULT);
    }

    /**
     * encrypt to string
     *
     * @param data data
     * @param step shift step length
     * @return String
     */
    public static  String encryptToString(char[] data, int step) {
        char[] cipher = encrypt(data, step);
        return new String(cipher);
    }

    /**
     * encrypt to string
     *
     * @param data data
     * @return String
     */
    public static  String encryptToString(char[] data) {
        return encryptToString(data, STEP_DEFAULT);
    }

    /**
     * encrypt to string
     *
     * @param data data
     * @param step shift step length
     * @return String
     */
    public static  String encryptToString(String data, int step) {
        char[] cipher = encrypt(data, step);
        return new String(cipher);
    }

    /**
     * encrypt to string
     *
     * @param data data
     * @return String
     */
    public static  String encryptToString(String data) {
        return encryptToString(data, STEP_DEFAULT);
    }

    /**
     * decrypt
     *
     * @param data data
     * @param step shift step length
     * @return char[]
     */
    public static  char[] decrypt(char[] data, int step) {
        if (data == null || data.length == 0) {
            return new char[0];
        }
        int offset = (step <= 0) ? STEP_DEFAULT : step;
        char[] plain = data;
        for (int i = 0, len = data.length; i < len; i++) {
            plain[i] = plainCode(data[i], offset);
        }
        return plain;
    }

    /**
     * decrypt
     *
     * @param data data
     * @return char[]
     */
    public static  char[] decrypt(char[] data) {
        return decrypt(data, STEP_DEFAULT);
    }

    /**
     * decrypt
     *
     * @param data data
     * @param step shift step length
     * @return char[]
     */
    public static  char[] decrypt(String data, int step) {
        if (StringUtil.isBlank(data)) {
            return new char[0];
        }
        int offset = (step <= 0) ? STEP_DEFAULT : step;
        return decrypt(data.toCharArray(), offset);
    }

    /**
     * decrypt
     *
     * @param data data
     * @return char[]
     */
    public static  char[] decrypt(String data) {
        return decrypt(data, STEP_DEFAULT);
    }

    /**
     * get data's caesar cipher code
     *
     * @param data data
     * @param step shift step len
     * @return char
     */
    private static  char cipherCode(char data, int step) {
        int temp = data + step;
        char ciper;
        if (temp >= CHAR_LENGTH) {
            temp -= CHAR_LENGTH;
        }
        ciper = (char) temp;
        return ciper;
    }

    /**
     * get data's plain code
     *
     * @param data data
     * @param step shift step len
     * @return char
     */
    private static  char plainCode(char data, int step) {
        int temp = data - step;
        char plain;
        if (temp < 0) {
            temp += CHAR_LENGTH;
        }
        plain = (char) temp;
        return plain;
    }

}
