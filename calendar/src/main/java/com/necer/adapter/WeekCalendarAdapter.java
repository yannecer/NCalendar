package com.necer.adapter;

import android.content.Context;
import android.view.View;

import com.necer.listener.OnClickWeekViewListener;
import com.necer.utils.Attrs;
import com.necer.view.BaseCalendarView;
import com.necer.view.WeekView;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekCalendarAdapter extends BaseCalendarAdapter {
    private OnClickWeekViewListener mOnClickWeekViewListener;
    public WeekCalendarAdapter(Context context, Attrs attrs, int count, int curr, OnClickWeekViewListener onClickWeekViewListener) {
        super(context, attrs, count, curr);
        this.mOnClickWeekViewListener = onClickWeekViewListener;
    }

    @Override
    protected BaseCalendarView getView(int position) {
        WeekView weekView = new WeekView(mContext, mAttrs, mInitializeDate.plusDays((position - mCurr) * 7),mOnClickWeekViewListener);
        return weekView;
    }
}
