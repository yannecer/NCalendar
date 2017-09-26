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

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by 闫彬彬 on 2017/8/30.
 * QQ:619008099
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

        mPageSize = Utils.getIntervalWeek(startDateTime, endDateTime, Attrs.firstDayOfWeek)+1;
        mCurrPage = Utils.getIntervalWeek(startDateTime, mInitialDateTime, Attrs.firstDayOfWeek);

        return new WeekAdapter(getContext(), mPageSize, mCurrPage, mInitialDateTime, this);
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

        if (lastPosition == -1) {
            lastPosition = position;
            currView.setDateTimeAndPoint(mInitialDateTime, pointList);
            mSelectDateTime = mInitialDateTime;
        } else {
            DateTime dateTime;
            if (setDateTime == null) {
                int i = position - lastPosition;
                dateTime = mSelectDateTime.plusWeeks(i);
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

        if (onWeekCalendarChangedListener != null) {
            onWeekCalendarChangedListener.onWeekCalendarChanged(mSelectDateTime);
        }
    }

    public void setOnWeekCalendarChangedListener(OnWeekCalendarChangedListener onWeekCalendarChangedListener) {
        this.onWeekCalendarChangedListener = onWeekCalendarChangedListener;
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
        WeekView currectMonthView = (WeekView) calendarViews.get(getCurrentItem());
        List<String> localDateList = currectMonthView.getLocalDateList();

        //同一周
        if (localDateList.contains(dateTime.toLocalDate().toString())) {
            initCurrentCalendarView(getCurrentItem());
        } else {
            DateTime initialDateTime = currectMonthView.getInitialDateTime();
            int months = Utils.getIntervalWeek(initialDateTime, dateTime, Attrs.firstDayOfWeek);

            int i = getCurrentItem() + months;
            setCurrentItem(i, false);
        }

    }


    @Override
    public void onClickCurrentWeek(DateTime dateTime) {

        if (dateTime.getMillis() > endDateTime.getMillis() || dateTime.getMillis() < startDateTime.getMillis()) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        WeekView weekView = (WeekView) calendarAdapter.getCalendarViews().get(getCurrentItem());
        weekView.setDateTimeAndPoint(dateTime, pointList);
        mSelectDateTime = dateTime;
        if (onWeekCalendarChangedListener != null) {
            onWeekCalendarChangedListener.onWeekCalendarChanged(dateTime);
        }

    }
}
