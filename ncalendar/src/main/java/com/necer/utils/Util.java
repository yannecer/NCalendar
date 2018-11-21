package com.necer.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.TypedValue;

import com.necer.entity.Lunar;
import com.necer.entity.NDate;

import org.joda.time.LocalDate;
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

public class Util {


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
     * @param type  一周
     * @return
     */
    public static int getIntervalWeek(LocalDate date1, LocalDate date2, int type) {

        if (type == Attrs.MONDAY) {
            date1 = getMonFirstDayOfWeek(date1);
            date2 = getMonFirstDayOfWeek(date2);
        } else {
            date1 = getSunFirstDayOfWeek(date1);
            date2 = getSunFirstDayOfWeek(date2);
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

    /**
     * @param localDate 今天
     * @param type      300，周日，301周一
     * @return
     */
    public static List<NDate> getMonthCalendar(LocalDate localDate, int type) {

        LocalDate lastMonthDate = localDate.plusMonths(-1);//上个月
        LocalDate nextMonthDate = localDate.plusMonths(1);//下个月

        int days = localDate.dayOfMonth().getMaximumValue();//当月天数
        int lastMonthDays = lastMonthDate.dayOfMonth().getMaximumValue();//上个月的天数
        int firstDayOfWeek = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), 1).getDayOfWeek();//当月第一天周几
        int endDayOfWeek = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), days).getDayOfWeek();//当月最后一天周几

        List<NDate> dateList = new ArrayList<>();


        //周一开始的
        if (type == Attrs.MONDAY) {

            //周一开始的
            for (int i = 0; i < firstDayOfWeek - 1; i++) {
                LocalDate date = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 2));
                dateList.add(getNDate(date));
            }
            for (int i = 0; i < days; i++) {
                LocalDate date = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }
            for (int i = 0; i < 7 - endDayOfWeek; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }

        } else {
            //上个月
            if (firstDayOfWeek != 7) {
                for (int i = 0; i < firstDayOfWeek; i++) {
                    LocalDate date = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 1));
                    dateList.add(getNDate(date));
                }
            }
            //当月
            for (int i = 0; i < days; i++) {
                LocalDate date = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }
            //下个月
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0;
            }
            for (int i = 0; i < 6 - endDayOfWeek; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }
        }

        //某些年的2月份28天，又正好日历只占4行
        if (dateList.size() == 28) {
            for (int i = 0; i < 7; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }
        }
        return dateList;

    }


    /**
     * 周视图的数据
     *
     * @param localDate
     * @return
     */
    public static List<NDate> getWeekCalendar(LocalDate localDate, int type) {
        List<NDate> dateList = new ArrayList<>();

        if (type == Attrs.MONDAY) {
            localDate = getMonFirstDayOfWeek(localDate);
        } else {
            localDate = getSunFirstDayOfWeek(localDate);
        }

        for (int i = 0; i < 7; i++) {
            LocalDate date = localDate.plusDays(i);
            dateList.add(getNDate(date));
        }
        return dateList;
    }


    public static List<String> getHolidayList() {
        return HolidayUtil.holidayList;
    }

    public static List<String> getWorkdayList() {
        return HolidayUtil.workdayList;
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


    public static NDate getNDate(LocalDate localDate) {
        NDate nDate = new NDate();

        int solarYear = localDate.getYear();
        int solarMonth = localDate.getMonthOfYear();
        int solarDay = localDate.getDayOfMonth();

        Lunar lunar = LunarUtil.getLunar(solarYear, solarMonth, solarDay);

        nDate.lunar = lunar;
        nDate.localDate = localDate;
        nDate.solarTerm = SolarTermUtil.getSolatName(solarYear, solarMonth < 10 ? ("0" + solarMonth) : (solarMonth + "") + solarDay);
        nDate.solarHoliday = HolidayUtil.getSolarHoliday(solarYear, solarMonth, solarDay);
        nDate.lunarHoliday = HolidayUtil.getLunarHoliday(lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay);

        return nDate;
    }
}
