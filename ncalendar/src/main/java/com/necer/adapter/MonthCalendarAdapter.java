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

    public MonthCalendarAdapter(Context context, Attrs attrs, LocalDate initializeDate,OnClickMonthViewListener onClickMonthViewListener) {
        super(context, attrs,initializeDate);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }

    @Override
    protected BaseCalendarView getView(int position) {
        int i = position - mCurr;
        LocalDate date = this.mInitializeDate.plusMonths(i);
        MonthView monthView = new MonthView(mContext, mAttrs, date, mOnClickMonthViewListener);
        return monthView;
    }

    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate);
    }

}
