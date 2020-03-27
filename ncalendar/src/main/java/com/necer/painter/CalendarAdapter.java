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
