/**
 * @Package edu.hit.utility.cfg
 * @author miao.xl
 * @date 2016年3月22日-上午9:05:29
 */
package org.ivy.util.common;

/**
 * <p> description: Ivy-Util 常量信息
 * <br>--------------------------------------------------------
 * <br> 使用频率较高的常量
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className IvyConstant
 * @date 2019/12/17 16:01
 */
public interface IvyConstant {

    /**
     * pattern: *.xml
     */
    String REGEXP_XML = "^[^\\.]+.*\\.[x|X][m|M][l|L]$";

    String UNIX_SEPARATOR = "/";
    String WIN_SEPARATOR = "\\";

    // 阿拉伯数字数组
    char[] DIGIT_ARR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    // 大写英文字母数组
    char[] CAPITAL_LETTER_ARR = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    // 小写英文字母数组
    char[] LETTER_ARR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    // 十六进制数字数组
    char[] HEX_ARR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    // 特殊字符数组
    char[] SPECIAL_CHAR_ARR = {'~', '!', '@', '#', '$', '%', '^', '&', '*', '_', '+', '=', '.', ','};


}
