package com.necer.ncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.Toast;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.NCalendarAdapter;
import com.necer.ncalendar.adapter.NWeekAdapter;
import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.listener.OnWeekCalendarChangedListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.NCalendarView;
import com.necer.ncalendar.view.NWeekView;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by 闫彬彬 on 2017/8/30.
 * QQ:619008099
 */

public class NWeekCalendar extends NCalendarPager implements OnClickWeekViewListener {

    private OnWeekCalendarChangedListener onWeekCalendarChangedListener;

    public NWeekCalendar(Context context) {
        super(context);
    }

    public NWeekCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected NCalendarAdapter getCalendarAdapter() {

        mPageSize = Utils.getIntervalWeek(startDateTime, endDateTime, Attrs.firstDayOfWeek)+1;
        mCurrPage = Utils.getIntervalWeek(startDateTime, mInitialDateTime, Attrs.firstDayOfWeek);

        return new NWeekAdapter(getContext(), mPageSize, mCurrPage, mInitialDateTime, this);
    }


    private int lastPosition = -1;

    @Override
    protected void initCurrentCalendarView(int position) {

        NWeekView currView = (NWeekView) calendarAdapter.getCalendarViews().get(position);
        NWeekView lastView = (NWeekView) calendarAdapter.getCalendarViews().get(position - 1);
        NWeekView nextView = (NWeekView) calendarAdapter.getCalendarViews().get(position + 1);
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

        SparseArray<NCalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return;
        }

        setDateTime = dateTime;
        NWeekView currectMonthView = (NWeekView) calendarViews.get(getCurrentItem());
        List<String> localDateList = currectMonthView.getLocalDateList();

        //同一周
        if (localDateList.contains(dateTime.toLocalDate().toString())) {
            initCurrentCalendarView(getCurrentItem());
        } else {
            DateTime initialDateTime = currectMonthView.getInitialDateTime();
           // int months = Utils.getIntervalWeek(initialDateTime, dateTime);
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

        NWeekView weekView = (NWeekView) calendarAdapter.getCalendarViews().get(getCurrentItem());
        weekView.setDateTimeAndPoint(dateTime, pointList);
        mSelectDateTime = dateTime;
        if (onWeekCalendarChangedListener != null) {
            onWeekCalendarChangedListener.onWeekCalendarChanged(dateTime);
        }

    }
}
