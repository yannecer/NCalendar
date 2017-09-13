package com.necer.ncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.Toast;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.NCalendarAdapter;
import com.necer.ncalendar.adapter.NWeekAdapter;
import com.necer.ncalendar.listener.OnClickWeekCalendarListener;
import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.listener.OnWeekCalendarPageChangeListener;
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

    private OnClickWeekCalendarListener onClickWeekCalendarListener;
    private OnWeekCalendarPageChangeListener onWeekCalendarPageChangeListener;

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
        } else if (setDateTime == null) {
            int i = position - lastPosition;
            DateTime dateTime = mSelectDateTime.plusWeeks(i);
            currView.setSelectDateTime(dateTime);
            mSelectDateTime = dateTime;
        }
        lastPosition = position;

        if (onWeekCalendarPageChangeListener != null&& setDateTime == null) {
            onWeekCalendarPageChangeListener.onWeekCalendarPageSelected(mSelectDateTime);
        }


    }

    public void setOnClickWeekCalendarListener(OnClickWeekCalendarListener onClickWeekCalendarListener) {
        this.onClickWeekCalendarListener = onClickWeekCalendarListener;
    }

    public void setOnWeekCalendarPageChangeListener(OnWeekCalendarPageChangeListener onWeekCalendarPageChangeListener) {
        this.onWeekCalendarPageChangeListener = onWeekCalendarPageChangeListener;
    }


    @Override
    public void setDateTime(DateTime dateTime) {

        if (dateTime.getMillis() > endDateTime.getMillis() || dateTime.getMillis() < startDateTime.getMillis()) {
            Toast.makeText(getContext(), R.string.illegal_date, Toast.LENGTH_SHORT).show();
            return;
        }

        this.setDateTime = dateTime;

        SparseArray<NCalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return ;
        }
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

        //跳转处理
        if (onWeekCalendarPageChangeListener != null && setDateTime != null) {
            setDateTime = null;
            onWeekCalendarPageChangeListener.onWeekCalendarPageSelected(mSelectDateTime);
        }
    }


    @Override
    public void onClickCurrentWeek(DateTime dateTime) {

        NWeekView weekView = (NWeekView) calendarAdapter.getCalendarViews().get(getCurrentItem());
        weekView.setSelectDateTime(dateTime);

        mSelectDateTime = dateTime;

        if (onClickWeekCalendarListener != null) {
            onClickWeekCalendarListener.onClickWeekCalendar(dateTime);
        }

    }
}
