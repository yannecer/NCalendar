package com.necer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.necer.view.CalendarView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public abstract class BaseCalendarAdapter extends PagerAdapter {


    protected Context mContext;
    protected int mCount;//总页数
    protected int mCurr;//当前位置
    protected LocalDate mInitializeDate;
    protected int mFirstDayOfWeek;


    public BaseCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, int firstDayOfWeek) {
        this.mContext = context;
        this.mInitializeDate = initializeDate;
        this.mCount = getIntervalCount(startDate, endDate, firstDayOfWeek) + 1;
        this.mCurr = getIntervalCount(startDate, initializeDate, firstDayOfWeek);
        this.mFirstDayOfWeek = firstDayOfWeek;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CalendarView view = getCalendarView(container, position);
        view.setTag(position);
        container.addView(view);
        return view;
    }

    //当前页的位置
    public int getCurrItem() {
        return mCurr;
    }

    protected abstract CalendarView getCalendarView(ViewGroup container, int position);

    protected abstract int getIntervalCount(LocalDate startDate, LocalDate endDate, int weekFirstDayType);


}

