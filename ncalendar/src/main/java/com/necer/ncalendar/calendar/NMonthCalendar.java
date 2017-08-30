package com.necer.ncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.necer.ncalendar.adapter.NCalendarAdapter;
import com.necer.ncalendar.adapter.NMonthAdapter;
import com.necer.ncalendar.listener.OnClickMonthCalendarListener;
import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.listener.OnMonthCalendarPageChangeListener;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.NCalendarView;
import com.necer.ncalendar.view.NMonthView;

import org.joda.time.DateTime;
import org.joda.time.Months;

/**
 * Created by 闫彬彬 on 2017/8/28.
 * QQ:619008099
 */

public class NMonthCalendar extends NCalendarPager implements OnClickMonthViewListener {


    private DateTime mInitialDateTime;//初始化datetime
    private DateTime mSelectDateTime;//当前页面选中的datetime

    private OnClickMonthCalendarListener onClickMonthCalendarListener;
    private OnMonthCalendarPageChangeListener onMonthCalendarPageChangeListener;



    public NMonthCalendar(Context context) {
        this(context, null);
    }

    public NMonthCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected NCalendarAdapter getCalendarAdapter() {
        mInitialDateTime = new DateTime();
        mPageSize = Months.monthsBetween(startDateTime, endDateTime).getMonths() + 1;
        mCurrPage = Months.monthsBetween(startDateTime, DateTime.now()).getMonths();
        return new NMonthAdapter(getContext(), mPageSize, mCurrPage, mInitialDateTime, this);
    }

    private int lastPosition;

    @Override
    protected void initCurrentCalendarView(int position) {
        NMonthView currView = (NMonthView) calendarAdapter.getCalendarViews().get(position);
        NMonthView lastView = (NMonthView) calendarAdapter.getCalendarViews().get(position - 1);
        NMonthView nextView = (NMonthView) calendarAdapter.getCalendarViews().get(position + 1);

        if (lastView != null)
            lastView.clear();

        if (nextView != null)
            nextView.clear();


        if (lastPosition == 0) {
            lastPosition = position;
            currView.setSelectDateTime(mInitialDateTime);
            mSelectDateTime = mInitialDateTime;
        }

        DateTime dateTime = mSelectDateTime == null ? mInitialDateTime : mSelectDateTime;

        if (lastPosition < position) {
            //又滑
            DateTime dateTime1 = dateTime.plusMonths(1);
            currView.setSelectDateTime(dateTime1);
            mSelectDateTime = dateTime1;

        }

        if (lastPosition > position) {
            //左划
            DateTime dateTime2 = dateTime.plusMonths(-1);
            currView.setSelectDateTime(dateTime2);
            mSelectDateTime = dateTime2;
        }
        lastPosition = position;

        if (onMonthCalendarPageChangeListener != null ) {
            onMonthCalendarPageChangeListener.onMonthCalendarPageSelected(mSelectDateTime);
        }


    }

    public void setOnClickMonthCalendarListener(OnClickMonthCalendarListener onClickMonthCalendarListener) {
        this.onClickMonthCalendarListener = onClickMonthCalendarListener;
    }

    public void setOnMonthCalendarPageChangeListener(OnMonthCalendarPageChangeListener onMonthCalendarPageChangeListener) {
        this.onMonthCalendarPageChangeListener = onMonthCalendarPageChangeListener;
    }

    @Override
    protected void setDateTime(DateTime dateTime) {
        int i = jumpDate(dateTime, false);
        NMonthView monthView = (NMonthView) calendarAdapter.getCalendarViews().get(i);
        if (monthView == null) {
            return;
        }
        mSelectDateTime = dateTime;
        monthView.setSelectDateTime(dateTime);
    }

    @Override
    protected int jumpDate(DateTime dateTime, boolean smoothScroll) {
        SparseArray<NCalendarView> calendarViews = calendarAdapter.getCalendarViews();
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

    private void doClickEvent(DateTime dateTime, int currentItem) {
        NMonthCalendar.this.setCurrentItem(currentItem);
        NMonthView nMonthView = (NMonthView) calendarAdapter.getCalendarViews().get(currentItem);
        if (nMonthView == null) {
            return;
        }
        nMonthView.setSelectDateTime(dateTime);
        mSelectDateTime = dateTime;

        if (onClickMonthCalendarListener != null) {
            onClickMonthCalendarListener.onClickMonthCalendar(dateTime);
        }


    }


    public NMonthView getCurrectMonthView() {
        return (NMonthView) calendarAdapter.getCalendarViews().get(getCurrentItem());
    }


}
