package org.ivy.util.common;

import org.ivy.util.annotation.Recommend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.ivy.util.common.IvyConstant.DIGIT_ARR;
import static org.ivy.util.common.IvyConstant.HEX_ARR;

/**
 * <p>  classname: DigitUtil
 * <br> description: 数字处理统计
 * <br>---------------------------------------------------------
 * <br> 功能描述：
 * <br> 1、中文数字、阿拉伯数字转换
 * <br> 2、数字进制转换（十进制、二进制、十六进制）
 * <br>---------------------------------------------------------
 * <br> 中文数字		Chinese
 * <br> 中文数字单位	Chinese digit unit
 * <br> 大写中文数字	Capital Chinese digit
 * <br> 阿拉伯数字	Arabic digit
 * <br>---------------------------------------------------------
 * <br> 定义：中文数字的值 (value) = 权值(weight) X 基数(radix)
 * <br>---------------------------------------------------------
 * <br>	中文数字转 integer/long 思路:
 * <br> 1、将汉字从右向左依次处理，
 * <br> 2、每次处理一个中文数字单位，例如”一万零八百零六“拆分为”(一万)(零八百)(零六)“
 * <br> 3、结果累加起来得到最后结果
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/10 10:28
 */
public class DigitUtil {

    /**
     * 小数点
     */
    private static final String DECIMAL_POINT = ".";
    private static final char CN_DECIMAL_POINT = '点';
    /**
     * 中文数字单位序列
     */
    private static final char[] CU_ARR = {'十', '百', '千', '万', '亿'};
    private static final char[] CAPITAL_CU_ARR = {'拾', '佰', '仟', '万', '亿'};
    /**
     * 中文数字序列
     */
    private static final char[] CN_ARR = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static final char[] CAPITAL_CN_ARR = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    /**
     * 完整中文数字单位
     */
    private static final char[] ALL_CU_ARR = {'个', '十', '百', '千', '万', '十', '百', '千', '亿', '十', '百', '千', '万'};
    /**
     * 单位进位，中文默认为4位即（万、亿）
     */
    private static final int UNIT_STEP = 4;
    /**
     * 中文数字正则表达式
     */
    private static final String REGEX_CN_DIGIT = "([零一二三四五六七八九十百千万亿]{1,})";
    private static final String REGEX_CAPITAL_CN_DIGIT = "([壹贰叁肆伍陆柒捌玖拾佰仟万亿]+)";
    private static final Pattern PATTERN_CN_DIGIT = Pattern.compile(REGEX_CN_DIGIT, Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_CAPITAL_CN_DIGIT = Pattern.compile(REGEX_CAPITAL_CN_DIGIT, Pattern.CASE_INSENSITIVE);

    private DigitUtil() {
    }

    /**
     * <p> description: 获取中文数字单位的基数值
     * <br>--------------------------------------------------------
     * <br> 阿拉伯数字采用10进制，中文单位的基数定义为 10的权值次幂
     * <br>	拾    0    10^1    10
     * <br> 佰    1    10^2    100
     * <br> 仟    2    10^3    1_000
     * <br> 万    3    10^4    10_000
     * <br> 亿    4    10^8    100_000_000
     * <br>--------------------------------------------------------
     * </p>
     *
     * @param unit unit
     * @return int radix
     */
    private static int getChineseDigitUnitRadix(int unit) {
        if (unit < 0 || unit > CU_ARR.length) {
            return Integer.MIN_VALUE;
        }
        int radix = -1;
        switch (unit) {
            case 0:
                radix = 10;
                break;
            case 1:
                radix = 100;
                break;
            case 2:
                radix = 1_000;
                break;
            case 3:
                radix = 10_000;
                break;
            case 4:
                radix = 100_000_000;
                break;
            default:
                break;
        }
        return radix;
    }

    /**
     * <p> description: 获取中文数字单位的基数值
     * <br>--------------------------------------------------------
     * <br> 阿拉伯数字采用10进制，中文单位的基数定义为 10的权值次幂
     * <br>	拾    0    10^1    10
     * <br> 佰    1    10^2    100
     * <br> 仟    2    10^3    1_000
     * <br> 万    3    10^4    10_000
     * <br> 亿    4    10^8    100_000_000
     * <br>--------------------------------------------------------
     * </p>
     *
     * @param unit unit
     * @return int radix
     */
    private static int getChineseDigitUnitRadix(char unit) {
        // 中文数字单位所在 cuArr 的下标值
        int unitVal = -1;
        // 中文数字单位对应的权值
        int radix = Integer.MIN_VALUE;
        for (int i = 0, len = CU_ARR.length; i < len; i++) {
            if (CU_ARR[i] == unit) {
                unitVal = i;
                break;
            }
        }
        if (unitVal > 0) {
            radix = getChineseDigitUnitRadix(unitVal);
        }
        return radix;
    }

    /**
     * <p> description: 长整型数字转中文数字
     * <br>--------------------------------------------------------
     * <br> 处理思路：
     * <br>
     * <br>--------------------------------------------------------
     * <br> 处理流程：
     * <br> 1、data 为负数，则将data转为正数带入计算，并在结果前添加“负”
     * <br> 2、data 小于 10, 直接将 data 按照 cnArr 下标转为中文数字
     * <br> 3、data 长度超过 cllCuArr 长度，则 data 超过最大处理范围
     * <br> 4、data 进入正常处理流程
     * <br>--------------------------------------------------------
     * <br> step-4 处理过程：
     * <br>
     * <br>--------------------------------------------------------
     * </p>
     *
     * @param data data
     * @return String String
     */
    public static String long2ChineseDigit(long data, boolean isColloquial) {
        /* ====>step-1: 处理负数*/
        if (data < 0) return "负" + long2ChineseDigit(-data, isColloquial);
        /* ====>step-2: 处理小于 10 的整数 */
        // 10以下直接返回对应汉字，ASCII 2 Integer String
        if (data < 10) return String.valueOf(CN_ARR[(int) data]);
        /* ====>step-3: 处理超范围的数*/
        char[] chars = String.valueOf(data).toCharArray();
        // 超过单位表示范围的返回空 TODO
        if (chars.length > ALL_CU_ARR.length) return "";
        /* ====>step-4: long处理过程*/
        // 记录上次单位进位 万、亿进位
        boolean isLastUnitStep = false;
        // 创建数组，将数字填入单位对应的位置
        char[] cnChars = new char[chars.length * 2];
        // cnChars 中有效 char 区间，offset 起始位置，cursor 结束位置
        int offset = 0, cursor = -1;
        // 从低位向高位循环
        for (int i = chars.length - 1; i >= 0; i--) {
            char ch = chars[i];
            // ascii 2 int
            char cnChar = CN_ARR[ch - '0'];
            // 对应的单位坐标
            int unitPos = chars.length - i - 1;
            // 单位
            char cnUnit = ALL_CU_ARR[unitPos];
            // 是否为 0
            boolean isZero = (ch == '0');
            // 是否低位为 0
            boolean isZeroLow = (i + 1 < chars.length && chars[i + 1] == '0');
            // 当前位是否需要单位进位
            boolean isUnitStep = (unitPos >= UNIT_STEP && (unitPos % UNIT_STEP == 0));
            // 去除相邻的上一个单位进位
            if (isUnitStep && isLastUnitStep) {
                cnChars[cursor--] = 'x';
                if (cursor > 1 && (CN_ARR[0] != cnChars[cursor - 1])) {
                    // 补0
                    cnChars[++cursor] = CN_ARR[0];
                }
            }

            // 单位进位(万、亿)，或者非0时加上单位
            if (isUnitStep || !isZero) {
                cnChars[++cursor] = cnUnit;
                isLastUnitStep = isUnitStep;
            }
            // 当前位为 0 且相邻低位为 0，或者当前位为 0 并且为单位进位时进行省略
            if (isZero && (isZeroLow || isUnitStep)) continue;

            cnChars[++cursor] = cnChar;
            isLastUnitStep = false;
        }
        /* 数组倒序*/
        Arrayx.reverse(cnChars, offset, cursor);
        // 清除最后一位的0
        char chEnd = cnChars[cursor];
        if (CN_ARR[0] == chEnd || ALL_CU_ARR[0] == chEnd) {
            cnChars[cursor--] = 'x';
        }

        // 口语化处理
        if (isColloquial) {
            char chFirst = cnChars[0];
            char chSecond = cnChars[1];
            // 是否以'一'开头，紧跟'十'
            if (chFirst == CN_ARR[1] && chSecond == ALL_CU_ARR[1]) {
                cnChars[offset++] = 'x';
            }
        }
        return new String(cnChars, offset, cursor + 1 - offset);
    }

    public static String long2ChineseDigit(long data) {
        return long2ChineseDigit(data, false);
    }

    private static String long2ChineseDigitDirect(long data) {
        char[] dataChars = String.valueOf(data).toCharArray();
        char[] cnChars = new char[dataChars.length];
        for (int i = 0, len = dataChars.length; i < len; i++) {
            cnChars[i] = CN_ARR[dataChars[i] - '0'];
        }
        return new String(cnChars);
    }


    public static String double2ChineseDigit(double data, boolean isColloquial) {
        if (data < 0) {
            return "负" + double2ChineseDigit(-data, isColloquial);
        }
        String dataStr = String.valueOf(data);
        if (!dataStr.contains(DECIMAL_POINT)) {
            return long2ChineseDigit((long) data, isColloquial);
        }
        String[] arr = dataStr.split("\\" + DECIMAL_POINT);
        return long2ChineseDigit(Long.valueOf(arr[0]), isColloquial)
                + CN_DECIMAL_POINT
                + long2ChineseDigitDirect(Long.valueOf(arr[1]));
    }

    public static String double2ChineseDigit(double data) {
        return double2ChineseDigit(data, false);
    }


    public static int capitalChineseDigit2Integer(String capitalChineseDigit) {
        // TODO
        return Integer.MIN_VALUE;
    }

    /**
     * 中文数字转整型数字
     *
     * @param chineseDigit chineseDigit
     * @return int int
     */
    public static int chineseDigit2Integer(String chineseDigit) {
        return chineseDigit2Integer(chineseDigit.toCharArray());
    }

    /**
     * <p> description: 中文数字转整型数字
     * <br>--------------------------------------------------------
     * <br> description: 中文数字转整型数字
     * <br> 1、integer 可取值范围 [-2^32, 2^32); [-2147483648, 2147483647]
     * <br> 1、中文数字分为基数和权值 eg：一万 Radix：10_000; Weight:1;
     * <br> 2、自右向左依次处理中文数字。每处理一个单位将结果数值写入到 result
     * <br> 3、记录已处理的最大单位，若当前处理元素的单位大于等于已处理的最大单位
     * <br>		result 也要乘以当前处理元素的基数 Radix
     * <br>--------------------------------------------------------
     * </p>
     *
     * @param chineseDigit chineseDigit
     * @return int int
     */
    public static int chineseDigit2Integer(char[] chineseDigit) {
        int result = 0;
        // 存放所有的单位下标
        int[] unitArr = new int[chineseDigit.length];
        // 存放每个单位对应的数值
        int[] valueArr = new int[chineseDigit.length];
        // 游标； valueArr, unitArr 下标
        int cursor = -1;
        /* ====>step-1：遍历 chineseDigit，以单位为最小单元，从右向左依次处理*/
        {
            // 当前元素
            char ele;
            // 判断 ele 是否为数字
            boolean isDigit;
            // 当前处理元素（数字或元素）在数组（cnArr，cuArr）中的下标值
            int eleIdx = -1;
            // ele 的权值、基数；如：五万，weight = 5； radix = 10_000;
            int weight = 0, radix;
            for (int i = 0, len = chineseDigit.length; i < len; i++) {
                /* ====> step-1：初始化操作*/
                // 每次处理前设置 isDigit 为初始值 false
                isDigit = false;
                // 当前处理字符
                ele = chineseDigit[i];

                /* ====> step-2：判断 currentElement 类型并为 currentElementIdx、weight 赋值*/
                // currentElement 非单位，即数字，首先判断 currentElement 是否在 cnArr 中
                for (int idx = 0, cnLen = CN_ARR.length; idx < cnLen; idx++) {
                    if (CN_ARR[idx] == ele) {
                        isDigit = true;
                        eleIdx = idx;
                        break;
                    }
                }
                // 若 currentElement不是数字，则判断其是否在 cuArr 当中
                if (!isDigit) {
                    for (int idx = 0, cuLen = CU_ARR.length; idx < cuLen; idx++) {
                        if (CU_ARR[idx] == ele) {
                            eleIdx = idx;
                            break;
                        }
                    }
                }

                /* ====>step-3：计算每个单元的数值*/
                // 每次处理 cursor 后移一位
                if (isDigit) {
                    weight = eleIdx;
                    // 若数字为最后一位元素，最后一位的基数为 1，所以将 weight 加入到 result 中
                    if (i == len - 1) {
                        cursor++;
                        unitArr[cursor] = -1;
                        valueArr[cursor] = weight;
                    }
                } else {
                    cursor++;
                    // ----step-3.1：计算 currentElement 的基数 Radix
                    radix = getChineseDigitUnitRadix(eleIdx);
                    // ----step-3.2：查看 currentElement 是否大于已处理的最大单位
                    int temp;
                    for (int ci = cursor - 1; ci >= 0; ci--) {
                        if (unitArr[ci] <= eleIdx) {
                            temp = valueArr[ci];
                            valueArr[ci] = temp * radix;
                            continue;
                        }
                        if (unitArr[ci] > eleIdx) break;
                    }
                    // ----step-3.3：将 currentElement 对应的数值 val (val = 权值 X 基数)，累加到 result
                    unitArr[cursor] = eleIdx;
                    valueArr[cursor] = (weight == 0 && cursor == 0 ? 1 : weight) * radix;
                    // ----step-3.5：将 cached 恢复为初始值
                    weight = 0;
                }
            }
        }
        /* ====>step-2：将 valueArr 进行汇总写入到 result 中*/
        for (int i = 0; i <= cursor; i++) {
            result += valueArr[i];
        }
        return result;
    }

    /**
     * 中文数字转长整型数字
     *
     * @param chineseDigit chineseDigit
     * @return long
     */
    public static long chineseDigit2Long(String chineseDigit) {
        return chineseDigit2Long(chineseDigit.toCharArray());
    }

    /**
     * <p> description: 中文数字转长整型数字
     * <br>--------------------------------------------------------
     * <br> 1、中文数字分为基数和权值 eg：一万 Radix：10_000; Weight:1;
     * <br> 2、自右向左依次处理中文数字。每处理一个单位将结果数值写入到 result
     * <br> 3、记录已处理的最大单位，若当前处理元素的单位大于等于已处理的最大单位
     * <br>		result 也要乘以当前处理元素的基数 Radix
     * <br>--------------------------------------------------------
     * </p>
     *
     * @param chineseDigit chineseDigit
     * @return long
     */
    public static long chineseDigit2Long(char[] chineseDigit) {
        long result = 0;
        // 存放所有的单位下标
        int[] unitArr = new int[chineseDigit.length];
        // 存放每个单位对应的数值
        long[] valueArr = new long[chineseDigit.length];
        // 游标； valueArr, unitArr 下标
        int cursor = -1;
        /* ====>step-1：遍历 chineseDigit，以单位为最小单元，从右向左依次处理*/
        {
            // 当前元素
            char ele;
            // 判断 ele 是否为数字
            boolean isDigit = false;
            // 当前处理元素（数字或元素）在数组（cnArr，cuArr）中的下标值
            int eleIdx = -1;
            // ele 的权值、基数；如：五万，weight = 5；radix = 10_000；
            int weight = 0, radix;
            for (int i = 0, len = chineseDigit.length; i < len; i++, isDigit = false) {
                // 当前处理字符
                ele = chineseDigit[i];

                /* ====> step-1.1：判断 ele 类型并为 eleIdx、weight 赋值*/
                // ele 非单位，即数字，首先判断 ele 是否在 cnArr 中
                for (int idx = 0, cnLen = CN_ARR.length; idx < cnLen; idx++) {
                    if (CN_ARR[idx] == ele) {
                        isDigit = true;
                        eleIdx = idx;
                        break;
                    }
                }
                // 若 ele 不是数字，则判断其是否在 cuArr 当中
                if (!isDigit) {
                    for (int idx = 0, cuLen = CU_ARR.length; idx < cuLen; idx++) {
                        if (CU_ARR[idx] == ele) {
                            eleIdx = idx;
                            break;
                        }
                    }
                }

                /* ====>step-1.3：计算每个单元的数值*/
                // 每次处理 cursor 后移一位
                cursor++;
                if (isDigit) {
                    weight = eleIdx;
                    // 若数字为最后一位元素，最后一位的基数为 1，所以将 weight 加入到 result 中
                    if (i == len - 1) {
                        unitArr[cursor] = -1;
                        valueArr[cursor] = weight;
                    }
                } else {
                    // ----step-3.1：计算 currentElement 的基数 Radix
                    radix = getChineseDigitUnitRadix(eleIdx);
                    // ----step-3.2：查看 currentElement 是否大于已处理的最大单位
                    long temp;
                    for (int ci = cursor - 1; ci >= 0; ci--) {
                        if (unitArr[ci] <= eleIdx) {
                            temp = valueArr[ci];
                            valueArr[ci] = temp * radix;
                            continue;
                        }
                        if (unitArr[ci] > eleIdx) break;
                    }
                    // ----step-3.3：将 currentElement 对应的数值 val (val = 权值 X 基数)，累加到 result
                    unitArr[cursor] = eleIdx;
                    valueArr[cursor] = (weight == 0 && cursor == 0 ? 1 : weight) * radix;
                    // ----step-3.5：将 cached 恢复为初始值
                    weight = 0;
                }
            }
        }
        /* ====>step-2：将 valueArr 进行汇总写入到 result 中*/
        for (int i = 0; i <= cursor; i++) {
            result += valueArr[i];
        }

        return result;
    }


    /**
     * 中文数字转阿拉伯数字字符串
     *
     * @param chineseDigit chineseDigit
     * @param level        the length of ArabicDigit  user expect
     * @return String String
     */
    public static String chineseDigit2Arabic(char[] chineseDigit, int level) {
        int arabicDigit = chineseDigit2Integer(chineseDigit);
        String result = String.valueOf(arabicDigit);

        if (level <= 0 || result.length() >= level) return result;

        int detta = level - result.length();
        char[] prifix = new char[detta];
        for (int i = 0; i < detta; i++) {
            prifix[i] = '0';
        }
        return new String(prifix) + result;
    }

    /**
     * <p> description: 将一段文本中的中文数字转换为阿拉伯数字
     * <br>--------------------------------------------------------
     * <br> 1、支持整型、
     * <br> 2、浮点型数字转换 TODO
     * <br> 3、用户可以指定句子中的一小段进行转换，并可以设置转换后数字宽度
     * <br> 4、offset 或者 length 小于 0，则全文进行替换处理
     * <br> 5、level 小于 0，则不进行数字宽度格式化
     * <br>--------------------------------------------------------
     * </p>
     *
     * @param sentence A sentence with Chinese digits
     * @param offset   the begin index in sencence
     * @param length   the length which need process in sentence
     * @param level    format the width of arabicDigits
     * @return String String
     */
    public static String chineseDigit2Arabic(String sentence, int offset, int length, int level) {
        /* 将 sentence 切割为 3 部分，target 为 用户指定处理部分*/
        String prifix = "", target, suffix = "";
        if (offset < 0 || length < 0) target = sentence;
        else {
            prifix = sentence.substring(0, offset);
            suffix = sentence.substring(offset + length);
            target = sentence.substring(offset, length);
        }

        Matcher matcher = PATTERN_CN_DIGIT.matcher(target);
        StringBuffer sb = new StringBuffer(prifix);
        String arabic;
        while (matcher.find()) {
            arabic = chineseDigit2Arabic(matcher.group().toCharArray(), level);
            matcher.appendReplacement(sb, arabic);
        }
        matcher.appendTail(sb);

        return sb.append(suffix).toString();
    }

    public static String chineseDigit2Arabic(String sentence, int offset, int length) {
        return chineseDigit2Arabic(sentence, offset, length, -1);
    }

    public static String chineseDigit2Arabic(String sentence, int level) {
        return chineseDigit2Arabic(sentence, -1, -1, level);
    }

    public static String chineseDigit2Arabic(String sentence) {
        return chineseDigit2Arabic(sentence, -1);
    }


    public static String toBinString(int data) {
        char[] sequence = new char[32];
        for (int j = sequence.length - 1, cursor = 0; j >= 0; ) {
            sequence[cursor++] = DIGIT_ARR[((data >>> j--) & 1)];
        }
        return new String(sequence);
    }

    /**
     * 字节数字转 Hex 字符串
     *
     * @param data
     * @return String
     */
    @Recommend(value = true, msg = {
            "1、char arr 代替 StringBuilder",
            "2、不推荐使用 Integer.toHex",
            "3、byte 8 位，Hex 长度为 2",
            "4、byte 高 4 位，data >>> 4 | data & 0xF0",
            "5、byte 低 4 位，data & 0xF"})
    public static String toHexString(byte... data) {
        int len = data.length;
        char[] hexChars = new char[len * 2];
        int v;
        for (int i = 0; i < len; i++) {
            v = data[i] & 0xFF;
            hexChars[i * 2] = HEX_ARR[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARR[v & 0xF];
        }
        return new String(hexChars);
    }

    public static String toHexString(char data) {
        return toHexString((short) data);
    }

    public static String toHexString(short data) {
        return toHexString((byte) (data >>> 8 & 0xFF), (byte) (data & 0xFF));
    }

    public static String toHexString(int data) {
        /* 将 int 切分成 4 个 byte*/
        return toHexString(
                (byte) (data >>> 24 & 0xFF),
                (byte) (data >>> 16 & 0xFF),
                (byte) (data >>> 8 & 0xFF),
                (byte) (data & 0xFF)
        );
    }

    public static String toHexString(long data) {
        /* 将 long 切分成 8 个 byte*/
        return toHexString(
                (byte) (data >>> 56 & 0xFF),
                (byte) (data >>> 48 & 0xFF),
                (byte) (data >>> 40 & 0xFF),
                (byte) (data >>> 32 & 0xFF),
                (byte) (data >>> 24 & 0xFF),
                (byte) (data >>> 16 & 0xFF),
                (byte) (data >>> 8 & 0xFF),
                (byte) (data & 0xFF)
        );
    }
}








