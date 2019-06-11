package com.necer.adapter;

import android.content.Context;

import com.necer.utils.Util;
import com.necer.view.BaseCalendarView;
import com.necer.view.MonthView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendarAdapter extends BaseCalendarAdapter {



    public MonthCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, int firstDayOfWeek) {
        super(context, startDate, endDate, initializeDate, firstDayOfWeek);
    }

    @Override
    protected BaseCalendarView getCalendarView( int position) {
        LocalDate localDate = mInitializeDate.plusMonths(position - mCurr);
        List<LocalDate> dateList = Util.getMonthCalendar(localDate, mFirstDayOfWeek);
        return new MonthView(mContext,localDate,dateList);
    }


    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate);
    }
}
