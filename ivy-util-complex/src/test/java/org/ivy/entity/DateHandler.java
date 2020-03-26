package org.ivy.entity;

import org.ivy.util.common.DateTimeUtil;

import java.util.Date;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @description TODO
 * @date 2020/3/21 16:28
 */
public class DateHandler {

    public static final String PATTERN_DEFAULT = "yyy-MM-dd HH:mm:ss";

    public static String currentDatetime() {
        return currentDatetime(PATTERN_DEFAULT);
    }

    public static String currentDatetime(String pattern) {
        return DateTimeUtil.format(new Date(), pattern);
    }


}
