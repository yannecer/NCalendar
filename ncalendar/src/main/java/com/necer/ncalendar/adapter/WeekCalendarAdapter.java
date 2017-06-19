package com.necer.ncalendar.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.view.WeekView;

import org.joda.time.DateTime;

/**
 * Created by necer on 2017/6/13.
 */

public class WeekCalendarAdapter extends CalendarAdapter{

    private OnClickWeekViewListener mOnClickWeekViewListener;

    public WeekCalendarAdapter(Context context, int count,int curr, DateTime dateTime, OnClickWeekViewListener onClickWeekViewListener) {
        super(context, count,curr, dateTime);
        this.mOnClickWeekViewListener = onClickWeekViewListener;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        WeekView weekView = (WeekView) mCalendarViews.get(position);
        if (weekView == null) {
            weekView = new WeekView(mContext, mDateTime.plusDays((position - mCurr) * 7),mOnClickWeekViewListener);
            mCalendarViews.put(position, weekView);
        }
        container.addView(mCalendarViews.get(position));
        return mCalendarViews.get(position);
    }


}
