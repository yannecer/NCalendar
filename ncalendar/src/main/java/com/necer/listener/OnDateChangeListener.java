package com.necer.listener;

import com.necer.calendar.BaseCalendar;

import org.joda.time.LocalDate;

import java.util.List;

public interface OnDateChangeListener {

    void onDateChange(BaseCalendar baseCalendar, LocalDate localDate, List<LocalDate> selectDateList);
}
