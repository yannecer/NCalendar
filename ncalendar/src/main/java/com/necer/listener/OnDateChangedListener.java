package com.necer.listener;

import com.necer.calendar.BaseCalendar;
import com.necer.entity.NDate;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/11/7.
 */
public interface OnDateChangedListener {
    void onDateChanged(BaseCalendar baseCalendar, NDate nDate, boolean isDraw);
}
