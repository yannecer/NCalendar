package com.necer.listener;

import com.necer.calendar.BaseCalendar;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/7/4.
 */

public interface OnCalendarChangedListener {
    void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate);
}
