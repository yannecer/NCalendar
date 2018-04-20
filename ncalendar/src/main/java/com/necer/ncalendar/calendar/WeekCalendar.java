package com.necer.ncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.Toast;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.CalendarAdapter;
import com.necer.ncalendar.adapter.WeekAdapter;
import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.listener.OnWeekCalendarChangedListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.CalendarView;
import com.necer.ncalendar.view.WeekView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/8/30.
 * QQ群:127278900
 */

public class WeekCalendar extends CalendarPager implements OnClickWeekViewListener {

    private OnWeekCalendarChangedListener onWeekCalendarChangedListener;

    public WeekCalendar(Context context) {
        super(context);
    }

    public WeekCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected CalendarAdapter getCalendarAdapter() {

        mPageSize = Utils.getIntervalWeek(startDate, endDate, Attrs.firstDayOfWeek) + 1;
        mCurrPage = Utils.getIntervalWeek(startDate, mInitialDate, Attrs.firstDayOfWeek);

        return new WeekAdapter(getContext(), mPageSize, mCurrPage, mInitialDate, this);
    }


    private int lastPosition = -1;

    @Override
    protected void initCurrentCalendarView(int position) {

        WeekView currView = (WeekView) calendarAdapter.getCalendarViews().get(position);
        WeekView lastView = (WeekView) calendarAdapter.getCalendarViews().get(position - 1);
        WeekView nextView = (WeekView) calendarAdapter.getCalendarViews().get(position + 1);
        if (currView == null)
            return;

        if (lastView != null)
            lastView.clear();

        if (nextView != null)
            nextView.clear();

        //只处理翻页
        if (lastPosition == -1) {
            currView.setDateAndPoint(mInitialDate, pointList);
            mSelectDate = mInitialDate;
            lastSelectDate = mInitialDate;
            if (onWeekCalendarChangedListener != null) {
                onWeekCalendarChangedListener.onWeekCalendarChanged(mSelectDate);
            }
        } else if (isPagerChanged) {
            int i = position - lastPosition;
            mSelectDate = mSelectDate.plusWeeks(i);

            if (isDefaultSelect) {
                //日期越界
                if (mSelectDate.isAfter(endDate)) {
                    mSelectDate = endDate;
                } else if (mSelectDate.isBefore(startDate)) {
                    mSelectDate = startDate;
                }

                currView.setDateAndPoint(mSelectDate, pointList);
                if (onWeekCalendarChangedListener != null) {
                    onWeekCalendarChangedListener.onWeekCalendarChanged(mSelectDate);
                }
            } else {
                if (Utils.isEqualsMonth(lastSelectDate, mSelectDate)) {
                    currView.setDateAndPoint(lastSelectDate, pointList);
                }
            }

        }
        lastPosition = position;
    }

    public void setOnWeekCalendarChangedListener(OnWeekCalendarChangedListener onWeekCalendarChangedListener) {
        this.onWeekCalendarChangedListener = onWeekCalendarChangedListener;
    }


    @Override
    protected void setDate(LocalDate date) {

        if (date.isAfter(endDate) || date.isBefore(startDate)) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        SparseArray<CalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return;
        }

        isPagerChanged = false;

        WeekView currectWeekView = (WeekView) calendarViews.get(getCurrentItem());

        //不是当周
        if (!currectWeekView.contains(date)) {

            LocalDate initialDate = currectWeekView.getInitialDate();
            int weeks = Utils.getIntervalWeek(initialDate, date, Attrs.firstDayOfWeek);
            int i = getCurrentItem() + weeks;
            setCurrentItem(i, Math.abs(weeks) < 2);
            currectWeekView = (WeekView) calendarViews.get(getCurrentItem());
        }

        currectWeekView.setDateAndPoint(date, pointList);
        mSelectDate = date;
        lastSelectDate = date;

        isPagerChanged = true;

        if (onWeekCalendarChangedListener != null) {
            onWeekCalendarChangedListener.onWeekCalendarChanged(mSelectDate);
        }
    }


    @Override
    public void onClickCurrentWeek(LocalDate date) {

        if (date.isAfter(endDate) || date.isBefore(startDate)) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        WeekView weekView = (WeekView) calendarAdapter.getCalendarViews().get(getCurrentItem());
        weekView.setDateAndPoint(date, pointList);
        mSelectDate = date;
        lastSelectDate = date;
        if (onWeekCalendarChangedListener != null) {
            onWeekCalendarChangedListener.onWeekCalendarChanged(date);
        }

    }
}
