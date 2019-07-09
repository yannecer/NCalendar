package com.necer.listener;

import com.necer.calendar.BaseCalendar;

import org.joda.time.LocalDate;

import java.util.List;

//月周切换时，中心点变化的回调
public interface OnMWDateChangeListener {
    void onMwDateChange(BaseCalendar baseCalendar, LocalDate localDate, List<LocalDate> selectDateList);
}
