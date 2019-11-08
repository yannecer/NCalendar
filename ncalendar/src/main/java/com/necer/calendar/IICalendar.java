package com.necer.calendar;

import com.necer.enumeration.CalendarState;
import com.necer.listener.OnCalendarScrollingListener;
import com.necer.listener.OnCalendarStateChangedListener;

/**
 * 折叠日历特有的功能接口
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
     * 设置周 滑动到周位置固定
     *
     * @param isWeekHoldEnable
     */
    void setWeekHoldEnable(boolean isWeekHoldEnable);

    /**
     * 设置月状态下 下拉拉伸
     *
     * @param isMonthStretchEnable
     */
    void setMonthStretchEnable(boolean isMonthStretchEnable);

    /**
     * 日历月周状态变化回调
     *
     * @param onCalendarStateChangedListener
     */
    void setOnCalendarStateChangedListener(OnCalendarStateChangedListener onCalendarStateChangedListener);

    /**
     * 日历 月 周 拉伸 状态滑动监听
     *
     * @param onCalendarScrollingListener
     */
    void setOnCalendarScrollingListener(OnCalendarScrollingListener onCalendarScrollingListener);

    /**
     * 设置日历状态
     *
     * @param calendarState
     */
    void setCalendarState(CalendarState calendarState);

    /**
     * 获取当前日历的状态
     *
     * @return CalendarState.MONTH==月视图  CalendarState.WEEK==周视图  CalendarState.MONTH_STRETCH==日历拉伸状态
     */
    CalendarState getCalendarState();


}
