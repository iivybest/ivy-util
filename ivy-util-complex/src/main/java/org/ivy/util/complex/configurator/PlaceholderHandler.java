package org.ivy.util.complex.configurator;

import org.ivy.util.common.RegExpUtil;
import org.ivy.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br> placeholder protocol
 * <br>     type: String[]
 * <br>     len: 2
 * <br>     schema: [placeholder, sn]
 * <br>
 * <br> #{}
 * <br> #{ }
 * <br> #{0}
 * <br> #{1 }
 * <br> #{ 2}
 * <br> #{ 3 }
 * <br>---------------------------------------------------------
 * <br> description:
 * <br>
 * <br>
 * <br>---------------------------------------------------------
 * <br>
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/3/19 15:29
 */
public class PlaceholderHandler {
    /**
     * placeholder pattern
     */
    private static final Pattern PATTERN = Pattern.compile("#\\{[\\s]*[\\d]*[\\s]*\\}");
    /**
     * default sn
     */
    private static final String DEFAULT_SN = "10000";
    private static Logger log = LoggerFactory.getLogger(PlaceholderHandler.class);
    /**
     * @description Protocol Serial Comparator
     * @author ivybest (ivybestdev@163.com)
     * @version 1.0
     * @date 2020/3/4 0:28
     */
    private Comparator<String[]> protocolSerialComparator = new Comparator<String[]>() {
        @Override
        public int compare(String[] arg0, String[] arg1) {
            // ----升序比较设置
            return Integer.valueOf(arg0[1]) - Integer.valueOf(arg1[1]);
        }
    };

    /**
     * get PlaceholderHandler instance
     *
     * @return PlaceholderHandler instance
     */
    public static PlaceholderHandler newInstance() {
        return new PlaceholderHandler();
    }

    /**
     * el 表达式处理
     *
     * @param message
     * @return result
     */
    public String handle(String message, Object... values) {
        if (StringUtil.isBlank(message)) {
            return message;
        }
        // ----the expression handle
        List<String> expressions = RegExpUtil.match(message, PATTERN);
        // ----no expression in message, return origin message
        if (null == expressions || expressions.size() == 0) {
            return message;
        }

        String result = message;
        List<String[]> protocols = this.explainExpression(expressions);
        int len = Math.min(protocols.size(), values.length);

        String placeholder, val;
        for (int i = 0; i < len; i++) {
            placeholder = this.getReplaceCharacterSequence(protocols.get(i));
            val = (null == values[i]) ? "null" : String.valueOf(values[i]);
            result = result.replaceFirst(placeholder, val);
        }
        return result;
    }


    /**
     * <p>
     * <br>---------------------------------------------------------
     * <br> description: process expression
     * <br> result schema: {expression, sn, iExp};
     * <br> 若没有数字,设置默认值 10_000;
     * <br>---------------------------------------------------------
     * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @param expression expression
     * @return result of String array
     * @throws Exception
     */
    private String[] explainExpression(String expression) {
        String iExp = expression.substring(2, expression.length() - 1);
        String[] protocol = new String[3];
        protocol[0] = expression;
        protocol[1] = DEFAULT_SN;
        protocol[2] = iExp;

        if (iExp.length() == 0) {
            return protocol;
        }
        String sn = iExp.trim();
        if (!StringUtil.isNumeric(sn)) {
            return protocol;
        }
        // 使用整型数字标识顺序号
        protocol[1] = String.valueOf(new BigDecimal(sn).intValue());
        return protocol;
    }


    private List<String[]> explainExpression(List<String> expressions) {
        List<String[]> protocols = new ArrayList<>(expressions.size());
        for (String e : expressions) {
            protocols.add(this.explainExpression(e));
        }
        Collections.sort(protocols, this.protocolSerialComparator);
        return protocols;
    }

    /**
     * 获取可替换字符串
     * 由于 placeholder 含有正则表达式字符。需要转义
     *
     * @param protocol protocol
     * @return character sequence
     */
    private String getReplaceCharacterSequence(String[] protocol) {
        return "#\\{" + protocol[2] + "\\}";
    }


}
