package org.ivy.util.common;

import java.util.ArrayList;
import java.util.Collections;

public class Long2ChineseDigitTest {

    /**
     * 单位进位，中文默认为4位即（万、亿）
     */
    private static final int UNIT_STEP = 4;

    /**
     * 单位
     */
    private static final char[] CN_UNITS = {'个', '十', '百', '千', '万', '十', '百', '千', '亿', '十', '百', '千', '万'};

    /**
     * 汉字
     */
    private static final char[] CN_CHARS = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

    /**
     * 将长整型数字转换为中文数字
     *
     * @param data         需要转换的数值
     * @param isColloquial 是否口语化。例如12转换为'十二'而不是'一十二'。
     * @return
     */
    public static String long2ChineseDigit(long data, boolean isColloquial) {
        if (data < 0) return "负" + long2ChineseDigit(-data, isColloquial);

        if (data < 10) {// 10以下直接返回对应汉字
            return String.valueOf(CN_CHARS[(int) data]);    // ASCII2int
        }

        char[] chars = String.valueOf(data).toCharArray();
        if (chars.length > CN_UNITS.length) {   // 超过单位表示范围的返回空
            return " ";
        }

        boolean isLastUnitStep = false;// 记录上次单位进位  
        ArrayList<Character> cnchars = new ArrayList<Character>(chars.length * 2);// 创建数组，将数字填入单位对应的位置
        for (int pos = chars.length - 1; pos >= 0; pos--) {// 从低位向高位循环  
            char ch = chars[pos];
            char cnChar = CN_CHARS[ch - '0'];// ascii2int 汉字
            int unitPos = chars.length - pos - 1;// 对应的单位坐标  
            char cnUnit = CN_UNITS[unitPos];// 单位
            boolean isZero = (ch == '0');// 是否为0  
            boolean isZeroLow = (pos + 1 < chars.length && chars[pos + 1] == '0');// 是否低位为0  

            boolean isUnitStep = (unitPos >= UNIT_STEP && (unitPos % UNIT_STEP == 0));// 当前位是否需要单位进位  

            if (isUnitStep && isLastUnitStep) {// 去除相邻的上一个单位进位  
                int size = cnchars.size();
                cnchars.remove(size - 1);
                if (!(CN_CHARS[0] == cnchars.get(size - 2))) {// 补0
                    cnchars.add(CN_CHARS[0]);
                }
            }

            if (isUnitStep || !isZero) {// 单位进位(万、亿)，或者非0时加上单位  
                cnchars.add(cnUnit);
                isLastUnitStep = isUnitStep;
            }
            if (isZero && (isZeroLow || isUnitStep)) {// 当前位为0低位为0，或者当前位为0并且为单位进位时进行省略  
                continue;
            }
            cnchars.add(cnChar);
            isLastUnitStep = false;
        }

        Collections.reverse(cnchars);
        // 清除最后一位的0  
        int chSize = cnchars.size();
        char chEnd = cnchars.get(chSize - 1);
        if (CN_CHARS[0] == chEnd || CN_UNITS[0] == chEnd) {
            cnchars.remove(chSize - 1);
        }

        // 口语化处理  
        if (isColloquial) {
            char chFirst = cnchars.get(0);
            char chSecond = cnchars.get(1);
            if (chFirst == CN_CHARS[1] && chSecond == CN_UNITS[1]) {// 是否以'一'开头，紧跟'十'
                cnchars.remove(0);
            }
        }

        char[] tempArr = new char[cnchars.size()];
        for (int i = 0, size = cnchars.size(); i < size; i++) {
            tempArr[i] = cnchars.get(i);
        }
        return new String(tempArr);
    }

}