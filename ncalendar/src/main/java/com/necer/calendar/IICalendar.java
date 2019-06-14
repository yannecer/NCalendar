package com.necer.calendar;

import com.necer.listener.OnCalendarStateChangedListener;

/**
 * 折叠日历特有的功能接口
 */
public interface IICalendar extends ICalendar {

    //回到周状态
    void toWeek();

    //回到月状态
    void toMonth();

    //日历月周状态变化回调
    void setOnCalendarStateChangedListener(OnCalendarStateChangedListener onCalendarStateChangedListener);

    //获取当前日历的状态  Attrs.MONTH==月视图    Attrs.WEEK==周视图
    int getCalendarState();

}
