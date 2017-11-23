package com.necer.ncalendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.view.CalendarView;

import org.joda.time.DateTime;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public abstract class CalendarAdapter extends PagerAdapter {


    protected Context mContext;
    protected int mCount;//总页数
    protected int mCurr;//当前位置
    protected SparseArray<CalendarView> mCalendarViews;
    protected DateTime mDateTime;

    public CalendarAdapter(Context mContext, int count, int curr, DateTime dateTime) {
        this.mContext = mContext;
        this.mDateTime = dateTime;
        this.mCurr = curr;
        this.mCount = count;
        mCalendarViews = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    public SparseArray<CalendarView> getCalendarViews() {
        return mCalendarViews;
    }
}
