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
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.NCalendarView;
import com.necer.ncalendar.view.NWeekView;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

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

         DateTime startSunFirstDayOfWeek = Utils.getSunFirstDayOfWeek(startDateTime);
         DateTime endSunFirstDayOfWeek = Utils.getSunFirstDayOfWeek(endDateTime);
         DateTime todaySunFirstDayOfWeek = Utils.getSunFirstDayOfWeek(DateTime.now());
         mPageSize = Weeks.weeksBetween(startSunFirstDayOfWeek, endSunFirstDayOfWeek).getWeeks() + 1;
         mCurrPage = Weeks.weeksBetween(startSunFirstDayOfWeek, todaySunFirstDayOfWeek).getWeeks();
        return new NWeekAdapter(getContext(), mPageSize, mCurrPage, mInitialDateTime, this);
    }


    private int lastPosition=-1;

    @Override
    protected void initCurrentCalendarView(int position) {

        NWeekView currView = (NWeekView) calendarAdapter.getCalendarViews().get(position);
        NWeekView lastView = (NWeekView) calendarAdapter.getCalendarViews().get(position - 1);
        NWeekView nextView = (NWeekView) calendarAdapter.getCalendarViews().get(position + 1);

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
            DateTime dateTime = mSelectDateTime.plusWeeks(i);

            //日期越界
            if (dateTime.getYear() > endDateTime.getYear()) {
                dateTime = endDateTime;
            }  else if (dateTime.getYear() < startDateTime.getYear()) {
                dateTime = startDateTime;
            }

            currView.setSelectDateTime(dateTime);
            mSelectDateTime = dateTime;
        }

        lastPosition = position;

        if (onWeekCalendarChangedListener != null&& !isSetDateTime) {
            onWeekCalendarChangedListener.onWeekCalendarChanged(mSelectDateTime);
        }
    }

    public void setOnWeekCalendarChangedListener(OnWeekCalendarChangedListener onWeekCalendarChangedListener) {
        this.onWeekCalendarChangedListener = onWeekCalendarChangedListener;
    }


    @Override
    public void setDateTime(DateTime dateTime) {

        if (dateTime.getYear() > endDateTime.getYear() || dateTime.getYear() < startDateTime.getYear()) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        SparseArray<NCalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return ;
        }

        isSetDateTime = true;
        DateTime initialDateTime = calendarViews.get(getCurrentItem()).getInitialDateTime();
        int months = Utils.getIntervalWeek(initialDateTime, dateTime);
        int i = getCurrentItem() + months;
        setCurrentItem(i, false);

        NWeekView weekView = (NWeekView) calendarAdapter.getCalendarViews().get(i);
        if (weekView == null) {
            return;
        }

        weekView.setSelectDateTime(dateTime);
        mSelectDateTime = dateTime;

        isSetDateTime = false;

        //跳转处理
        if (onWeekCalendarChangedListener != null ) {
            onWeekCalendarChangedListener.onWeekCalendarChanged(mSelectDateTime);
        }
    }


    @Override
    public void onClickCurrentWeek(DateTime dateTime) {

        if (dateTime.getYear() > endDateTime.getYear() || dateTime.getYear() < startDateTime.getYear()) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        NWeekView weekView = (NWeekView) calendarAdapter.getCalendarViews().get(getCurrentItem());
        weekView.setSelectDateTime(dateTime);
        mSelectDateTime = dateTime;

        if (onWeekCalendarChangedListener != null) {
            onWeekCalendarChangedListener.onWeekCalendarChanged(dateTime);
        }

    }
}
