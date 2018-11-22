package com.qcx.mini.utils;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by codeest on 16/8/13.
 */

public class DateUtil {
    public final static long DAY=24*60*60*1000;
    public final static long HOUR=60*60*1000;
    public final static long MINUTE=60*1000;
    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 通过字符串获取时间戳
     *
     * @param time
     * @param pattern
     * @return
     */
    public static long getTimeFromString(String time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("zh", "CN"));
        try {
            Date d = sdf.parse(time);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取指定格式时间字符串
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String getTimeStr(long time, @Nullable String pattern) {
        pattern = pattern == null ? "yyyy-MM-dd HH:mm ss" : pattern;
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
        return df.format(time);
    }

    /**
     * 获取当前日期字符串
     *
     * @return
     */
    public static String getCurrentDateString(String pattern) {
        return getTimeStr(getCurrentTimeMillis(), pattern);
    }

    /**
     * 获取当前年
     *
     * @return
     */
    @SuppressLint("WrongConstant")
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     *
     * @return
     */
    @SuppressLint("WrongConstant")
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取当前日
     *
     * @return
     */
    @SuppressLint("WrongConstant")
    public static int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    //获取当天的开始时间
    public static java.util.Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
            return -10;
        }
    }

    public static String formatDay(String pattern,long time){
        long today=getDayBegin().getTime();
        int d=(int)((time-today)/DAY);
        String dayStr="";
        if(d==0){
            dayStr="今天";
        }else if(d==1){
            dayStr="明天";
        }else if(d==2){
            dayStr="后天";
        }else {
            SimpleDateFormat format=new SimpleDateFormat(pattern,Locale.CHINA);
            dayStr=format.format(time);
        }
        return dayStr;
    }
    public static String formatDayInWeek(long time){
        long today=DateUtil.getDayBegin().getTime();
        int d=(int)((time-today)/DAY);
        String dayStr="";
        if(d==0){
            dayStr="今天";
        }else if(d==1){
            dayStr="明天";
        }else if(d==2){
            dayStr="后天";
        }else {
            dayStr="周".concat(getWeek(time));
        }
        return dayStr;
    }

    //获取指定毫秒数的对应星期
    public static String getWeek(long millis) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        String week = "";
        int cweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (cweek) {
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            case 7:
                week = "六";
                break;
        }
        return week;

    }



    public static String formatTime2String(long showTime, boolean haveYear) {
        String str = "";
        long distance = (System.currentTimeMillis()  - showTime)/1000;
        if (distance < 300) {
            str = "5分钟内";
        } else if (distance >= 300 && distance < 600) {
            str = "5分钟前";
        } else if (distance >= 600 && distance < 1200) {
            str = "10分钟前";
        } else if (distance >= 1200 && distance < 1800) {
            str = "20分钟前";
        } else if (distance >= 1800 && distance < 2700) {
            str = "半小时前";
        } else if (distance >= 2700) {
            Date date = new Date(showTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            str = formatDateTime(sdf.format(date), haveYear);
        }
        return str;
    }


    @SuppressLint("WrongConstant")
    public static String formatDateTime(String time, boolean haveYear) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time == null) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        Calendar yesterday = Calendar.getInstance();
        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);
        if (current.after(today)) {
            return "今天 " + time.split(" ")[1];
        } else if (current.before(today) && current.after(yesterday)) {
            return "昨天 " + time.split(" ")[1];
        } else {
            if (haveYear) {
                int index = time.indexOf(" ");
                return time.substring(0, index);
            } else {
                int yearIndex = time.indexOf("-") + 1;
                int index = time.indexOf(" ");
                return time.substring(yearIndex, time.length()).substring(0, index+1);
            }
        }
    }

    public static String getTime(long time,boolean showDay,boolean showHour,boolean showMinute,boolean showSecond){
        int day;
        int hour;
        int minute;
        int second;
        StringBuilder builder=new StringBuilder();
        day=(int)(time/DAY);
        if(showDay){
            builder.append(day);
            builder.append("天");
            time=time%DAY;
        }

        hour=(int)(time/HOUR);
        if(showHour){
            builder.append(hour);
            builder.append("小时");
            time=time%HOUR;
        }

        minute=(int)(time/MINUTE);
        if(showMinute){
            builder.append(minute);
            builder.append("分钟");
            time=time%MINUTE;
        }

        second=(int)(time/1000);
        if(showSecond){
            builder.append(second);
            builder.append("秒");
        }
        return builder.toString();
    }


}
