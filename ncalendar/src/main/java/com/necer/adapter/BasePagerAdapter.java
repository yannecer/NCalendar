package com.necer.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarBuild;
import com.necer.enumeration.CalendarType;
import com.necer.view.CalendarView;
import com.necer.view.CalendarView2;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

/**
 * @author necer
 * @date 2017/8/25
 * QQ群:127278900
 */
public abstract class BasePagerAdapter extends PagerAdapter {


    private Context mContext;
    private int mPageSize;
    private int mPageCurrIndex;
    private LocalDate mInitializeDate;

    private BaseCalendar mCalendar;

    BasePagerAdapter(Context context, BaseCalendar baseCalendar) {
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
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ICalendarView iCalendarView;
        LocalDate pageInitializeDate = getPageInitializeDate(position);
        if (mCalendar.getCalendarBuild() == CalendarBuild.DRAW) {
            iCalendarView = new CalendarView(mContext, mCalendar, pageInitializeDate, getCalendarType());
        } else {
            iCalendarView = new CalendarView2(mContext, mCalendar, pageInitializeDate, getCalendarType());
        }
        ((View) iCalendarView).setTag(position);
        container.addView((View) iCalendarView);
        return iCalendarView;
    }


    int getPageCurrIndex() {
        return mPageCurrIndex;
    }

    LocalDate getInitializeDate() {
        return mInitializeDate;
    }

    public BaseCalendar getCalendar() {
        return mCalendar;
    }


    /**
     * 每个页面的初始化日期
     *
     * @param position 当前的position
     * @return 当前页面初始化的日期
     */
    protected abstract LocalDate getPageInitializeDate(int position);

    /**
     * 获取是周日历还是月日历
     *
     * @return MONTH->月    WEEK->周
     */
    protected abstract CalendarType getCalendarType();


}

