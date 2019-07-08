package com.necer.listener;

import com.necer.enumeration.CalendarState;

/**
 * 折叠日历状态变化回调
 */
public interface OnCalendarStateChangedListener {
    void onCalendarStateChange(CalendarState calendarState);
}
