package com.necer.ncalendar.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.TypedValue;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2017/6/9.
 */

public class Utils {


    /**
     * dp转px
     *
     * @param context
     * @param
     * @return
     */
    public static float dp2px(Context context, int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static float sp2px(Context context, float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }


    /**
     * 屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    //是否同月
    public static boolean isEqualsMonth(DateTime dateTime1, DateTime dateTime2) {
        return dateTime1.getYear() == dateTime2.getYear() && dateTime1.getMonthOfYear() == dateTime2.getMonthOfYear();
    }


    /**
     * 第一个是不是第二个的上一个月,只在此处有效
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean isLastMonth(DateTime dateTime1, DateTime dateTime2) {
        DateTime dateTime = dateTime2.plusMonths(-1);
        return dateTime1.getMonthOfYear() == dateTime.getMonthOfYear();
    }


    /**
     * 第一个是不是第二个的下一个月，只在此处有效
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean isNextMonth(DateTime dateTime1, DateTime dateTime2) {
        DateTime dateTime = dateTime2.plusMonths(1);
        return dateTime1.getMonthOfYear() == dateTime.getMonthOfYear();
    }


    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    public static int getIntervalMonths(DateTime dateTime1, DateTime dateTime2) {
        dateTime1 = dateTime1.withDayOfMonth(1).withTimeAtStartOfDay();
        dateTime2 = dateTime2.withDayOfMonth(1).withTimeAtStartOfDay();

        return Months.monthsBetween(dateTime1, dateTime2).getMonths();
    }

    /**
     * 获得两个日期距离几周
     *
     * @param dateTime1
     * @param dateTime2
     * @param type      一周
     * @return
     */
    public static int getIntervalWeek(DateTime dateTime1, DateTime dateTime2, int type) {

        if (type == 0) {
            dateTime1 = getSunFirstDayOfWeek(dateTime1);
            dateTime2 = getSunFirstDayOfWeek(dateTime2);
        } else {
            dateTime1 = getMonFirstDayOfWeek(dateTime1);
            dateTime2 = getMonFirstDayOfWeek(dateTime2);
        }

        return Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();

    }


    /**
     * 是否是今天
     *
     * @param dateTime
     * @return
     */
    public static boolean isToday(DateTime dateTime) {
        return new DateTime().withTimeAtStartOfDay().equals(dateTime);

    }

    public static NCalendar getMonthCalendar(DateTime dateTime) {
        DateTime lastMonthDateTime = dateTime.plusMonths(-1);//上个月
        DateTime nextMonthDateTime = dateTime.plusMonths(1);//下个月

        int firstDayOfWeek = getFirstDayOfWeekOfMonth(dateTime.getYear(), dateTime.getMonthOfYear());//该月的第一天是星期几
        int lastMonthDays = lastMonthDateTime.dayOfMonth().getMaximumValue();//上月天数
        int days = dateTime.dayOfMonth().getMaximumValue();//当月天数

        NCalendar nCalendar = new NCalendar();
        List<DateTime> dateTimeList = new ArrayList<>();
        List<String> lunarList = new ArrayList<>();

        int j = 1;
        for (int i = 0; i < 42; i++) {
            DateTime dateTime1 = null;
            if (i < firstDayOfWeek) { // 前一个月
                int temp = lastMonthDays - firstDayOfWeek + 1;
                dateTime1 = new DateTime(lastMonthDateTime.getYear(), lastMonthDateTime.getMonthOfYear(), (temp + i), 0, 0, 0);
            } else if (i < days + firstDayOfWeek) { // 本月
                dateTime1 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), (i - firstDayOfWeek + 1), 0, 0, 0);
            } else { // 下一个月
                dateTime1 = new DateTime(nextMonthDateTime.getYear(), nextMonthDateTime.getMonthOfYear(), j, 0, 0, 0);
                j++;
            }
            dateTimeList.add(dateTime1);
            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
            String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
            lunarList.add(lunarDayString);
        }

