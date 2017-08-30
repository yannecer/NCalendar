package com.necer.ncalendar.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.view.NWeekView;

import org.joda.time.DateTime;

/**
 * Created by 闫彬彬 on 2017/8/30.
 * QQ:619008099
 */

public class NWeekAdapter extends NCalendarAdapter{

    private OnClickWeekViewListener mOnClickWeekViewListener;

    public NWeekAdapter(Context mContext, int count, int curr, DateTime dateTime,OnClickWeekViewListener onClickWeekViewListener) {
        super(mContext, count, curr, dateTime);
        this.mOnClickWeekViewListener = onClickWeekViewListener;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        NWeekView nWeekView = (NWeekView) mCalendarViews.get(position);
        if (nWeekView == null) {
            nWeekView = new NWeekView(mContext, mDateTime.plusDays((position - mCurr) * 7),mOnClickWeekViewListener);
            mCalendarViews.put(position, nWeekView);
        }
        container.addView(mCalendarViews.get(position));
        return mCalendarViews.get(position);
    }




}
