package com.necer.adapter;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.necer.calendar.BaseCalendar;
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
    protected int mPageSize;//总页数
    protected int mPageCurrIndex;
    protected LocalDate mInitializeDate;

    protected BaseCalendar mCalendar;

    public BasePagerAdapter(Context context, BaseCalendar baseCalendar) {
        this.mContext = context;
        this.mCalendar = baseCalendar;
        this.mInitializeDate = baseCalendar.getInitializeDate();
        this.mPageSize = baseCalendar.getCalendarPagerSize();
        this.mPageCurrIndex = baseCalendar.getCalendarCurrIndex();
    }

    @Override
    public int getCount() {
        return mPageSize;
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



}

