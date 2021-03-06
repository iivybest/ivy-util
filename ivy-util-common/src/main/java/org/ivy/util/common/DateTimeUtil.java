package org.ivy.util.common;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * <p>
 * <br>--------------------------------------------------------
 * <br> description: 日期时间工具
 * <br>     * 1、日期
 * <br>     * 2、时间戳、Unix时间戳
 * <br>     * 3、格式化
 * <br>     * 4、格式转换
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/9 7:59
 * @since 1.8+
 */
public class DateTimeUtil {
    /*
     * 默认日期格式
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * get current date time
     *
     * @return LocalDateTime
     */
    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * get current date time formatted with pattern
     *
     * @param pattern date pattern
     * @return String
     */
    public static String currentDateTime(String pattern) {
        if (StringUtil.isBlank(pattern)) {
            pattern = DEFAULT_DATE_PATTERN;
        }
        return format(currentDateTime(), pattern);
    }

    /**
     * formate DateTime
     *
     * @param dateTime dateTime
     * @param pattern  pattern
     * @return String
     */
    public static String format(TemporalAccessor dateTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(dateTime);
    }

    /**
     * format Date
     *
     * @param date date
     * @return String
     */
    public static String format(LocalDateTime date) {
        return format(date, DEFAULT_DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        return format(getDateTime(date), pattern);
    }

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_PATTERN);
    }


    /**
     * date format adaptation
     *
     * @param date     date string
     * @param oPattern old pattern
     * @param nPattern new pattern
     * @return String
     * @throws Exception
     */
    public static String datePatternAdapt(String date, String oPattern, String nPattern) throws Exception {
        // ----非空检查，主要针对字段 date
        if (StringUtil.containsBlank(date, nPattern, nPattern)) {
            throw new Exception("====[operation args can not be null]");
        }
        Temporal time;
        try {
            if (oPattern.contains("h") || oPattern.contains("H")) {
                time = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(oPattern));
            } else {
                time = LocalDate.parse(date, DateTimeFormatter.ofPattern(oPattern));
            }
        } catch (Exception e) {
            throw new Exception("====[" + date + "] not matched [" + oPattern + "]", e);
        }

        String targetPatternDate = format(time, nPattern);
        return targetPatternDate;
    }

    /**
     * 将时间戳适配为指定格式时间字符串
     *
     * @param timestamp timestamp
     * @param pattern   pattern
     * @param unit      time unit
     * @return the formatted date with pattern
     */
    public static String datePatternAdapt(Long timestamp, TemporalUnit unit, String pattern) {
        return format(getDateTime(timestamp, unit), pattern);
    }

    /**
     * 将时间戳适配为默认格式时间字符串
     *
     * @param timestamp
     * @param unit
     * @return String
     * 将时间戳适配为默认格式 "yyyy-MM-dd HH:mm:ss" 时间字符串
     */
    public static String datePatternAdapt(Long timestamp, TemporalUnit unit) {
        return datePatternAdapt(timestamp, unit, DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取 date（将 localDateTime 转换为 date）
     *
     * @param localDateTime
     * @return Date
     */
    public static Date getDate(LocalDateTime localDateTime) {
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * 获取 dateTime（将 date 转换为 localDateTime）
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime getDateTime(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dateTime;
    }

    /**
     * 获取 dateTime（将 timestamp 转换为 localDateTime）
     *
     * @param timestamp
     * @param unit
     * @return LocalDateTime
     */
    public static LocalDateTime getDateTime(long timestamp, TemporalUnit unit) {
        Instant instant = null;
        if (ChronoUnit.MILLIS.equals(unit)) {
            instant = Instant.ofEpochMilli(timestamp);
        } else if (ChronoUnit.SECONDS.equals(unit)) {
            instant = Instant.ofEpochSecond(timestamp);
        }

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * @return Long
     * getCurrentTimestamp
     * 获取当前时间戳--毫秒数
     */
    public static Long getCurrentTimestamp() {
        return getTimestamp();
    }

    /**
     * @return Long
     * getCurrentUnixTimestamp
     * 获取当前时间UNIX时间戳戳--秒数
     */
    public static Long getCurrentUnixTimestamp() {
        return getUnixTimestamp();
    }

    /**
     * 获取当前时间戳--毫秒数
     *
     * @return Long
     */
    public static Long getTimestamp() {
        return getTimestamp(null);
    }

    /**
     * 获取某个时刻的时间戳
     *
     * @param time time
     * @return Long timestamp
     */
    public static Long getTimestamp(LocalDateTime time) {
        LocalDateTime dateTime = (null == time) ? LocalDateTime.now() : time;
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当前时间UNIX时间戳戳--秒数
     *
     * @return Long
     */
    public static Long getUnixTimestamp() {
        return getUnixTimestamp(null);
    }

    /**
     * 获取某个时刻的UNIX时间戳
     *
     * @param time
     * @return Long
     */
    public static Long getUnixTimestamp(LocalDateTime time) {
        LocalDateTime dateTime = (null == time) ? LocalDateTime.now() : time;
        return dateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取一个距离 from 间隔为 to 的时间戳
     *
     * @param from 起点时间（时间戳） -- from == null起点时间为now
     * @param to   间隔时间（指定值）-- 距离起点时间间隔To
     * @param unit 时间单位（用户指定的时间单位）
     * @return Long timestamp
     */
    public static Long getTimestamp(long from, long to, TemporalUnit unit) {
        Long originTimeStamp = (from < 0) ? getTimestamp(null) : from;
        // ----ChronoUnit / Unit implements interface TemporalUnit
        LocalDateTime time = getDateTime(originTimeStamp, ChronoUnit.MILLIS).plus(to, unit);
        // ----目标时刻转为时间戳
        return getTimestamp(time);
    }

    /**
     * 获取一个距离 from 间隔为 to 的 UNIX 时间戳
     *
     * @param from 起点时间（UNIX时间戳） -- from == null起点时间为now
     * @param to   间隔时间（指定值）-- 距离起点时间间隔To
     * @param unit 时间单位（用户指定的时间单位）
     * @return Long
     */
    public static Long getUnixTimestamp(long from, long to, TemporalUnit unit) {
        Long stampFrom = (from < 0) ? getUnixTimestamp(null) : from;
        // ----ChronoUnit / Unit implements interface TemporalUnit
        LocalDateTime time = getDateTime(stampFrom, ChronoUnit.SECONDS).plus(to, unit);
        // ----目标时刻转为时间戳
        return getUnixTimestamp(time);
    }

}









