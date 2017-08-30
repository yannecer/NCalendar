package com.necer.ncalendar.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.necer.ncalendar.adapter.NCalendarAdapter;
import com.necer.ncalendar.view.NCalendarView;

import org.joda.time.DateTime;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public abstract class NCalendarPager extends ViewPager {

    protected NCalendarAdapter calendarAdapter;
    protected NCalendarView currentView;
    protected DateTime startDateTime;
    protected DateTime endDateTime;
    protected int mPageSize;
    protected int mCurrPage;

    public NCalendarPager(Context context) {
        this(context, null);
    }

    public NCalendarPager(Context context, AttributeSet attrs) {
        super(context, attrs);


        String startString = null;
        String endString = null;

        startDateTime = new DateTime(startString == null ? "1901-01-01" : startString);
        endDateTime = new DateTime(endString == null ? "2099-12-31" : endString);

        calendarAdapter = getCalendarAdapter();
        setAdapter(calendarAdapter);
        setCurrentItem(mCurrPage);

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                initCurrentCalendarView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                initCurrentCalendarView(mCurrPage);
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }


    protected abstract NCalendarAdapter getCalendarAdapter();


    protected abstract void initCurrentCalendarView(int position);

    protected abstract void setDateTime(DateTime dateTime);
    protected abstract int jumpDate(DateTime dateTime,boolean smoothScroll);



}
