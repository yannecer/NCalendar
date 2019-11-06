package com.necer.calendar;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;

import com.necer.adapter.BasePagerAdapter;
import com.necer.adapter.MonthPagerAdapter;
import com.necer.enumeration.CalendarBuild;
import com.necer.utils.Attrs;
import com.necer.utils.CalendarUtil;

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
    protected BasePagerAdapter getPagerAdapter(Context context, CalendarBuild calendarBuild, LocalDate initializeDate, int count, int currIndex, int firstDayOfWeek, boolean isAllMonthSixLine) {
        return new MonthPagerAdapter(context, calendarBuild,initializeDate, count, currIndex, firstDayOfWeek, isAllMonthSixLine) ;
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return CalendarUtil.getIntervalMonths(startDate, endDate);
    }

    @Override
    protected LocalDate getIntervalDate(LocalDate localDate, int count) {
        LocalDate date = localDate.plusMonths(count);
        return date;
    }
}
