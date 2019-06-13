package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.adapter.BaseCalendarAdapter;
import com.necer.adapter.MonthCalendarAdapter;
import com.necer.entity.NDate;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendar extends BaseCalendar {



    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, int firstDayOfWeek) {
        return new MonthCalendarAdapter(context, startDate, endDate, initializeDate, firstDayOfWeek);
    }

    public MonthCalendar(Context context, Attrs attrs, CalendarPainter calendarPainter) {
        super(context, attrs, calendarPainter);
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate);
    }

    @Override
    protected LocalDate getIntervalDate(LocalDate localDate, int count) {
        LocalDate date = localDate.plusMonths(count);
        return date;
    }


    public boolean isMonthState() {
        return getY() >= 0;
    }

    public boolean isWeekState() {
        return getY() <= -getPivotDistanceFromTop();
    }

    @Override
    public float getY() {
        return super.getY();
    }
}
