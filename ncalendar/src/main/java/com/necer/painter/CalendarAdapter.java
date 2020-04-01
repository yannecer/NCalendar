package com.necer.painter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.necer.R;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * @author necer
 */
public abstract class CalendarAdapter {


    /**
     * 获取日历item的View
     */
    public abstract View getCalendarItemView(Context context);


    /**
     * 绑定今天的数据
     *
     * @param calendarItemView     getCalendarItemView()方法获取的 View
     * @param localDate            今天的日期
     * @param totalCheckedDateList 所有选中日期的集合，
     *                             单选模式中，集合中最多只有一个元素，
     *                             多选模式中，可以有多个元素
     */
    public abstract void onBindToadyView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList);

    /**
     * 绑定当前月或周的日期
     *
     * @param calendarItemView     getCalendarItemView()方法获取的 View
     * @param localDate            月日历-> 当前页面月份中的每个日期（内部是循环处理）
     *                             周日历-> 当前页面一周中的每个日期（内部是循环处理）
     * @param totalCheckedDateList 所有选中日期的集合，
     *                             单选模式中，集合中最多只有一个元素，
     *                             多选模式中，可以有多个元素
     */
    public abstract void onBindCurrentMonthOrWeekView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList);

    /**
     * 绑定上下月的数据 周日历可不用实现
     *
     * @param calendarItemView     getCalendarItemView()方法获取的 View
     * @param localDate            月日历-> 当前页面上一月、下一月中的每个日期（内部是循环处理）
     *                             周日里-> 周日历无需实现此方法
     * @param totalCheckedDateList 所有选中日期的集合，
     *                             单选模式中，集合中最多只有一个元素，
     *                             多选模式中，可以有多个元素
     */
    public abstract void onBindLastOrNextMonthView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList);

    /**
     * 绑定不可用的日期数据，和方法setDateInterval(startFormatDate, endFormatDate)对应,
     * 如果没有使用setDateInterval设置日期范围 此方法不用实现
     *
     * @param calendarItemView getCalendarItemView()方法获取的 View
     * @param localDate        不可用的日期
     */
    public void onBindDisableDateView(View calendarItemView, LocalDate localDate) {

    }

}
