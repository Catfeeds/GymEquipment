package com.saiyi.libfast.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {

    /**
     * 格式化年月日
     */
    public static String formatYMD(long date) {
        return formatYMD(date, "/", "/");
    }

    /**
     * 格式化时分
     */
    public static String formatHM(long date) {
        return formatHM(date, ":");
    }

    /**
     * 格式化日期
     *
     * @param date        时间
     * @param delimiterHM 小时和分钟的分隔符
     */
    public static String formatHM(long date, String delimiterHM) {
        String format = "HH" + delimiterHM + "mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(date));
    }

    /**
     * 格式化日期
     *
     * @param date        时间
     * @param delimiterYM 年和月的分隔符
     * @param delimiterMD 月和日的分隔符
     */
    public static String formatYMD(long date, String delimiterYM, String delimiterMD) {
        String format = "yyyy" + delimiterYM + "MM" + delimiterMD + "dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(date));
    }

    /**
     * 格式化日期
     *
     * @param date        时间
     * @param delimiterYM 年和月的分隔符
     * @param delimiterMD 月和日的分隔符
     * @param delimiterDH 日和时的分隔符
     * @param delimiterHM 时和分的分隔符
     * @param delimiterMS 分和秒的分隔符
     */
    public static String formatDate(long date, String delimiterYM, String delimiterMD, String delimiterDH, String delimiterHM, String delimiterMS) {
        String format = "yyyy" + delimiterYM + "MM" + delimiterMD + "dd" + delimiterDH + "HH" + delimiterHM + "mm" + delimiterMS + "ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(date));
    }

    /**
     * 格式化日期
     *
     * @param date 时间
     */
    public static String formatDate(long date) {
        return formatDate(date, "", "", "", "", "");
    }

    public static String formatLogDate(long date) {
        return formatDate(date, "-", "-", " ", ":", ":");
    }

    /**
     * 格式化
     *
     * @param date 时间
     */
    public static String formatdate(long date) {
        String format = "yy-MM-dd-HH-mm-ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(date));
    }

    /**
     * 格式化
     *
     * @param date 时间
     */
    public static String formatDateByTime(long date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(date));
    }

    /**
     * 格式化
     *
     * @param date 时间
     */
    public static int minuteFromSecond(int date) {
        return date / 60;
    }

}
