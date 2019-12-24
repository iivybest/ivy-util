package org.ivy.util.common;

public class AdvLong2ChineseDigitTest {

    /**
     * 单位
     */
    private static final char[] allCnUnits = {'个', '十', '百', '千', '万', '十', '百', '千', '亿', '十', '百', '千', '万'};
    /**
     * 中文数字
     */
    private static final char[] cnArr = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
    /**
     * 单位进位，中文默认为4位即（万、亿）
     */
    private static int UNIT_STEP = 4;

    /**
     * 将长整型数字转换为中文数字
     *
     * @param data         需要转换的数值
     * @param isColloquial 是否口语化。例如12转换为'十二'而不是'一十二'。
     * @return
     */
    public static String long2ChineseDigit(long data, boolean isColloquial) {
        /* ====>step-1: 处理负数*/
        if (data < 0) return "负" + long2ChineseDigit(-data, isColloquial);
        /* ====>step-2: 处理小于 10 的整数；ASCII 2 Integer Character*/
        if (data < 10) return String.valueOf(cnArr[(int) data]);
        /* ====>step-3: 处理超范围的数，超过单位表示范围的返回空 TODO*/
        char[] chars = String.valueOf(data).toCharArray();
        if (chars.length > allCnUnits.length) return "";
        /* ====>step-4: long处理过程*/
        // 记录上次单位进位
        boolean isLastUnitStep = false;
        // 创建数组，将数字填入单位对应的位置
        char[] cnChars = new char[chars.length * 2];
        // cnChars 中有效 char 区间，offset 起始位置，cursor 结束位置
        int offset = 0, cursor = -1;
        // 从低位向高位循环
        for (int pos = chars.length - 1; pos >= 0; pos--) {
            char ch = chars[pos];
            // ASCII 2 Integer Character
            char cnChar = cnArr[ch - '0'];
            // 对应的单位坐标
            int unitPos = chars.length - pos - 1;
            // 单位
            char cnUnit = allCnUnits[unitPos];
            // 是否为 0
            boolean isZero = (ch == '0');
            // 是否低位为 0
            boolean isZeroLow = (pos + 1 < chars.length && chars[pos + 1] == '0');
            // 当前位是否需要单位进位
            boolean isUnitStep = (unitPos >= UNIT_STEP && (unitPos % UNIT_STEP == 0));
            // 去除相邻的上一个单位进位
            if (isUnitStep && isLastUnitStep) {
                cnChars[cursor--] = 'x';
//                System.out.println("====> cursor: " + cursor);
                if (cursor > 1 && !(cnArr[0] == cnChars[cursor - 1])) {
                    // 补0
                    cnChars[++cursor] = cnArr[0];
                }
            }

            // 单位进位(万、亿)，或者非0时加上单位
            if (isUnitStep || !isZero) {
                cnChars[++cursor] = cnUnit;
                isLastUnitStep = isUnitStep;
            }
            // 当前位为 0 且相邻低位为 0，或者当前位为 0 并且为单位进位时进行省略
            if (isZero && (isZeroLow || isUnitStep))
                continue;

            cnChars[++cursor] = cnChar;
            isLastUnitStep = false;
        }

        Arrayx.reverse(cnChars, offset, cursor);
        // 清除最后一位的0  
        char chEnd = cnChars[cursor];
        if (cnArr[0] == chEnd || allCnUnits[0] == chEnd) {
            cnChars[cursor--] = 'x';
        }

        // 口语化处理  
        if (isColloquial) {
            char chFirst = cnChars[0];
            char chSecond = cnChars[1];
            // 是否以'一'开头，紧跟'十'
            if (chFirst == cnArr[1] && chSecond == allCnUnits[1]) {
                cnChars[offset++] = 'x';
            }
        }
        return new String(cnChars, offset, cursor + 1 - offset);
    }
}