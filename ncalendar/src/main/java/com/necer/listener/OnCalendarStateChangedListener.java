package com.necer.listener;

import com.necer.enumeration.CalendarState;

/**
 * 折叠日历状态变化回调
 * @author necer
 */
public interface OnCalendarStateChangedListener {

    /**
     * 折叠日历（miui9，miui10，emui）月周切换时的回调
     * @param calendarState 日历状态 ，参考 CalendarState
     */
    void onCalendarStateChange(CalendarState calendarState);
}
