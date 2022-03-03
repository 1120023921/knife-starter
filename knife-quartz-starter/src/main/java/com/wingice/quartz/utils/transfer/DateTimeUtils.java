package com.wingice.quartz.utils.transfer;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author 胡昊
 * Description: 时间工具类
 * Date: 2019/5/18
 * Time: 22:16
 * Create: DoubleH
 */
public class DateTimeUtils {

    /**
     * 字符串转时间戳
     *
     * @param dateTime 日期时间字符串
     * @param zoneId   区域偏移
     * @param pattern  时间表达式
     * @return 时间戳
     */
    public static Long stringToLong(String dateTime, ZoneId zoneId, String pattern) {
        final DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return ZonedDateTime.from(df.withZone(zoneId).parse(dateTime)).toInstant().toEpochMilli() / 1000 * 1000;
    }

    /**
     * 字符串转时间戳
     * 时间字符串格式 yyyy-MM-dd HH:mm:ss
     * 时区系统默认时区
     *
     * @param dateTime 日期时间字符串
     * @return 时间戳
     */
    public static Long stringToLong(String dateTime) {
        final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ZonedDateTime.from(df.withZone(ZoneId.systemDefault()).parse(dateTime)).toInstant().toEpochMilli() / 1000 * 1000;
    }

    /**
     * 时间戳转时间
     *
     * @param dateTime 日期时间字符串
     * @param zoneId   区域偏移
     * @param pattern  时间表达式
     * @return 时间字符串
     */
    public static String longToString(Long dateTime, ZoneId zoneId, String pattern) {
        final DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateTime), zoneId));
    }

    /**
     * 时间戳转时间
     * 时间字符串格式 yyyy-MM-dd HH:mm:ss
     * 时区系统默认时区
     *
     * @param dateTime 日期时间字符串
     * @return 时间字符串
     */
    public static String longToString(Long dateTime) {
        final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateTime), ZoneId.systemDefault()));
    }

    /**
     * 获取当天开始时间戳
     *
     * @param zoneId 区域偏移
     * @return 当天时间戳
     */
    public static Long getStartTimeOfDay(ZoneId zoneId) {
        final ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        return zonedDateTime.withHour(0).withMinute(0).withSecond(0).toInstant().toEpochMilli() / 1000 * 1000;
    }

    /**
     * 获取当天开始时间戳
     *
     * @return 当天时间戳
     */
    public static Long getStartTimeOfDay() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        return zonedDateTime.withHour(0).withMinute(0).withSecond(0).toInstant().toEpochMilli() / 1000 * 1000;
    }

    public static LocalTime stringToLocalTime(String time) {
        final DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(time, df);
    }

    public static String localDateTimeToString(LocalDateTime dateTime, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        final DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(dateTime);
    }
}
