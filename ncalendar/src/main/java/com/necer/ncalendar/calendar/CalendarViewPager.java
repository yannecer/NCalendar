package com.necer.ncalendar.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.CalendarAdapter;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.CalendarView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2017/6/13.
 */

public abstract class CalendarViewPager extends ViewPager {

    protected CalendarAdapter calendarAdapter;
    protected CalendarView currentView;
    protected DateTime startDateTime;
    protected DateTime endDateTime;
    protected int mPageSize;
    protected int mCurrPage;
    protected List<String> mPointList;

    protected boolean isMultiple;//是否多选，多选是指周与周，月与月之间


    public CalendarViewPager(Context context) {
        this(context, null);
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        Attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, getResources().getColor(R.color.solarTextColor));
        Attrs.lunarTextColor = ta.getColor(R.styleable.NCalendar_lunarTextColor, getResources().getColor(R.color.lunarTextColor));
        Attrs.selectCircleColor = ta.getColor(R.styleable.NCalendar_selectCircleColor, getResources().getColor(R.color.selectCircleColor));
        Attrs.hintColor = ta.getColor(R.styleable.NCalendar_hintColor, getResources().getColor(R.color.hintColor));
        Attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, Utils.sp2px(context, 14));
        Attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, Utils.sp2px(context, 8));
        Attrs.selectCircleRadius = ta.getInt(R.styleable.NCalendar_selectCircleRadius, (int) Utils.dp2px(context, 20));
        Attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, true);

        Attrs.pointSize = ta.getDimension(R.styleable.NCalendar_pointSize, (int) Utils.dp2px(context, 2));
        Attrs.pointColor = ta.getColor(R.styleable.NCalendar_pointColor, getResources().getColor(R.color.selectCircleColor));
        Attrs.hollowCircleColor = ta.getColor(R.styleable.NCalendar_hollowCircleColor, Color.WHITE);
        Attrs.hollowCircleStroke = ta.getInt(R.styleable.NCalendar_hollowCircleStroke, (int) Utils.dp2px(context, 1));

        isMultiple = ta.getBoolean(R.styleable.NCalendar_isMultiple, true);

        String startString = ta.getString(R.styleable.NCalendar_startDateTime);
        String endString = ta.getString(R.styleable.NCalendar_endDateTime);
        ta.recycle();


        mPointList = new ArrayList<>();

        startDateTime = new DateTime(startString == null ? "1901-01-01" : startString);
        endDateTime = new DateTime(endString == null ? "2099-12-31" : endString);

        calendarAdapter = getCalendarAdapter(mPointList);
        setAdapter(calendarAdapter);
        setCurrentItem(mCurrPage);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                initCurrentCalendarView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                initCurrentCalendarView();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        setBackgroundColor(getResources().getColor(android.R.color.white));
    }


    public void setDate(int year, int month, int day) {
        setDate(year, month, day, false);
    }

    protected abstract CalendarAdapter getCalendarAdapter(List<String> pointList);

    protected abstract void initCurrentCalendarView();

    public abstract void setDate(int year, int month, int day, boolean smoothScroll);

    public abstract int jumpDate(DateTime dateTime, boolean smoothScroll);

    public DateTime getSelectDateTime() {
        if (currentView == null) {
            return null;
        }
        return currentView.getSelectDateTime();
    }

    public DateTime getInitialDateTime() {
        if (currentView == null) {
            return null;
        }
        return currentView.getInitialDateTime();
    }


    public CalendarView getCurrentCalendarView() {
        return currentView;
    }

    public void setPointList(List<String> pointList) {
        //全部页面重绘

        mPointList.clear();
        mPointList.addAll(pointList);
        SparseArray<CalendarView> calendarViews = calendarAdapter.getCalendarViews();
        for (int i = 0; i < calendarViews.size(); i++) {
            int key = calendarViews.keyAt(i);
            calendarViews.get(key).invalidate();
        }
    }

    protected void clearSelect(CalendarView currentCalendarView) {
        SparseArray<CalendarView> monthViews = calendarAdapter.getCalendarViews();
        for (int i = 0; i < monthViews.size(); i++) {
            int key = monthViews.keyAt(i);
            CalendarView view = monthViews.get(key);
            if (view.hashCode() != currentCalendarView.hashCode()) {
                view.clear();
            }
        }
    }


    private boolean isScrollEnable = true;

    public void setScrollEnable(boolean isScrollEnable) {
        this.isScrollEnable = isScrollEnable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollEnable ? super.onTouchEvent(ev) : false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollEnable ? super.onInterceptTouchEvent(ev) : false;
    }
}
