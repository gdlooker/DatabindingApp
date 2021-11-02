package com.example.dell.databindingapp.util;

import android.content.Context;

import com.example.dell.databindingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtils {
    public final static String DATEFORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public final static String DATEFORMAT_YMD = "yyyy-MM-dd";
    public final static String DATEFORMAT_YMDH = "yyyy-MM-dd HH";
    public final static String DATEFORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public final static String DATEFORMAT_Y = "yyyy";
    public final static String DATEFORMAT_MM = "MM";
    public final static String DATEFORMAT_DD = "dd";
    public final static String DATEFORMAT_HMS = "HH:mm:ss";
    public final static String DATEFORMAT_HM = "HH:mm";
    public final static String DATEFORMAT_HH = "HH";
    public final static String DATEFORMAT_mm = "mm";
    public final static String DATEFORMAT_ss = "ss";
    public final static String DATEFORMAT_YM = "yyyy年MM月";
    public final static String DATEFORMAT_YM_HM = "MM月dd日 HH:mm";
    public final static String DATEFORMAT_YYYMM = "yyyy/MM";
    public final static String DATEFORMAT_YYYMMDD = "yyyy/MM/dd HH:mm";

    public static int CALENDAR_FIRST_DAY_OF_WEEK = Calendar.SUNDAY;
    public static int MAX_WEEK_DAYS = 7;

    /**
     * 通过年份和月份得到当月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份1号位于周几?
     *
     * @param year  年份
     * @param month 月份，传入系统获取的
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取本周第一天和最后一天
     *
     * @param pattern 年月日格式（如yyyy-MM-dd）
     * @return 长度为2的字符串数组，0下标为本周第一天的年月日，1下标为本周最后一天的年月日
     */
    public static String[] getDayOfWeek(String pattern) {
        return getDayOfWeekOrMonthOrYear(pattern, Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取本月第一天和最后一天
     *
     * @param pattern 年月日格式（如yyyy-MM-dd）
     * @return 长度为2的字符串数组，0下标为本月第一天的年月日，1下标为本月最后一天的年月日
     */
    public static String[] getDayOfMonth(String pattern) {
        return getDayOfWeekOrMonthOrYear(pattern, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取本年第一天和最后一天
     *
     * @param pattern 年月日格式（如yyyy-MM-dd）
     * @return 长度为2的字符串数组，0下标为本年第一天的年月日，1下标为本年最后一天的年月日
     */
    public static String[] getDayOfYEAR(String pattern) {
        return getDayOfWeekOrMonthOrYear(pattern, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取本周或本月或今年的第一天和最后一天日期（年月日）
     *
     * @param pattern 年月日格式（如yyyy-MM-dd）
     * @param field   领域（本周Calendar.DAY_OF_WEEK，本月Calendar.DAY_OF_MONTH，本年Calendar.DAY_OF_YEAR）
     * @return 长度为2的字符串数组
     */
    private static String[] getDayOfWeekOrMonthOrYear(String pattern, int field) {
        String[] days = new String[2];
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);

        Calendar cal = getCalendar(CALENDAR_FIRST_DAY_OF_WEEK);
        cal.set(field, 1);
        days[0] = dateFormat.format(cal.getTime());

        cal.set(field, cal.getActualMaximum(field));
        days[1] = dateFormat.format(cal.getTime());

        return days;
    }

    /**
     * 获取指定日期的那一周的所有日期
     *
     * @param pattern 日期格式
     * @param time    日期
     * @return
     * @throws ParseException
     */
    public static String[] getDaysOfWeekForDate(String pattern, String time) {
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        try {
            Date date = dateFormat.parse(time);
            Calendar cal = getCalendar(CALENDAR_FIRST_DAY_OF_WEEK, date);
            int weekDays = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
            String[] days = new String[weekDays];
            for (int i = 0; i < weekDays; i++) {
                cal.set(Calendar.DAY_OF_WEEK, i + 1);
                days[i] = dateFormat.format(cal.getTime());
            }
            return days;
        } catch (ParseException e) {
            return new String[]{};
        }
    }

    /**
     * 获取指定日期的那一周的所有日期
     *
     * @param pattern 日期格式
     * @param time    日期
     * @return
     * @throws ParseException
     */
    public static List<String> getDayListOfWeekForDate(String pattern, String time) throws ParseException {
        String[] days = getDaysOfWeekForDate(pattern, time);
        List<String> dayList = new ArrayList<>();
        for (int i = 0; i < days.length; i++) {
            dayList.add(days[i]);
        }
        return dayList;
    }

    /**
     * 获取某一年中第几周的开始日期
     *
     * @param year
     * @param weekIndex
     * @return
     */
    public static String getFirstDayOfWeek(int year, int weekIndex, String pattern) {
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(CALENDAR_FIRST_DAY_OF_WEEK);  // 设置周几为每周的第一天
        cal.setMinimalDaysInFirstWeek(MAX_WEEK_DAYS);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.add(Calendar.DATE, weekIndex * 7);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        return dateFormat.format(cal.getTime());
    }

    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    private static Calendar getCalendar(int firstDayOfWeek) {
        return getCalendar(firstDayOfWeek, new Date());
    }

    private static Calendar getCalendar(int firstDayOfWeek, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(firstDayOfWeek);  // 设置周几为每周的第一天
        cal.setTime(date);
        return cal;
    }

    /**
     * 获取指定日期属于本年的第几周
     *
     * @param time    日期
     * @param pattern 日期的格式
     * @return
     * @throws ParseException
     */
    public static int getWeekOfYear(String pattern, String time) throws ParseException {
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        Date date = dateFormat.parse(time);

        Calendar cal = getCalendar(CALENDAR_FIRST_DAY_OF_WEEK, date);
        cal.setMinimalDaysInFirstWeek(MAX_WEEK_DAYS);

        int weeks = cal.get(Calendar.WEEK_OF_YEAR);
        int month = cal.get(Calendar.MONTH);
        //  JDK  think  2015-12-31  as  2016  1th  week 
        //如果月份是12月，且求出来的周数是第一周，说明该日期实质上是这一年的第53周，也是下一年的第一周  
        if (month >= 11 && weeks == 1) {
            weeks += 52;
        }
        return weeks;
    }

    /**
     * 根据时间戳获取指定格式的日期时间
     *
     * @param pattern
     * @param time
     * @return
     */
    public static String getFormatDate(String pattern, long time) {
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"2017-12-18 16:09:00"）返回时间戳
     *
     * @param dateFormatStr
     * @return
     */
    public static long getTimeMillis(String pattern, String dateFormatStr) {
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        try {
            Date date = dateFormat.parse(dateFormatStr);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前日期前几天或后几天的日期
     *
     * @param days 正数：往后几天，负数：往前几天
     * @return
     */
    public static String getFormatDateByDays(String pattern, int days) {
        Date date = new Date();
        Calendar cal = getCalendar(CALENDAR_FIRST_DAY_OF_WEEK, date);

        cal.add(Calendar.DATE, days);// 正数往后推, 负数往前移动 
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        return dateFormat.format(cal.getTime());
    }

    /**
     * 获取当前时间指定格式的日期时间
     *
     * @param pattern
     * @return
     */
    public static String getCurrFormatDate(String pattern) {
        return getFormatDate(pattern, System.currentTimeMillis());
    }

    /**
     * beforeDate和afterDate相差天数
     *
     * @param beforeDay
     * @param afterDay
     * @param pattern
     * @return
     */
    public static int betwweenDays(String beforeDay, String afterDay, String pattern) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(pattern);
        int days = 0;
        try {
            Date beforeDate = simpleDateFormat.parse(beforeDay);
            Date afterDate = simpleDateFormat.parse(afterDay);
            days = betweenDays(beforeDate, afterDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * beforeDate和afterDate相差天数
     *
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static int betweenDays(Date beforeDate, Date afterDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(beforeDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(afterDate);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) { // 不同年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {    // 闰年
                    timeDistance += 366;
                } else {   // 不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else { // 同一年
            return day2 - day1;
        }
    }

    /**
     * 出生日期字符串转化成Date对象
     *
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static int getAge(String strDate, String pattern) {
        try {
            Date birthDay = getSimpleDateFormat(pattern).parse(strDate);
            return getAge(birthDay);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 由出生日期获得年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            return -1;
//			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    public static String getCurrYearString() {
        return getFormatDate(DATEFORMAT_Y, System.currentTimeMillis());
    }

    public static int getCurrYearInteger() {
        String year = getCurrYearString();
        return Integer.valueOf(year);
    }

    public static String getCurrMonthString() {
        return getFormatDate(DATEFORMAT_MM, System.currentTimeMillis());
    }

    public static int getCurrMonthInteger() {
        String month = getCurrMonthString();
        return Integer.valueOf(month);
    }

    public static String getCurrDayString() {
        return getFormatDate("dd", System.currentTimeMillis());
    }

    public static int getCurrDayInteger() {
        String month = getCurrDayString();
        return Integer.valueOf(month);
    }

    public static String getCurrHourString() {
        return getFormatDate(DATEFORMAT_HH, System.currentTimeMillis());
    }

    public static int getCurrHourInteger() {
        String month = getCurrHourString();
        return Integer.valueOf(month);
    }

    public static String getCurrMinuteString() {
        return getFormatDate(DATEFORMAT_mm, System.currentTimeMillis());
    }

    public static int getCurrMinuteInteger() {
        String month = getCurrMinuteString();
        return Integer.valueOf(month);
    }

    public static String getCurrSecondString() {
        return getFormatDate(DATEFORMAT_ss, System.currentTimeMillis());
    }

    public static int getCurrSecondInteger() {
        String month = getCurrMinuteString();
        return Integer.valueOf(month);
    }

    public static void setCalendarFirstDayOfWeek(int calendarFirstDayOfWeek) {
        CALENDAR_FIRST_DAY_OF_WEEK = calendarFirstDayOfWeek;
    }

    /**
     *  * 返回指定pattern样的日期时间字符串。
     * <p>
     * <p>@param dt
     * <p>@param pattern
     * <p>@return 如果时间转换成功则返回结果，否则返回空字符串""
     * <p>@author 即时通讯网([url=http://www.52im.net]http://www.52im.net[/url])
     * <p>
     */
//
//    public static String getTimeString(Date dt, String pattern) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(pattern);//"yyyy-MM-dd HH:mm:ss"
//            sdf.setTimeZone(TimeZone.getDefault());
//            return sdf.format(dt);
//        } catch (Exception e) {
//            return "";
//        }
//    }

    /**
     * 仿照微信中的消息时间显示逻辑，将时间戳（单位：毫秒）转换为友好的显示格式.
     * <p>
     * <p>
     * 1）7天之内的日期显示逻辑是：今天、昨天(-1d)、前天(-2d)、星期？（只显示总计7天之内的星期数，即<=-4d）；<br>
     * <p>
     * 2）7天之外（即>7天）的逻辑：直接显示完整日期时间。
     *
     * @param context              上下文，需获取资源里的字符串（中英文处理）
     * @param srcDate              要处理的源日期时间对象
     * @param includeHourAndMinute true表示输出的格式里一定会包含“时间:分钟”，否则不包含（参考微信，不包含时分的情况，用于首页“消息”中显示时）
     * @return 输出格式形如：“10:30”、“昨天 12:04”、“前天 20:51”、“星期二”、“2019/2/21 12:09”等形式
     * @author 即时通讯网([url = http : / / www.52im.net]http : / / www.52im.net[ / url])
     * @since 4.5
     */
    public static String getTimeStringAutoShort(Context context, Date srcDate, boolean includeHourAndMinute) {
        String ret = "";
        try {
            GregorianCalendar gcCurrent = new GregorianCalendar();
            gcCurrent.setTime(new Date());
            int currentYear = gcCurrent.get(GregorianCalendar.YEAR);
            int currentMonth = gcCurrent.get(GregorianCalendar.MONTH) + 1;
            int currentDay = gcCurrent.get(GregorianCalendar.DAY_OF_MONTH);
            GregorianCalendar gcSrc = new GregorianCalendar();
            gcSrc.setTime(srcDate);
            int srcYear = gcSrc.get(GregorianCalendar.YEAR);
            int srcMonth = gcSrc.get(GregorianCalendar.MONTH) + 1;
            int srcDay = gcSrc.get(GregorianCalendar.DAY_OF_MONTH);
            // 要额外显示的时间分钟
            String timeExtraStr = (includeHourAndMinute ? " " + getFormatDate("HH:mm", srcDate.getTime()) : "");
            // 当年
            if (currentYear == srcYear) {
                long currentTimestamp = gcCurrent.getTimeInMillis();
                long srcTimestamp = gcSrc.getTimeInMillis();
                // 相差时间（单位：毫秒）
                long delta = (currentTimestamp - srcTimestamp);
                // 当天（月份和日期一致才是）
                if (currentMonth == srcMonth && currentDay == srcDay) {
                    // 时间相差60秒以内
//                    if (delta < 60 * 1000)
//                        ret = context.getString(R.string.just_now);
//                        // 否则当天其它时间段的，直接显示“时:分”的形式
//                    else
                    ret = getFormatDate("HH:mm", srcDate.getTime());
                }
                // 当年 && 当天之外的时间（即昨天及以前的时间）
                else {
                    // 昨天（以“现在”的时候为基准-1天）
                    GregorianCalendar yesterdayDate = new GregorianCalendar();
                    yesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -1);

                    // 前天（以“现在”的时候为基准-2天）
                    GregorianCalendar beforeYesterdayDate = new GregorianCalendar();
                    beforeYesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -2);

                    // 用目标日期的“月”和“天”跟上方计算出来的“昨天”进行比较，是最为准确的（如果用时间戳差值
                    // 的形式，是不准确的，比如：现在时刻是2019年02月22日1:00、而srcDate是2019年02月21日23:00，
                    // 这两者间只相差2小时，直接用“delta/(3600 * 1000)” > 24小时来判断是否昨天，就完全是扯蛋的逻辑了）
                    if (srcMonth == (yesterdayDate.get(GregorianCalendar.MONTH) + 1)
                            && srcDay == yesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
                        ret = context.getString(R.string.yesterday) + timeExtraStr;// -1d
                    }
                    // “前天”判断逻辑同上
                    else if (srcMonth == (beforeYesterdayDate.get(GregorianCalendar.MONTH) + 1)
                            && srcDay == beforeYesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
                        ret = context.getString(R.string.before_yesterday) + timeExtraStr;// -2d
                    } else {
                        // 跟当前时间相差的小时数
                        long deltaHour = (delta / (3600 * 1000));
                        // 如果小于 7*24小时就显示星期几
                        if (deltaHour < 7 * 24) {
                            String[] weekday = {context.getString(R.string.sunday), context.getString(R.string.monday),
                                    context.getString(R.string.tuesday), context.getString(R.string.wednesday),
                                    context.getString(R.string.thursday), context.getString(R.string.friday), context.getString(R.string.saturday)};
                            // 取出当前是星期几
                            String weekdayDesc = weekday[gcSrc.get(GregorianCalendar.DAY_OF_WEEK) - 1];
                            ret = weekdayDesc + timeExtraStr;
                        }
                        // 否则直接显示月日
                        else {
                            ret = getFormatDate("M/d", srcDate.getTime()) + timeExtraStr;
                        }
                    }
                }
            } else {
                ret = getFormatDate("yyyy/M/d", srcDate.getTime()) + timeExtraStr;
            }
        } catch (Exception e) {
            System.err.println("【DEBUG-getTimeStringAutoShort】计算出错：" + e.getMessage() + " 【NO】");
        }
        return ret;
    }

    public static String getTimeStringAutoShort(Context context, long timeMillis, boolean mustIncludeTime) {
        return getTimeStringAutoShort(context.getApplicationContext(), new Date(timeMillis), mustIncludeTime);
    }

    public static String getTimeStringAutoShort(Context context, long timeMillis, boolean mustIncludeTime, boolean isComment) {
        return getTimeStringAutoShort(context.getApplicationContext(), new Date(timeMillis), mustIncludeTime, isComment);
    }

    /**
     * 动态页：
     * 1. 3分钟以内：刚刚
     * 2. 60分钟内：n分钟以前
     * 3. 24小时内：n小时前
     * 4. 前一天：昨天 xx:xx
     * 5. 7天内：n天前
     * 6. 其他：xxxx-xx-xx xx:xx
     * 评论页：
     * 1. 3分钟以内：刚刚
     * 2. 60分钟内：n分钟以前
     * 3. 24小时内：xx:xx
     * 4. 其他：xxxx-xx-xx xx:xx
     *
     * @param context              上下文，需获取资源里的字符串（中英文处理）
     * @param srcDate              要处理的源日期时间对象
     * @param includeHourAndMinute true表示输出的格式里一定会包含“时间:分钟”，否则不包含（参考微信，不包含时分的情况，用于首页“消息”中显示时）
     * @param isComment            是否评论
     * @return 输出格式形如：“10:30”、“昨天 12:04”、“前天 20:51”、“星期二”、“2019/2/21 12:09”等形式
     * @author 即时通讯网([url = http : / / www.52im.net]http : / / www.52im.net[ / url])
     * @since 4.5
     */
    public static String getTimeStringAutoShort(Context context, Date srcDate, boolean includeHourAndMinute, boolean isComment) {
        String ret = "";
        try {
            GregorianCalendar gcCurrent = new GregorianCalendar();
            gcCurrent.setTime(new Date());
            int currentYear = gcCurrent.get(GregorianCalendar.YEAR);
            int currentMonth = gcCurrent.get(GregorianCalendar.MONTH) + 1;
            int currentDay = gcCurrent.get(GregorianCalendar.DAY_OF_MONTH);
            GregorianCalendar gcSrc = new GregorianCalendar();
            gcSrc.setTime(srcDate);
            int srcYear = gcSrc.get(GregorianCalendar.YEAR);
            int srcMonth = gcSrc.get(GregorianCalendar.MONTH) + 1;
            int srcDay = gcSrc.get(GregorianCalendar.DAY_OF_MONTH);
            // 要额外显示的时间分钟
            String timeExtraStr = (includeHourAndMinute ? " " + getFormatDate("HH:mm", srcDate.getTime()) : "");
            long currentTimestamp = gcCurrent.getTimeInMillis();
            long srcTimestamp = gcSrc.getTimeInMillis();
            // 相差时间（单位：毫秒）
            long delta = (currentTimestamp - srcTimestamp);
            // 当年
            if (currentYear == srcYear) {
                // 当天（月份和日期一致才是）
                if (currentMonth == srcMonth && currentDay == srcDay) {
                    //3分钟内显示刚刚
                    if (delta < 60 * 1000 * 3)
                        ret = context.getString(R.string.just_now);
                    else if (delta < 60l * 1000 * 60)  // 60分钟内显示n分钟前
                        ret = String.format(context.getString(R.string.just_min_ago), delta / (60 * 1000) + "");
                    else if (delta < 60l * 1000 * 60 * 24 || isComment)  // 当天，显示n小时前
                        if (isComment) {
                            ret = timeExtraStr;
                        } else {
                            ret = String.format(context.getString(R.string.just_hour_ago), delta / (60 * 1000 * 60) + "");
                        }
                    else {
                        ret = getFormatDate("yyyy-MM-dd HH:mm", srcDate.getTime());
                    }
                } else if (isComment) {
                    ret = getFormatDate("yyyy-MM-dd ", srcDate.getTime()) + timeExtraStr;
                } else {// 当年 && 当天之外的时间（即昨天及以前的时间）

                    // 昨天（以“现在”的时候为基准-1天）
                    GregorianCalendar yesterdayDate = new GregorianCalendar();
                    yesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -1);
//
                    // 7天内
                    GregorianCalendar before7dayDate = new GregorianCalendar();
                    before7dayDate.add(GregorianCalendar.DAY_OF_MONTH, -8);

                    // 用目标日期的“月”和“天”跟上方计算出来的“昨天”进行比较，是最为准确的（如果用时间戳差值
                    // 的形式，是不准确的，比如：现在时刻是2019年02月22日1:00、而srcDate是2019年02月21日23:00，
                    // 这两者间只相差2小时，直接用“delta/(3600 * 1000)” > 24小时来判断是否昨天，就完全是扯蛋的逻辑了）
                    if (srcMonth == (yesterdayDate.get(GregorianCalendar.MONTH) + 1)
                            && srcDay == yesterdayDate.get(GregorianCalendar.DAY_OF_MONTH) && !isComment) {
                        ret = context.getString(R.string.yesterday) + timeExtraStr;// -1d
                    } else if (srcMonth == (before7dayDate.get(GregorianCalendar.MONTH) + 1)
                            && srcDay > before7dayDate.get(GregorianCalendar.DAY_OF_MONTH) && !isComment) {
                        int count = 8 - srcDay + before7dayDate.get(GregorianCalendar.DAY_OF_MONTH);
                        if (count > 0) {
                            ret = String.format(context.getString(R.string.before_days_ago), count + "");// -2d
                        } else {
                            if(isComment){
                                ret = getFormatDate("yyyy-MM-dd ", srcDate.getTime()) + timeExtraStr;
                            }else{
                                ret = getFormatDate("yyyy年MM月dd日 ", srcDate.getTime()) + timeExtraStr;
                            }
                        }
                    } else {
                        if(isComment){
                            ret = getFormatDate("yyyy-MM-dd ", srcDate.getTime()) + timeExtraStr;
                        }else{
                            ret = getFormatDate("yyyy年MM月dd日 ", srcDate.getTime()) + timeExtraStr;
                        }
                    }
                }
            } else {
                if(isComment){
                    ret = getFormatDate("yyyy-MM-dd ", srcDate.getTime()) + timeExtraStr;
                }else{
                    ret = getFormatDate("yyyy年MM月dd日 ", srcDate.getTime()) + timeExtraStr;
                }
            }
        } catch (Exception e) {
            System.err.println("【DEBUG-getTimeStringAutoShort】计算出错：" + e.getMessage() + " 【NO】");
        }
        return ret;
    }
//        globalScope.launch(Dispatchers.IO){
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-01 16:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-25 16:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-24 23:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-23 21:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-22 21:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-21 21:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-20 21:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-19 03:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-18 03:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-17 03:27:59"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-26 06:30:13"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-26 16:00:13"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-26 17:14:00"),true,true)
//            DateUtils.getTimeStringAutoShort(context,DateUtils.getTimeMillis("yyyy-MM-dd HH:mm:ss","2021-01-26 17:17:00"),true,true)
//        }


}
