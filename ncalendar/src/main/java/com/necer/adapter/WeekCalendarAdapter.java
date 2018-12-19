package com.necer.adapter;

import android.content.Context;

import com.necer.listener.OnClickWeekViewListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;
import com.necer.view.BaseCalendarView;
import com.necer.view.WeekView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekCalendarAdapter extends BaseCalendarAdapter {
    private OnClickWeekViewListener mOnClickWeekViewListener;

    public WeekCalendarAdapter(Context context, Attrs attrs, OnClickWeekViewListener onClickWeekViewListener) {
        super(context, attrs);
        this.mOnClickWeekViewListener = onClickWeekViewListener;
    }

    @Override
    protected BaseCalendarView getView(int position) {
        WeekView weekView = new WeekView(mContext, mAttrs, mInitializeDate.plusDays((position - mCurr) * 7), mOnClickWeekViewListener);
        return weekView;
    }

    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalWeek(startDate, endDate, type);
    }
}
