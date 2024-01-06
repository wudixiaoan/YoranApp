package com.ph.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public class Format {
        public static final String date_sdf = "yyyy-MM-dd";
        public static final String yyyyMMdd = "yyyyMMdd";
        public static final String yyyyMMddHHmm = "yyyyMMddHHmm";
        public static final String date_sdf_wz = "yyyy年MM月dd日";
        public static final String time_sdf = "yyyy-MM-dd HH:mm";
        public static final String yyyymmddhhmmss = "yyyyMMddHHmmss";
        public static final String yyyymmddhhmmssSSS = "yyyyMMddHHmmssSSS";
        public static final String short_time_sdf = "HH:mm:ss";
        public static final String HHmm = "HH:mm";
        public static final String datetimeFormat = "yyyy-MM-dd HH:mm:ss.SSS";
        public static final String datetimeFormat2 = "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * 毫秒转化为时分秒
     *
     * @param duration
     * @return
     */
    public static String formatDuration(long duration) {
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }


    /**
     * String转long
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * string转date
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * date转long
     *
     * @param date
     * @return
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    /**
     * long转String
     *
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * long转date
     *
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }
    /**
     * date转String
     *
     * @param data
     * @param formatType
     * @return
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    public static Date getDateBefore(int before) {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - before);
        return now.getTime();
    }

    /**
     * 获取当前时间
     *
     * @param formate
     * @return
     */
    public static String getNowTime(String formate) {
        final Date date = new Date(System.currentTimeMillis());
        final SimpleDateFormat dateFormat = new SimpleDateFormat(formate, Locale.getDefault());
        return dateFormat.format(date);
    }
}
