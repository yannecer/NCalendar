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

/**
 * Created by 闫彬彬 on 2017/8/28.
 * QQ:619008099
 */

public class MonthCalendar extends CalendarPager implements OnClickMonthViewListener {

    private OnMonthCalendarChangedListener onMonthCalendarChangedListener;
    private int lastPosition = -1;
    public MonthCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected CalendarAdapter getCalendarAdapter() {

        mPageSize = Utils.getIntervalMonths(startDateTime, endDateTime) + 1;
        mCurrPage = Utils.getIntervalMonths(startDateTime, mInitialDateTime);

        return new MonthAdapter(getContext(), mPageSize, mCurrPage, mInitialDateTime, this);
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

        if (lastPosition == -1) {
            currView.setDateTimeAndPoint(mInitialDateTime, pointList);
            mSelectDateTime = mInitialDateTime;
        } else {
            DateTime dateTime;
            if (setDateTime == null) {
                int i = position - lastPosition;
                dateTime = mSelectDateTime.plusMonths(i);
            } else {
                dateTime = setDateTime;
                //置为空，不影响下次判断
                setDateTime = null;
            }

            //日期越界
            if (dateTime.getMillis() > endDateTime.getMillis()) {
                dateTime = endDateTime;
            } else if (dateTime.getMillis() < startDateTime.getMillis()) {
                dateTime = startDateTime;
            }

            currView.setDateTimeAndPoint(dateTime, pointList);
            mSelectDateTime = dateTime;
        }
        lastPosition = position;

        if (onMonthCalendarChangedListener != null) {
            onMonthCalendarChangedListener.onMonthCalendarChanged(mSelectDateTime);
        }
    }

    public void setOnMonthCalendarChangedListener(OnMonthCalendarChangedListener onMonthCalendarChangedListener) {
        this.onMonthCalendarChangedListener = onMonthCalendarChangedListener;
    }

    @Override
    protected void setDateTime(DateTime dateTime) {

        if (dateTime.getMillis() > endDateTime.getMillis() || dateTime.getMillis() < startDateTime.getMillis()) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        SparseArray<CalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return;
        }

        setDateTime = dateTime;
        MonthView currectMonthView = getCurrectMonthView();

        //选中日历是同一月，则执行initCurrentCalendarView，不是则跳转，间接执行initCurrentCalendarView
        if (Utils.isEqualsMonth(mSelectDateTime,dateTime)) {
            initCurrentCalendarView(getCurrentItem());
        } else {
            DateTime initialDateTime = currectMonthView.getInitialDateTime();
            int months = Utils.getIntervalMonths(initialDateTime, dateTime);
            int i = getCurrentItem() + months;
            setCurrentItem(i, false);
        }


    }

    @Override
    public void onClickCurrentMonth(DateTime dateTime) {
        dealClickEvent(dateTime, getCurrentItem());
    }

    @Override
    public void onClickLastMonth(DateTime dateTime) {
        int currentItem = getCurrentItem() - 1;
        dealClickEvent(dateTime, currentItem);
    }

    @Override
    public void onClickNextMonth(DateTime dateTime) {
        int currentItem = getCurrentItem() + 1;
        dealClickEvent(dateTime, currentItem);
    }

    private void dealClickEvent(DateTime dateTime, int currentItem) {

        if (dateTime.getMillis() > endDateTime.getMillis() || dateTime.getMillis() < startDateTime.getMillis()) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        MonthCalendar.this.setCurrentItem(currentItem);
        MonthView nMonthView = (MonthView) calendarAdapter.getCalendarViews().get(currentItem);
        if (nMonthView == null) {
            return;
        }
        nMonthView.setDateTimeAndPoint(dateTime, pointList);
        mSelectDateTime = dateTime;

        if (onMonthCalendarChangedListener != null) {
            onMonthCalendarChangedListener.onMonthCalendarChanged(dateTime);
        }

    }


    public MonthView getCurrectMonthView() {
        return (MonthView) calendarAdapter.getCalendarViews().get(getCurrentItem());
    }



}
