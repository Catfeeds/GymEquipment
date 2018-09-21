package com.saiyi.gymequipment.common.tools;

import android.text.TextUtils;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.utils.Arith;
import com.saiyi.libfast.utils.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StringUtils {


    /**
     * 更改时间格式
     *
     * @param time long類型的
     * @return MM月dd日 HH:mm
     */
    public static String fromDateString(long time) {
        String date = DateUtils.formatdate(time);
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        String[] dates = date.split("-");
        if (dates.length < 6) return null;
        for (int i = 1; i < dates.length - 3; i++) {
            if (dates[i].length() < 2) break;
            if (dates[i].substring(0, 1).equals("0")) {
                dates[i] = dates[i].substring(1, 2);
            }
        }
        return dates[1] + GymApplication.getContext().getResources().getString(R.string.month) + dates[2] + GymApplication.getContext().getResources().getString(R.string.day) + " " + dates[3] + ":" + dates[4];
    }

    /**
     * 输出某月某日格式
     *
     * @param date
     * @return
     */
    public static String fromDateToMMdd(long date) {
        String[] dates = fromDateString(date).split(" ");
        if (dates == null) return null;
        return dates[0];
    }

    /**
     * 输出某年某月格式
     * yyyy月-MM日
     *
     * @param time
     * @return
     */
    public static String fromDateToyyyyMM(long time) {
        String date = DateUtils.formatYMD(time, "/", "/");
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        String[] dates = date.split("/");
        if (dates[1].substring(0, 1).equals("0")) {
            dates[1] = dates[1].substring(1, 2);
        }
        String d = dates[0] + GymApplication.getContext().getResources().getString(R.string.year) + "-" + dates[1] + GymApplication.getContext().getResources().getString(R.string.month);
        return d;
    }

    /**
     * 输出某时某分格式
     * HH:mm
     *
     * @param date
     * @return
     */
    public static String fromDateToHHmm(long date) {
        String[] dates = fromDateString(date).split(" ");
        if (dates == null) return null;
        if (dates.length < 2) return null;
        return dates[1];
    }


    /**
     * 秒转时分秒
     *
     * @param time 单位s
     * @return HH:mm:ss
     */
    public static String secToTime(int time) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        hour = time / 3600;
        minute = time / 60 % 60;
        second = time % 60;
        if (time <= 0)
            return "00:00:00";
        else {
            return unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
        }
    }

    /**
     * 將個位數前面加0
     *
     * @param i
     * @return
     */
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String secToMinute(double sec) {
        return (int) Math.ceil(sec / 60) + "";
    }

    public static String secToHour(double sec) {
        double d = sec / (60 * 60);
        BigDecimal bg = new BigDecimal(d).setScale(1, RoundingMode.UP);
        return bg.doubleValue() + "";
    }

    /**
     * 将string转为日期的long类型
     *
     * @param date 2017-12-12
     * @return
     */
    public static long stringDateTolong(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return d.getTime();
    }

    /**
     * 获取本月的到当日的日期
     *
     * @return
     */
    public static List<String> getDayListOfMonth(long date) {
        List<String> list = new ArrayList();
        Calendar aCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String cDate = dateFormat.format(new Date(date));
        aCalendar.setTime(new Date(date));
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH) + 1;//月份
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String aDate = String.valueOf(year) + "-" + unitFormat(month) + "-" + StringUtils.unitFormat(i);
            list.add(aDate);
            if (aDate.equals(cDate)) {
                break;
            }
        }
        return list;
    }

    /**
     * 获取到当前日期的本周日期list
     * 例：2018.5.16 是星期三， list包含2018.5.14-2018.5.20的日期
     *
     * @param date
     * @return
     */
    public static List<Date> getWeekDate(long date) {
        String yz_time = getTimeInterval(new Date(date));
        String array[] = yz_time.split(",");
        String start_time = array[0];//本周第一天
        String end_time = array[1];  //本周最后一天
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dBegin = sdf.parse(start_time);
            Date dEnd = sdf.parse(end_time);
            return findDates(dBegin, dEnd);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取本周的开始和结束时间
     * 例：今天是5月16星期三，返回5/14-5/20
     *
     * @param date
     * @return
     */
    public static String getWeekBeginEnd(long date) {
        String yz_time = getTimeInterval(new Date(date));
        String array[] = yz_time.split(",");
        String start_time = array[0];//本周第一天
        String end_time = array[1];  //本周最后一天
        return start_time.substring(5, start_time.length()).replace("-", "/") + "-" + end_time.substring(5, end_time.length()).replace("-", "/");
    }

    private static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }


    private static String getTimeInterval(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin + "," + imptimeEnd;
    }

    /**
     * 根据距离和时间计算配速
     *
     * @param distance 距离 单位米
     * @param time     时间 单位秒
     * @return 配速
     */
    public static String getSpeedDistribution(double distance, int time) {
        if (distance == 0) {
            return "0'0''";
        }

        if (distance > 0) {
            double m = Arith.divide(time, distance);
            int minute = (int) Arith.divide(m, 60);
            double second = m % 60;
            int ye1 = (int) Math.ceil(second);
            String speed = minute + "'" + ye1 + "''";
            return speed;
        }
        return null;
    }
}