        nCalendar.dateTimeList = dateTimeList;
        nCalendar.lunarList = lunarList;
        return nCalendar;
    }


    /**
     * @param dateTime 今天
     * @param type     0，周日，1周一
     * @return
     */
    public static NCalendar getMonthCalendar2(DateTime dateTime, int type) {

        DateTime lastMonthDateTime = dateTime.plusMonths(-1);//上个月
        DateTime nextMonthDateTime = dateTime.plusMonths(1);//下个月

        int days = dateTime.dayOfMonth().getMaximumValue();//当月天数
        int lastMonthDays = lastMonthDateTime.dayOfMonth().getMaximumValue();//上个月的天数

        int firstDayOfWeek = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0, 0).getDayOfWeek();//当月第一天周几

        int endDayOfWeek = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), days, 0, 0, 0).getDayOfWeek();//当月最后一天周几

        NCalendar nCalendar = new NCalendar();
        List<DateTime> dateTimes = new ArrayList<>();
        List<String> lunarList = new ArrayList<>();

        //周日开始的
        if (type == 0) {
            //上个月
            if (firstDayOfWeek != 7) {
                for (int i = 0; i < firstDayOfWeek; i++) {
                    DateTime dateTime1 = new DateTime(lastMonthDateTime.getYear(), lastMonthDateTime.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 1), 0, 0, 0);
                    dateTimes.add(dateTime1);
                    LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
                    String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                    lunarList.add(lunarDayString);
                }
            }
            //当月
            for (int i = 0; i < days; i++) {
                DateTime dateTime1 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), i + 1, 0, 0, 0);
                dateTimes.add(dateTime1);
                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
            //下个月
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0;
            }
            for (int i = 0; i < 6 - endDayOfWeek; i++) {
                DateTime dateTime1 = new DateTime(nextMonthDateTime.getYear(), nextMonthDateTime.getMonthOfYear(), i + 1, 0, 0, 0);
                dateTimes.add(dateTime1);
                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
        } else {
            //周一开始的
            for (int i = 0; i < firstDayOfWeek - 1; i++) {
                DateTime dateTime1 = new DateTime(lastMonthDateTime.getYear(), lastMonthDateTime.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 2), 0, 0, 0);
                dateTimes.add(dateTime1);

                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
            for (int i = 0; i < days; i++) {
                DateTime dateTime1 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), i + 1, 0, 0, 0);
                dateTimes.add(dateTime1);

                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
            for (int i = 0; i < 7 - endDayOfWeek; i++) {
                DateTime dateTime1 = new DateTime(nextMonthDateTime.getYear(), nextMonthDateTime.getMonthOfYear(), i + 1, 0, 0, 0);
                dateTimes.add(dateTime1);

                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
        }

        nCalendar.dateTimeList = dateTimes;
        nCalendar.lunarList = lunarList;
        return nCalendar;

    }


    /**
     * 某月第一天是周几
     *
     * @return
     */
    public static int getFirstDayOfWeekOfMonth(int year, int month) {
        int dayOfWeek = new DateTime(year, month, 1, 0, 0, 0).getDayOfWeek();
        if (dayOfWeek == 7) {
            return 0;
        }
        return dayOfWeek;
    }

    public static NCalendar getWeekCalendar2(DateTime dateTime, int type) {
        List<DateTime> dateTimeList = new ArrayList<>();
        List<String> lunarStringList = new ArrayList<>();
        List<String> localDateList = new ArrayList<>();

        if (type == 0) {
            dateTime = getSunFirstDayOfWeek(dateTime);
        } else {
            dateTime = getMonFirstDayOfWeek(dateTime);
        }

        NCalendar calendar = new NCalendar();
        for (int i = 0; i < 7; i++) {
            DateTime dateTime1 = dateTime.plusDays(i);

            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
            String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);

            dateTimeList.add(dateTime1);
            localDateList.add(dateTime1.toLocalDate().toString());
            lunarStringList.add(lunarDayString);
        }
        calendar.dateTimeList = dateTimeList;
        calendar.lunarList = lunarStringList;
        return calendar;
    }


    /**
     * 周视图的数据
     *
     * @param dateTime
     * @return
     */
    public static NCalendar getWeekCalendar(DateTime dateTime) {
        List<DateTime> dateTimeList = new ArrayList<>();
        List<String> lunarStringList = new ArrayList<>();

        dateTime = getSunFirstDayOfWeek(dateTime);
        NCalendar calendar = new NCalendar();
        for (int i = 0; i < 7; i++) {
            DateTime dateTime1 = dateTime.plusDays(i);

            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth()));
            String lunarDayString = LunarCalendarUtils.getLunarDayString(dateTime1.getYear(), dateTime1.getMonthOfYear(), dateTime1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);

            dateTimeList.add(dateTime1);
            lunarStringList.add(lunarDayString);
        }
        calendar.dateTimeList = dateTimeList;
        calendar.lunarList = lunarStringList;
        return calendar;
    }

    //转化一周从周日开始
    public static DateTime getSunFirstDayOfWeek(DateTime dateTime) {
        if (dateTime.dayOfWeek().get() == 7) {
            return dateTime;
        } else {
            return dateTime.minusWeeks(1).withDayOfWeek(7);
        }
    }

    //转化一周从周一开始
    public static DateTime getMonFirstDayOfWeek(DateTime dateTime) {
        return dateTime.dayOfWeek().withMinimumValue();
    }


    //包含农历,公历,格式化的日期
    public static class NCalendar {
        public List<DateTime> dateTimeList;
        public List<String> lunarList;
    }


    private static List<String> holidayList;
    private static List<String> workdayList;


    public static void initHoliday(Context context) {
        String holidayJson = getHolidayJson(context);

        try {
            JSONObject jsonObject = new JSONObject(holidayJson);

            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray dataArray = data.getJSONArray("data");

            holidayList = new ArrayList<>();
            workdayList = new ArrayList<>();

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject jsonObject1 = dataArray.getJSONObject(i);
                String date = jsonObject1.getString("date");
                int val = jsonObject1.getInt("val");
                if (val == 2 || val == 3) {
                    holidayList.add(date);
                } else {
                    workdayList.add(date);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public static List<String> getHolidayList(Context context) {

        if (holidayList == null) {
            initHoliday(context);
        }
        return holidayList;
    }

    public static List<String> getWorkdayList(Context context) {

        if (workdayList == null) {
            initHoliday(context);
        }
        return workdayList;
    }


    public static String getHolidayJson(Context context) {
        String json = null;
        try {
            AssetManager asset = context.getAssets();
            InputStream in = asset.open("holiday.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(in, "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer("");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");

            }
            inputStreamReader.close();
            json = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


}
