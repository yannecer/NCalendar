package com.necer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.necer.utils.Attrs;
import com.necer.view.BaseCalendarView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public abstract class BaseCalendarAdapter extends PagerAdapter {


    protected Context mContext;
    protected int mCount;//总页数
    protected int mCurr;//当前位置
    protected Attrs mAttrs;//属性参数
    protected LocalDate mInitializeDate;//日期初始化，默认是当天


    public BaseCalendarAdapter(Context context, Attrs attrs,LocalDate initializeDate) {
        this.mContext = context;
        this.mAttrs = attrs;
        this.mInitializeDate = initializeDate;
        LocalDate startDate = new LocalDate(attrs.startDateString);
        this.mCount = getIntervalCount(startDate, new LocalDate(attrs.endDateString), attrs.firstDayOfWeek) + 1;
        this.mCurr = getIntervalCount(startDate, mInitializeDate, attrs.firstDayOfWeek);
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
        BaseCalendarView view = getView(mContext, mAttrs.firstDayOfWeek, mInitializeDate, mCurr, position);
        view.setTag(position);
        container.addView(view);
        return view;
    }

    protected abstract BaseCalendarView getView(Context context,int weekFirstDayType,LocalDate initializeDate,int curr,int position);

    protected abstract int getIntervalCount(LocalDate startDate, LocalDate endDate, int weekFirstDayType);

    //当前页的位置
    public int getCurrItem() {
        return mCurr;
    }

}

