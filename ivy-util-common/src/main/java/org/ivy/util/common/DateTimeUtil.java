package org.ivy.util.common;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * <p> description: 日期时间工具
 * <br>--------------------------------------------------------
 * <br> 1、日期
 * <br> 2、时间戳、Unix时间戳
 * <br> 3、格式化、格式转换
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
    // ----默认日期格式
    private static String defaultPattern = "yyyy-MM-dd HH:mm:ss";

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
        if (StringUtil.isBlank(pattern)) pattern = defaultPattern;
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
        return format(date, defaultPattern);
    }

    public static String format(Date date, String pattern) {
        return format(getDateTime(date), pattern);
    }

    public static String formate(Date date) {
        return format(date, defaultPattern);
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
    public static String datePatternAdapte(String date, String oPattern, String nPattern) throws Exception {
        // ----非空检查，主要针对字段 date
        if (StringUtil.containsBlank(date, nPattern, nPattern))
            throw new Exception("====【operaion args can not be null】");

        Temporal time = null;
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
     * @param timestamp
     * @param pattern
     * @param unit
     * @return String
     */
    public static String datePatternAdapte(Long timestamp, TemporalUnit unit, String pattern) {
        return format(getDateTime(timestamp, unit), pattern);
    }

    /**
     * @param timestamp
     * @param unit
     * @return String
     * @className: datePatternAdapte
     * 将时间戳适配为默认格式 "yyyy-MM-dd HH:mm:ss" 时间字符串
     */
    public static String datePatternAdapte(Long timestamp, TemporalUnit unit) {
        return datePatternAdapte(timestamp, unit, defaultPattern);
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
        if (ChronoUnit.MILLIS.equals(unit)) instant = Instant.ofEpochMilli(timestamp);
        else if (ChronoUnit.SECONDS.equals(unit)) instant = Instant.ofEpochSecond(timestamp);

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
     * @param time
     * @return Long
     */
    public static Long getTimestamp(LocalDateTime time) {
        LocalDateTime _time = (null == time) ? LocalDateTime.now() : time;
        return _time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
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
        LocalDateTime _time = (null == time) ? LocalDateTime.now() : time;
        return _time.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取一个距离from间隔为to的时间戳
     *
     * @param from 起点时间（时间戳） -- from == null起点时间为now
     * @param to   间隔时间（指定值）-- 距离起点时间间隔To
     * @param unit 时间单位（用户指定的时间单位）
     * @return Long
     */
    public static Long getTimestamp(Long from, Long to, TemporalUnit unit) {
        Long _from = (null == from) ? getTimestamp(null) : from;
        // ----ChronoUnit / Unit implements interface TemporalUnit
        LocalDateTime time = getDateTime(_from, ChronoUnit.MILLIS).plus(to, unit);
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
        // ----
        Long _from = (from <= 0) ? getUnixTimestamp(null) : from;
        // ----ChronoUnit / Unit implements interface TemporalUnit
        LocalDateTime time = getDateTime(_from, ChronoUnit.SECONDS).plus(to, unit);
        // ----目标时刻转为时间戳
        return getUnixTimestamp(time);
    }

}









