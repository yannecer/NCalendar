package com.necer.ncalendar.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.TypedValue;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
    public static boolean isEqualsMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonthOfYear() == date2.getMonthOfYear();
    }


    /**
     * 第一个是不是第二个的上一个月,只在此处有效
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isLastMonth(LocalDate date1, LocalDate date2) {
        LocalDate date = date2.plusMonths(-1);
        return date1.getMonthOfYear() == date.getMonthOfYear();
    }


    /**
     * 第一个是不是第二个的下一个月，只在此处有效
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isNextMonth(LocalDate date1, LocalDate date2) {
        LocalDate date = date2.plusMonths(1);
        return date1.getMonthOfYear() == date.getMonthOfYear();
    }


    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    public static int getIntervalMonths(LocalDate date1, LocalDate date2) {
        date1 = date1.withDayOfMonth(1);
        date2 = date2.withDayOfMonth(1);

        return Months.monthsBetween(date1, date2).getMonths();
    }

    /**
     * 获得两个日期距离几周
     *
     * @param date1
     * @param date2
     * @param type      一周
     * @return
     */
    public static int getIntervalWeek(LocalDate date1, LocalDate date2, int type) {

        if (type == 0) {
            date1 = getSunFirstDayOfWeek(date1);
            date2 = getSunFirstDayOfWeek(date2);
        } else {
            date1 = getMonFirstDayOfWeek(date1);
            date2 = getMonFirstDayOfWeek(date2);
        }

        return Weeks.weeksBetween(date1, date2).getWeeks();

    }


    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(LocalDate date) {
        return new LocalDate().equals(date);

    }

    public static NCalendar getMonthCalendar(LocalDate date) {
        LocalDate lastMonthDate = date.plusMonths(-1);//上个月
        LocalDate nextMonthDate = date.plusMonths(1);//下个月

        int firstDayOfWeek = getFirstDayOfWeekOfMonth(date.getYear(), date.getMonthOfYear());//该月的第一天是星期几
        int lastMonthDays = lastMonthDate.dayOfMonth().getMaximumValue();//上月天数
        int days = date.dayOfMonth().getMaximumValue();//当月天数

        NCalendar nCalendar = new NCalendar();
        List<LocalDate> dateList = new ArrayList<>();
        List<String> lunarList = new ArrayList<>();

        int j = 1;
        for (int i = 0; i < 42; i++) {
            LocalDate date1 = null;
            if (i < firstDayOfWeek) { // 前一个月
                int temp = lastMonthDays - firstDayOfWeek + 1;
                date1 = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), (temp + i));
            } else if (i < days + firstDayOfWeek) { // 本月
                date1 = new LocalDate(date.getYear(), date.getMonthOfYear(), (i - firstDayOfWeek + 1));
            } else { // 下一个月
                date1 = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), j);
                j++;
            }
            dateList.add(date1);
            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
            String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
            lunarList.add(lunarDayString);
        }

        nCalendar.dateList = dateList;
        nCalendar.lunarList = lunarList;
        return nCalendar;
    }


    /**
     * @param date 今天
     * @param type     0，周日，1周一
     * @return
     */
    public static NCalendar getMonthCalendar2(LocalDate date, int type) {

        LocalDate lastMonthDate = date.plusMonths(-1);//上个月
        LocalDate nextMonthDate = date.plusMonths(1);//下个月

        int days = date.dayOfMonth().getMaximumValue();//当月天数
        int lastMonthDays = lastMonthDate.dayOfMonth().getMaximumValue();//上个月的天数

        int firstDayOfWeek = new LocalDate(date.getYear(), date.getMonthOfYear(), 1).getDayOfWeek();//当月第一天周几

        int endDayOfWeek = new LocalDate(date.getYear(), date.getMonthOfYear(), days).getDayOfWeek();//当月最后一天周几

        NCalendar nCalendar = new NCalendar();
        List<LocalDate> dates = new ArrayList<>();
        List<String> lunarList = new ArrayList<>();

        //周日开始的
        if (type == 0) {
            //上个月
            if (firstDayOfWeek != 7) {
                for (int i = 0; i < firstDayOfWeek; i++) {
                    LocalDate date1 = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 1));
                    dates.add(date1);
                    LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
                    String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                    lunarList.add(lunarDayString);
                }
            }
            //当月
            for (int i = 0; i < days; i++) {
                LocalDate date1 = new LocalDate(date.getYear(), date.getMonthOfYear(), i + 1);
                dates.add(date1);
                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
            //下个月
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0;
            }
            for (int i = 0; i < 6 - endDayOfWeek; i++) {
                LocalDate date1 = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dates.add(date1);
                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
        } else {
            //周一开始的
            for (int i = 0; i < firstDayOfWeek - 1; i++) {
                LocalDate date1 = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 2));
                dates.add(date1);

                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
            for (int i = 0; i < days; i++) {
                LocalDate date1 = new LocalDate(date.getYear(), date.getMonthOfYear(), i + 1);
                dates.add(date1);

                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
            for (int i = 0; i < 7 - endDayOfWeek; i++) {
                LocalDate date1 = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dates.add(date1);

                LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
                String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                lunarList.add(lunarDayString);
            }
        }

        nCalendar.dateList = dates;
        nCalendar.lunarList = lunarList;
        return nCalendar;

    }


    /**
     * 某月第一天是周几
     *
     * @return
     */
    public static int getFirstDayOfWeekOfMonth(int year, int month) {
        int dayOfWeek = new LocalDate(year, month, 1).getDayOfWeek();
        if (dayOfWeek == 7) {
            return 0;
        }
        return dayOfWeek;
    }

    public static NCalendar getWeekCalendar2(LocalDate date, int type) {
        List<LocalDate> dateList = new ArrayList<>();
        List<String> lunarStringList = new ArrayList<>();
        List<String> localDateList = new ArrayList<>();

        if (type == 0) {
            date = getSunFirstDayOfWeek(date);
        } else {
            date = getMonFirstDayOfWeek(date);
        }

        NCalendar calendar = new NCalendar();
        for (int i = 0; i < 7; i++) {
            LocalDate date1 = date.plusDays(i);

            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
            String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);

            dateList.add(date1);
            localDateList.add(date1.toString());
            lunarStringList.add(lunarDayString);
        }
        calendar.dateList = dateList;
        calendar.lunarList = lunarStringList;
        return calendar;
    }


    /**
     * 周视图的数据
     *
     * @param date
     * @return
     */
    public static NCalendar getWeekCalendar(LocalDate date) {
        List<LocalDate> dateList = new ArrayList<>();
        List<String> lunarStringList = new ArrayList<>();

        date = getSunFirstDayOfWeek(date);
        NCalendar calendar = new NCalendar();
        for (int i = 0; i < 7; i++) {
            LocalDate date1 = date.plusDays(i);

            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth()));
            String lunarDayString = LunarCalendarUtils.getLunarDayString(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth(), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);

            dateList.add(date1);
            lunarStringList.add(lunarDayString);
        }
        calendar.dateList = dateList;
        calendar.lunarList = lunarStringList;
        return calendar;
    }

    //转化一周从周日开始
    public static LocalDate getSunFirstDayOfWeek(LocalDate date) {
        if (date.dayOfWeek().get() == 7) {
            return date;
        } else {
            return date.minusWeeks(1).withDayOfWeek(7);
        }
    }

    //转化一周从周一开始
    public static LocalDate getMonFirstDayOfWeek(LocalDate date) {
        return date.dayOfWeek().withMinimumValue();
    }


    //包含农历,公历,格式化的日期
    public static class NCalendar {
        public List<LocalDate> dateList;
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
