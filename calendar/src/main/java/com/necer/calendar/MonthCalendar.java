package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.necer.adapter.BaseCalendarAdapter;
import com.necer.adapter.MonthCalendarAdapter;
import com.necer.listener.OnClickMonthViewListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendar extends BaseCalendar implements OnClickMonthViewListener {


    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, Attrs attrs, int calendarSize, int currNum) {
        return new MonthCalendarAdapter(context,attrs,calendarSize,currNum,this);
    }

    @Override
    protected int getCalendarSize(LocalDate startDate, LocalDate endDate,int type) {
        return Util.getIntervalMonths(startDate, endDate) + 1;
    }

    @Override
    protected int getCurrNum(LocalDate startDate,int type) {
        return Util.getIntervalMonths(startDate, new LocalDate());
    }

    @Override
    public void onClickCurrentMonth(LocalDate date) {

    }

    @Override
    public void onClickLastMonth(LocalDate date) {

    }

    @Override
    public void onClickNextMonth(LocalDate date) {

    }
}
