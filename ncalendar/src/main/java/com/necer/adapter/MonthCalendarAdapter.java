package com.necer.adapter;

import android.content.Context;

import com.necer.listener.OnClickMonthViewListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;
import com.necer.view.BaseCalendarView;
import com.necer.view.MonthView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendarAdapter extends BaseCalendarAdapter {


    private OnClickMonthViewListener mOnClickMonthViewListener;

    public MonthCalendarAdapter(Context context, Attrs attrs, LocalDate initializeDate, OnClickMonthViewListener onClickMonthViewListener) {
        super(context, attrs,initializeDate);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }

    @Override
    protected BaseCalendarView getView(Context context,int weekFirstDayType,LocalDate initializeDate,int curr,int position) {
        LocalDate date = initializeDate.plusMonths(position - curr);
        MonthView monthView = new MonthView(context, date, weekFirstDayType, mOnClickMonthViewListener);
        return monthView;
    }

    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate);
    }

}
