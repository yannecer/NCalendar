package com.necer.listener;

import com.necer.calendar.BaseCalendar;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/11/7.
 */
public interface OnDateChangedListener {
    void onDateChanged(BaseCalendar baseCalendar,LocalDate localDate,boolean isDraw);
}
