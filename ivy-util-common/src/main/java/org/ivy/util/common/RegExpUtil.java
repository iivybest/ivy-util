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
 * <p>  classname: RegExpUtil
 * <br> description: 正则表达式工具
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @date 2019/12/24 18:44
 * @version 1.0
 */
public enum RegExpUtil {
    instance;


    /**
     * regExp
     * 匹配正则表达式的所有数据
     *
     * @param data  data
     * @param regex tegular expression
     * @return List
     */
    public List<String> match(String data, String regex) {
        if (StringUtil.isBlank(data) || StringUtil.isBlank(regex)) return null;

        List<String> result = new ArrayList<String>();
        Matcher m = Pattern.compile(regex).matcher(data);
        while (m.find()) result.add(m.group());
        return result;
    }

}
