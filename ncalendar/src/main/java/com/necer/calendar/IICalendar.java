package com.necer.calendar;

import com.necer.enumeration.CalendarState;
import com.necer.listener.OnCalendarScrollingListener;
import com.necer.listener.OnCalendarStateChangedListener;
import com.necer.painter.CalendarBackground;

/**
 * 折叠日历特有的功能接口
 * @author necer
 */
public interface IICalendar extends ICalendar {

    /**
     * 回到周状态 只能从月->周
     */
    void toWeek();

    /**
     * 回到月状态 可以从周回到月或者从拉伸回到周
     */
    void toMonth();

    /**
     * 回到拉伸状态 只能从月->拉伸
     */
    void toStretch();

    /**
     * 设置是否滑动到周位置固定
     */
    void setWeekHoldEnable(boolean isWeekHoldEnable);

    /**
     * 设置月状态下 是否可以下拉拉伸
     */
    void setStretchCalendarEnable(boolean isMonthStretchEnable);

    /**
     * 日历月周状态变化回调
     */
    void setOnCalendarStateChangedListener(OnCalendarStateChangedListener onCalendarStateChangedListener);

    /**
     * 日历 月 周 拉伸 状态滑动监听
     */
    void setOnCalendarScrollingListener(OnCalendarScrollingListener onCalendarScrollingListener);

    /**
     * 设置日历状态
     *
     * @param calendarState WEEK 周
     *                      MONTH 月
     *                      MONTH_STRETCH 向下拉伸
     */
    void setCalendarState(CalendarState calendarState);

    /**
     * 获取当前日历的状态
     *
     * @return CalendarState.MONTH==月视图
     * CalendarState.WEEK==周视图
     * CalendarState.MONTH_STRETCH==日历拉伸状态
     */
    CalendarState getCalendarState();


    /**
     * 月周折叠日历设置月日历背景
     *
     * @param calendarBackground 实现了CalendarBackground接口的日历背景
     */
    void setMonthCalendarBackground(CalendarBackground calendarBackground);

    /**
     * 月周折叠日历设置周日历背景
     *
     * @param calendarBackground 实现了CalendarBackground接口的日历背景
     */
    void setWeekCalendarBackground(CalendarBackground calendarBackground);

}
