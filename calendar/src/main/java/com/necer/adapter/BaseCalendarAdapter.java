package com.necer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.necer.MyLog;
import com.necer.utils.Attrs;
import com.necer.view.BaseCalendarView;
import com.necer.view.MonthView;

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
    protected SparseArray<BaseCalendarView> mCalendarViews;



    public BaseCalendarAdapter(Context context, Attrs attrs, int count, int curr) {
        this.mContext = context;
        this.mAttrs = attrs;
        this.mCount = count;
        this.mCurr = curr;
        mCalendarViews = new SparseArray<>();
        mInitializeDate = new LocalDate();
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
        mCalendarViews.remove(position);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        BaseCalendarView view = getView(position);
        mCalendarViews.put(position, view);
        container.addView(view);
        return view;
    }

    protected abstract BaseCalendarView getView(int position);


    public BaseCalendarView getBaseCalendarView(int position) {

        return mCalendarViews.get(position);
    }
}
