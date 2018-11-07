package com.necer.listener;

import com.necer.calendar.BaseCalendar;

/**
 * Created by necer on 2018/11/7.
 */
public interface OnYearMonthChangedListener {
    void onYearMonthChanged(BaseCalendar baseCalendar, int year, int month);
}
