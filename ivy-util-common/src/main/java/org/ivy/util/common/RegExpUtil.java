/**
 * Filename 	RegExpUtil
 *
 * @author ivybest
 * @version V1.0
 * CreateDate 	2017年7月28日 上午9:17:17
 * Company 		IB.
 * Copyright 	Copyright(C) 2010-
 * All rights Reserved, Designed By ivybest
 * <p>
 * Modification History:
 * Date			Author		Version		Discription
 * --------------------------------------------------------
 * 2017年7月28日		ivybest		1.0			new create
 */
package org.ivy.util.common;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> description: 正则表达式工具
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/24 18:44
 */
public class RegExpUtil {

    private RegExpUtil() {
    }


    /**
     * regExp
     * 匹配正则表达式的所有数据
     *
     * @param data  data
     * @param regex regular expression
     * @return List
     */
    public static List<String> match(String data, String regex) {
        if (StringUtil.isBlank(data) || StringUtil.isBlank(regex)) {
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        return RegExpUtil.match(data, pattern);
    }


    public static List<String> match(String data, Pattern pattern) {
        if (StringUtil.isBlank(data)) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        if (null == pattern) {
            result.add(data);
            return result;
        }

        Matcher m = pattern.matcher(data);
        while (m.find()) {
            result.add(m.group());
        }
        return result;
    }

}
