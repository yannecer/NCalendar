package com.necer.adapter;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.necer.enumeration.CalendarBuild;
import com.necer.utils.Attrs;
import com.necer.view.CalendarView;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public abstract class BasePagerAdapter extends PagerAdapter {


    protected Context mContext;
    protected int mCount;//总页数
    protected int mCurrIndex;//总页数
    protected LocalDate mInitializeDate;
    protected int mFirstDayOfWeek;
    protected boolean mIsAllMonthSixLine;
    protected CalendarBuild mCalendarBuild;

    public BasePagerAdapter(Context context, CalendarBuild calendarBuild, LocalDate initializeDate, int count, int currIndex, int firstDayOfWeek, boolean isAllMonthSixLine) {
        this.mContext = context;
        this.mCalendarBuild = calendarBuild;
        this.mInitializeDate = initializeDate;
        this.mCount = count;
        this.mCurrIndex = currIndex;
        this.mFirstDayOfWeek = firstDayOfWeek;
        this.mIsAllMonthSixLine = isAllMonthSixLine;
    }

//    public BasePagerAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, Attrs attrs) {
//        this.mContext = context;
//        this.mInitializeDate = initializeDate;
//        this.mCount = getIntervalCount(startDate, endDate, attrs.firstDayOfWeek) + 1;
//        this.mCurr = getIntervalCount(startDate, initializeDate, attrs.firstDayOfWeek);
//        this.mAttrs = attrs;
//    }

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
        ICalendarView iCalendarView = getCalendarView(container, position);
        ((View) iCalendarView).setTag(position);
        container.addView((View) iCalendarView);
        return iCalendarView;
    }

    protected abstract ICalendarView getCalendarView(ViewGroup container, int position);

    protected abstract int getIntervalCount(LocalDate startDate, LocalDate endDate, int weekFirstDayType);


}

