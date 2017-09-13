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
        mPageSize = Months.monthsBetween(startDateTime, endDateTime).getMonths() + 1;
        mCurrPage = Months.monthsBetween(startDateTime, DateTime.now()).getMonths();
        return new NMonthAdapter(getContext(), mPageSize, mCurrPage, mInitialDateTime, this);
    }

    private int lastPosition = -1;

    @Override
    protected void initCurrentCalendarView(int position) {

        NMonthView currView = (NMonthView) calendarAdapter.getCalendarViews().get(position);
        NMonthView lastView = (NMonthView) calendarAdapter.getCalendarViews().get(position - 1);
        NMonthView nextView = (NMonthView) calendarAdapter.getCalendarViews().get(position + 1);

        if (lastView != null)
            lastView.clear();

        if (nextView != null)
            nextView.clear();

        if (lastPosition == -1) {
            lastPosition = position;
            currView.setSelectDateTime(mInitialDateTime);
            mSelectDateTime = mInitialDateTime;
        } else if (setDateTime == null) {
            int i = position - lastPosition;//相差几个月
            DateTime dateTime = mSelectDateTime.plusMonths(i);
            currView.setSelectDateTime(dateTime);
            mSelectDateTime = dateTime;
        }
        lastPosition = position;

        //正常滑动处理
        if (onMonthCalendarPageChangeListener != null && setDateTime == null) {
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
    public void setDateTime(DateTime dateTime) {
        this.setDateTime = dateTime;

        SparseArray<NCalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return;
        }
        DateTime initialDateTime = calendarViews.get(getCurrentItem()).getInitialDateTime();
        int months = Utils.getIntervalMonths(initialDateTime, dateTime);
        int i = getCurrentItem() + months;
        setCurrentItem(i, false);

        NMonthView monthView = (NMonthView) calendarAdapter.getCalendarViews().get(i);
        if (monthView == null) {
            return;
        }
        monthView.setSelectDateTime(dateTime);
        mSelectDateTime = dateTime;

        //跳转处理
        if (onMonthCalendarPageChangeListener != null && setDateTime != null) {
            setDateTime = null;
            onMonthCalendarPageChangeListener.onMonthCalendarPageSelected(mSelectDateTime);
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
