package com.necer.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.necer.MyLog;
import com.necer.listener.OnClickMonthViewListener;
import com.necer.utils.Attrs;
import com.necer.view.BaseCalendarView;
import com.necer.view.MonthView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendarAdapter extends BaseCalendarAdapter {


    private OnClickMonthViewListener mOnClickMonthViewListener;
    public MonthCalendarAdapter(Context context, Attrs attrs, int count, int curr ,OnClickMonthViewListener onClickMonthViewListener) {
        super(context, attrs, count, curr);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }

    @Override
    protected BaseCalendarView getView(int position) {
        int i = position - mCurr;
        LocalDate date = this.mInitializeDate.plusMonths(i);
        MonthView monthView = new MonthView(mContext,mAttrs, date,mOnClickMonthViewListener);
        return monthView;
    }

}
