package org.ivy;

import java.util.Arrays;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/4/28 16:51
 */
public class DigitalUtil {

    private static final char BASE_CHAR = '0';
    private static final int DEFAULT_LENGTH = 4;


    public static String getFormatNo(int arg) {
        String origin = String.valueOf(arg);
        while (origin.length() < DEFAULT_LENGTH) {
            origin = BASE_CHAR + origin;
        }
        return origin;
    }

    public static String getFormatNo(int data, int len, char mask) {
        String origin = String.valueOf(data);
        while (origin.length() < len) {
            origin = mask + origin;
        }
        return origin;
    }

    public static String getFormatNo2(int data, int len, char mask) {
        char[] origin = String.valueOf(data).toCharArray();
        char[] target = new char[len];
        int oLen = origin.length;
        int pos = len - oLen;
        System.arraycopy(origin, 0, target, pos, oLen);

        for (int i = 0; i < pos; i++) {
            target[i] = mask;
        }
        return new String (target);
    }

    public static String getFormatNo3(int data, int len, char mask) {
        char[] origin = String.valueOf(data).toCharArray();
        char[] target = new char[len];
        int oLen = origin.length;
        int pos = len - oLen;
        System.arraycopy(origin, 0, target, pos, oLen);

        int idx = 0;
        while (idx < pos) {
            target[idx ++] = mask;
        }
        return new String (target);
    }

    public static String getFormatNo4(int data, int len, char mask) {
        char[] origin = String.valueOf(data).toCharArray();
        char[] target = new char[len];
        int oLen = origin.length;
        int pos = len - oLen;
        System.arraycopy(origin, 0, target, pos, oLen);
        Arrays.fill(target, 0, pos, mask);
        return new String (target);
    }

    public static String getFormatNo5(int data, int len, char mask) {
        char[] origin = Integer.toString(data).toCharArray();
        char[] target = new char[len];
        int oLen = origin.length;
        int pos = len - oLen;
        System.arraycopy(origin, 0, target, pos, oLen);
        Arrays.fill(target, 0, pos, mask);
        return new String (target);
    }

}
