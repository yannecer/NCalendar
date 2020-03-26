package com.necer.painter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.necer.R;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

import java.util.List;

public abstract class CalendarAdapter {


    /**
     * 获取日历背景View，可用于做背景渐变等，不需要可不用实现
     *
     * @param context
     * @return
     */
    public View getCalendarBackgroundView(Context context) {
        return null;
    }

    /**
     * 绑定日历背景View，不需要可不用实现
     *
     * @param iCalendarView          ICalendarView 日历页面，可判断是月日历或者周日历
     * @param calendarBackgroundView 日历View，即是getCalendarBackgroundView获取的view
     * @param localDate
     * @param totalDistance          滑动的全部距离
     * @param currentDistance        当前位置的距离
     */
    public void onBindCalendarBackgroundView(ICalendarView iCalendarView, View calendarBackgroundView, LocalDate localDate, int totalDistance, int currentDistance) {

    }

    /**
     * 获取日历item的View
     *
     * @param context
     * @return
     */
    public abstract View getCalendarItemView(Context context);


    /**
     * 绑定今天的数据
     *
     * @param calendarItemView
     * @param localDate
     * @param totalCheckedDateList
     */
    public abstract void onBindToadyView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList);

    /**
     * 绑定除今天的当月数据
     *
     * @param calendarItemView
     * @param localDate
     * @param totalCheckedDateList
     */
    public abstract void onBindCurrentMonthOrWeekView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList);

    /**
     * 绑定上下月的数据 周日历可不用实现
     *
     * @param calendarItemView
     * @param localDate
     * @param totalCheckedDateList
     */
    public abstract void onBindLastOrNextMonthView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList);

    /**
     * 绑定不可用的日期数据，和方法setDateInterval(startFormatDate, endFormatDate)对应,
     * 如果没有使用setDateInterval设置日期范围 此方法不用实现
     *
     * @param calendarItemView
     * @param localDate
     */
    public void onBindDisableDateView(View calendarItemView, LocalDate localDate) {

    }

}
