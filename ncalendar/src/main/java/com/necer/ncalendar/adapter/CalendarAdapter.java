package com.necer.ncalendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.view.CalendarView;

import org.joda.time.DateTime;

/**
 * Created by necer on 2017/6/13.
 */

public abstract class CalendarAdapter extends PagerAdapter{

    protected Context mContext;
    protected int mCount;//总页数
    protected int mCurr;//当前位置
    protected SparseArray<CalendarView> mCalendarViews;
    protected DateTime mDateTime;

    public CalendarAdapter(Context context, int count, int curr,DateTime dateTime) {
        this.mDateTime = dateTime;
        this.mContext = context;
        this.mCount = count;
        this.mCurr = curr;
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
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    public SparseArray<CalendarView> getCalendarViews() {
        return mCalendarViews;
    }

}
