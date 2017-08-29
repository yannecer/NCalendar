package com.necer.ncalendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.view.NCalendarView;

import org.joda.time.DateTime;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public abstract class NCalendarAdapter extends PagerAdapter {


    protected Context mContext;
    protected int mCount;//总页数
    protected int mCurr;//当前位置
    protected SparseArray<NCalendarView> mCalendarViews;
    protected DateTime mDateTime;

    public NCalendarAdapter(Context mContext, int count, int curr, DateTime dateTime) {
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
    public SparseArray<NCalendarView> getCalendarViews() {
        return mCalendarViews;
    }
}
