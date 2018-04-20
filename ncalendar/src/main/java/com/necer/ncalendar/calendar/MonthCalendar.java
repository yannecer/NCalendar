package com.necer.ncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.Toast;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.CalendarAdapter;
import com.necer.ncalendar.adapter.MonthAdapter;
import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.listener.OnMonthCalendarChangedListener;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.CalendarView;
import com.necer.ncalendar.view.MonthView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/8/28.
 * QQ群:127278900
 */

public class MonthCalendar extends CalendarPager implements OnClickMonthViewListener {

    private OnMonthCalendarChangedListener onMonthCalendarChangedListener;
    private int lastPosition = -1;

    public MonthCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected CalendarAdapter getCalendarAdapter() {

        mPageSize = Utils.getIntervalMonths(startDate, endDate) + 1;
        mCurrPage = Utils.getIntervalMonths(startDate, mInitialDate);

        return new MonthAdapter(getContext(), mPageSize, mCurrPage, mInitialDate, this);
    }


    @Override
    protected void initCurrentCalendarView(int position) {

        MonthView currView = (MonthView) calendarAdapter.getCalendarViews().get(position);
        MonthView lastView = (MonthView) calendarAdapter.getCalendarViews().get(position - 1);
        MonthView nextView = (MonthView) calendarAdapter.getCalendarViews().get(position + 1);


        if (currView == null) {
            return;
        }

        if (lastView != null)
            lastView.clear();

        if (nextView != null)
            nextView.clear();


        //只处理翻页
        if (lastPosition == -1) {
            currView.setDateAndPoint(mInitialDate, pointList);
            mSelectDate = mInitialDate;
            lastSelectDate = mInitialDate;
            if (onMonthCalendarChangedListener != null) {
                onMonthCalendarChangedListener.onMonthCalendarChanged(mSelectDate);
            }
        } else if (isPagerChanged) {
            int i = position - lastPosition;
            mSelectDate = mSelectDate.plusMonths(i);

            if (isDefaultSelect) {
                //日期越界
                if (mSelectDate.isAfter(endDate)) {
                    mSelectDate = endDate;
                } else if (mSelectDate.isBefore(startDate)) {
                    mSelectDate = startDate;
                }

                currView.setDateAndPoint(mSelectDate, pointList);
                if (onMonthCalendarChangedListener != null) {
                    onMonthCalendarChangedListener.onMonthCalendarChanged(mSelectDate);
                }
            } else {
                if (Utils.isEqualsMonth(lastSelectDate, mSelectDate)) {
                    currView.setDateAndPoint(lastSelectDate, pointList);
                }
            }

        }
        lastPosition = position;
    }

    public void setOnMonthCalendarChangedListener(OnMonthCalendarChangedListener onMonthCalendarChangedListener) {
        this.onMonthCalendarChangedListener = onMonthCalendarChangedListener;
    }

    @Override
    protected void setDate(LocalDate date) {
        if (date.isAfter(endDate)  || date.isBefore(startDate)) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        SparseArray<CalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return;
        }

        isPagerChanged = false;

        MonthView currectMonthView = getCurrectMonthView();
        LocalDate initialDate = currectMonthView.getInitialDate();

        //不是当月
        if (!Utils.isEqualsMonth(initialDate, date)) {
            int months = Utils.getIntervalMonths(initialDate, date);
            int i = getCurrentItem() + months;
            setCurrentItem(i, Math.abs(months) < 2);
            currectMonthView = getCurrectMonthView();
        }

        currectMonthView.setDateAndPoint(date, pointList);
        mSelectDate = date;
        lastSelectDate = date;

        isPagerChanged = true;

        if (onMonthCalendarChangedListener != null) {
            onMonthCalendarChangedListener.onMonthCalendarChanged(mSelectDate);
        }


    }

    @Override
    public void onClickCurrentMonth(LocalDate date) {
        dealClickEvent(date, getCurrentItem());
    }

    @Override
    public void onClickLastMonth(LocalDate date) {
        int currentItem = getCurrentItem() - 1;
        dealClickEvent(date, currentItem);
    }

    @Override
    public void onClickNextMonth(LocalDate date) {
        int currentItem = getCurrentItem() + 1;
        dealClickEvent(date, currentItem);
    }

    private void dealClickEvent(LocalDate date, int currentItem) {

        if (date.isAfter(endDate)  || date.isBefore(startDate)) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        isPagerChanged = false;
        setCurrentItem(currentItem, true);
        MonthView nMonthView = getCurrectMonthView();
        nMonthView.setDateAndPoint(date, pointList);
        mSelectDate = date;
        lastSelectDate = date;

        isPagerChanged = true;

        if (onMonthCalendarChangedListener != null) {
            onMonthCalendarChangedListener.onMonthCalendarChanged(date);
        }
    }


    public MonthView getCurrectMonthView() {
        return (MonthView) calendarAdapter.getCalendarViews().get(getCurrentItem());
    }

}
