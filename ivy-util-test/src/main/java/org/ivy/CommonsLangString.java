/**
 * @Package edu.hit.dev.commons.lang
 * @author miao.xl
 * @date 2016年3月21日-下午4:06:41
 */
package org.ivy;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>CommonsLangString</p>
 * <p>TODO</p>
 *
 * @author miao.xl
 * @version 1.0
 * @date 2016年3月21日-下午4:06:41
 */
public class CommonsLangString {

    String str_null;
    String str_empty;
    String str_upcase;
    String str_lowcase;
    String str_common;
    String str_num;
    String str_lang;

    @Before
    public void setup() {
        this.str_empty = "";
        this.str_upcase = "ABCDEFG";
        this.str_lowcase = "abcdefg";
        this.str_common = "Rashford";
        this.str_num = "39";
        this.str_lang = "123456789abcdefg*()&^%$#@";
    }

    @Test
    public void testCommonOpt() {
        System.out.println("==== org.apache.commons.lang.StringUtils test... "
                + "\n isBlank -> " + StringUtils.isBlank(this.str_null)
                + "\n isNotBlank -> " + StringUtils.isNotBlank(this.str_empty)
                + "\n isWhitespace -> " + StringUtils.isWhitespace(this.str_empty)
                + "\n isWhitespace -> " + StringUtils.isWhitespace("	")
                + "\n isAllUpperCase -> " + StringUtils.isAllUpperCase(this.str_common)
                + "\n isAllLowerCase -> " + StringUtils.isAllLowerCase(this.str_common)
                + "\n isAlpha -> " + StringUtils.isAlpha(this.str_common)
                + "\n isNumeric -> " + StringUtils.isNumeric(this.str_num)
                // 首字母大写
                + "\n capitalize -> " + StringUtils.capitalize(this.str_lowcase)
                // 字符串缩写
                + "\n abbreviate -> " + StringUtils.abbreviate("Technology", 6)
                // 左侧空格填充
                + "\n leftPad -> " + StringUtils.leftPad(this.str_num, 10)
                // 左侧字符串填充
                + "\n leftPad -> " + StringUtils.leftPad(this.str_num, 10, "abc")
                // 右侧空格填充
                + "\n rightPad -> " + StringUtils.rightPad(this.str_num, 10)
                // 左右填充，字符串居中
                + "\n center -> " + StringUtils.center(this.str_num, 10, "*")
                // 生成一个重复的字符串
                + "\n repeat -> " + StringUtils.repeat(this.str_common + " ", 5)
                // 字符串倒序
                + "\n reverse -> " + StringUtils.reverse(this.str_lowcase)
        );
    }

}
