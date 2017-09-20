package com.necer.ncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.Toast;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.NCalendarAdapter;
import com.necer.ncalendar.adapter.NMonthAdapter;
import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.listener.OnMonthCalendarChangedListener;
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

   // private OnClickMonthCalendarListener onClickMonthCalendarListener;
   // private OnMonthCalendarPageChangeListener onMonthCalendarPageChangeListener;

    private OnMonthCalendarChangedListener onMonthCalendarChangedListener;

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
        } else if (!isSetDateTime) {
            int i = position - lastPosition;
            DateTime dateTime = mSelectDateTime.plusMonths(i);
            currView.setSelectDateTime(dateTime);
            mSelectDateTime = dateTime;
        }
        lastPosition = position;

        //正常滑动处理
        if (onMonthCalendarChangedListener != null && !isSetDateTime) {
            onMonthCalendarChangedListener.onMonthCalendarChanged(mSelectDateTime);
        }

    }

    public void setOnMonthCalendarChangedListener(OnMonthCalendarChangedListener onMonthCalendarChangedListener) {
        this.onMonthCalendarChangedListener = onMonthCalendarChangedListener;
    }


    @Override
    public void setDateTime(DateTime dateTime) {

        if (dateTime.getMillis() > endDateTime.getMillis() || dateTime.getMillis() < startDateTime.getMillis()) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        SparseArray<NCalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return;
        }

        isSetDateTime = true;

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

        isSetDateTime = false;

        //跳转处理
        if (onMonthCalendarChangedListener != null ) {
            onMonthCalendarChangedListener.onMonthCalendarChanged(mSelectDateTime);
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
        NMonthCalendar.this.setCurrentItem(currentItem);
        NMonthView nMonthView = (NMonthView) calendarAdapter.getCalendarViews().get(currentItem);
        if (nMonthView == null) {
            return;
        }
        nMonthView.setSelectDateTime(dateTime);
        mSelectDateTime = dateTime;

        if (onMonthCalendarChangedListener != null) {
            onMonthCalendarChangedListener.onMonthCalendarChanged(dateTime);
        }


    }


    public NMonthView getCurrectMonthView() {
        return (NMonthView) calendarAdapter.getCalendarViews().get(getCurrentItem());
    }


}
