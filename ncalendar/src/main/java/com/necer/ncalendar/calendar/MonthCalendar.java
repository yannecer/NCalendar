package com.necer.ncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.necer.ncalendar.adapter.CalendarAdapter;
import com.necer.ncalendar.adapter.MonthCalendarAdapter;
import com.necer.ncalendar.listener.OnClickMonthCalendarListener;
import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.listener.OnMonthCalendarPageChangeListener;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.CalendarView;
import com.necer.ncalendar.view.MonthView;

import org.joda.time.DateTime;

/**
 * Created by necer on 2017/6/12.
 * 月视图日历
 */

public class MonthCalendar extends CalendarViewPager implements OnClickMonthViewListener {

    private MonthView currentMothView;
    private OnClickMonthCalendarListener onClickMonthCalendarListener;
    private OnMonthCalendarPageChangeListener onMonthCalendarPageChangeListener;


    public MonthCalendar(Context context) {
        this(context, null);
    }

    public MonthCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected CalendarAdapter getCalendarAdapter() {
        return new MonthCalendarAdapter(getContext(), mPageSize, new DateTime(), this);
    }

    @Override
    protected void initCurrentCalendarView() {
        currentMothView = (MonthView) calendarAdapter.getCalendarViews().get(getCurrentItem());
        if (currentMothView == null) {
            return;
        }
        currentMothView.setPointList(mPointList);
        if (onMonthCalendarPageChangeListener != null && currentMothView != null) {
            DateTime selectDateTime = currentMothView.getSelectDateTime();
            DateTime initialDateTime = currentMothView.getInitialDateTime();
            onMonthCalendarPageChangeListener.onMonthCalendarPageSelected(selectDateTime == null ? initialDateTime : selectDateTime);
        }
    }

    @Override
    public void onClickCurrentMonth(DateTime dateTime) {
        doClickEvent(dateTime, getCurrentItem());
    }

    @Override
    public void onClickLastMonth(DateTime dateTime) {
        int currentItem = getCurrentItem() - 1;
        doClickEvent(dateTime, currentItem);
    }

    @Override
    public void onClickNextMonth(DateTime dateTime) {
        int currentItem = getCurrentItem() + 1;
        doClickEvent(dateTime, currentItem);
    }

    @Override
    public void setDate(int year, int month, int day, boolean smoothScroll) {
        DateTime dateTime = new DateTime(year, month, day, 0, 0, 0);
        int i = jumpDate(dateTime, smoothScroll);
        MonthView monthView = (MonthView) calendarAdapter.getCalendarViews().get(i);
        if (monthView == null) {
            throw new RuntimeException("日历的页数不够");
        }
        monthView.setSelectDateTime(dateTime);
    }

    @Override
    public int jumpDate(DateTime dateTime, boolean smoothScroll) {
        SparseArray<CalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return getCurrentItem();
        }
        DateTime initialDateTime = calendarViews.get(getCurrentItem()).getInitialDateTime();
        int months = Utils.getIntervalMonths(initialDateTime, dateTime);

        int i = getCurrentItem() + months;
        setCurrentItem(i, smoothScroll);
        return i;
    }

    @Override
    public DateTime getSelectDateTime() {
        if (currentMothView == null) {
            return null;
        }
        return currentMothView.getSelectDateTime();
    }

    public void setOnClickMonthCalendarListener(OnClickMonthCalendarListener onClickMonthCalendarListener) {
        this.onClickMonthCalendarListener = onClickMonthCalendarListener;
    }

    public void setOnMonthCalendarPageChangeListener(OnMonthCalendarPageChangeListener onMonthCalendarPageChangeListener) {
        this.onMonthCalendarPageChangeListener = onMonthCalendarPageChangeListener;
    }


    private void doClickEvent(DateTime dateTime, int currentItem) {
        MonthCalendar.this.setCurrentItem(currentItem);
        MonthView monthView = (MonthView) calendarAdapter.getCalendarViews().get(currentItem);
        monthView.setSelectDateTime(dateTime);
        //清除其他选中
        //clearSelect(monthView);
        if (onClickMonthCalendarListener != null) {
            onClickMonthCalendarListener.onClickMonthCalendar(dateTime);
        }

    }

    public MonthView getCurrentMothView() {
        return currentMothView;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mRowHeigh = heightSize / 6;
    }
}

