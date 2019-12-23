/**
 * 
 */
package org.ivy.util.common;

import org.ivy.util.annotation.Description;

import static org.ivy.util.common.IvyConstant.*;
import java.security.SecureRandom;
import java.util.Random;

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
 * @className RandomSeqUtil
 * @author Ivybest (ivybestdev@163.com)
 * @date 2018/12/24 9:30
 * @version 1.0
 */
public class RandomSeqUtil {
	// 奇数数组
//	private static final int[] oddArray = {9, 11, 13, 15};

	@Description ("各静态数组长度，方便编程使用")
	public static final int digitLen = digitArr.length;
	public static final int letterLen = letterArr.length;
	public static final int hexLen = hexArr.length;
	public static final int specialCharLen = specialCharArr.length;

	@Description ("随机数生成对象")
	private static final Random random = new SecureRandom();
	
	/**
	 * 获取随机的阿拉伯数字
	 *
	 * @param len
	 * @return String
	 */
	public static String generateRandomArabic(int len) {
		StringBuffer randomBuffer = new StringBuffer();
		char[] sequence = new char[len];
		for (int i = 0; i < len; i++)
			sequence[i] = digitArr[random.nextInt(digitArr.length)];
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
		for (int i = 0; i < len; i++)
			sequence[i] = capitalLetterArr[random.nextInt(capitalLetterArr.length)];
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
		for (int i = 0; i < len; sequence[i++] = letterArr[random.nextInt(letterArr.length)]);
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
		@Description ("每次随机生成的一个Hex数组下标")
		int randomIdx;
		for (int i = 0; i < len; i++) {
			randomIdx = random.nextInt(hexArr.length);
			sequence[i] = hexArr[randomIdx];
		}
		return new String(sequence);
	}
	
	/**
	 * 获取随机字符串--含有特殊字符
	 *
	 * @param len len > 8
	 * @return String
	 */
	public static String generateRandomSequence(int len) {
		// 结果序列
		char[] sequence = new char[len];
		// 结果序列游标
		int cursor = -1;

		/* 特殊字符位数，[1, 3] */
		final int specialCharCount = random.nextInt(3) + 1;
		/* 英文字符位数，至少 4 位*/
		final int letterCount = random.nextInt(len - specialCharCount - 4) + 4;
		/* 阿拉伯数字位数，至少 1 位*/
		final int digitCount = len - letterCount - specialCharCount;

		/*构造随机英文字符，大小写随机
		* 构造随机数 0 和 1；0 随机构造一个小写字母；1 随机构造一个大写字母 */
		for (int i = 0; i < letterCount; i++) {
			if (random.nextInt(2) == 0) {
                sequence[++cursor] = letterArr[random.nextInt(letterLen)];
            } else {
                sequence[++cursor] = capitalLetterArr[random.nextInt(letterLen)];
            }
		}
		for (int i = 0; i < digitCount; i++) {
		    sequence[++cursor] = digitArr[random.nextInt(digitLen)];
        }
		for (int i = 0; i < specialCharCount; i++) {
		    sequence[++cursor] = specialCharArr[random.nextInt(specialCharLen)];
        }

//		System.out.println("====> origin: " + new String(sequence));

        /*
         * 对随机序列进行乱序处理
         * --增加碰撞难度
         * --首位不能是特殊字符，通过两次乱序来实现
         */
		Arrayx.derange(sequence, 0, len - 3);
		Arrayx.derange(sequence, 1, len - 1);

//		System.out.println("====> format: " + new String(sequence) + "\n");

        return new String(sequence);
	}
	
}











