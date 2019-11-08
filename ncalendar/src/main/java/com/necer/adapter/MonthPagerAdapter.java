package com.necer.adapter;

import android.content.Context;

import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarType;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthPagerAdapter extends BasePagerAdapter {

    public MonthPagerAdapter(Context context, BaseCalendar baseCalendar) {
        super(context, baseCalendar);
    }

    @Override
    protected LocalDate getPageInitializeDate(int position) {
        LocalDate localDate = mInitializeDate.plusMonths(position - mPageCurrIndex);
        return localDate;
    }

    @Override
    protected CalendarType getCalendarType() {
        return CalendarType.MONTH;
    }
}
