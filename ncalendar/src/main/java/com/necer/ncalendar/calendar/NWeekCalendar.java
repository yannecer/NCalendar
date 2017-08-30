package com.necer.ncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;

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


    private DateTime mInitialDateTime;//初始化datetime
    private DateTime mSelectDateTime;//当前页面选中的datetime


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
        mInitialDateTime = new DateTime();
        DateTime startSunFirstDayOfWeek = Utils.getSunFirstDayOfWeek(startDateTime);
        DateTime endSunFirstDayOfWeek = Utils.getSunFirstDayOfWeek(endDateTime);
        DateTime todaySunFirstDayOfWeek = Utils.getSunFirstDayOfWeek(DateTime.now());

        mPageSize = Weeks.weeksBetween(startSunFirstDayOfWeek, endSunFirstDayOfWeek).getWeeks() + 1;
        mCurrPage = Weeks.weeksBetween(startSunFirstDayOfWeek, todaySunFirstDayOfWeek).getWeeks();
        return new NWeekAdapter(getContext(),mPageSize,mCurrPage,mInitialDateTime,this);
    }


    private int lastPosition;

    @Override
    protected void initCurrentCalendarView(int position) {

        NWeekView currView = (NWeekView) calendarAdapter.getCalendarViews().get(position);
        NWeekView lastView = (NWeekView) calendarAdapter.getCalendarViews().get(position - 1);
        NWeekView nextView = (NWeekView) calendarAdapter.getCalendarViews().get(position + 1);

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
            DateTime dateTime1 = dateTime.plusWeeks(1);
            currView.setSelectDateTime(dateTime1);
            mSelectDateTime = dateTime1;
        }

        if (lastPosition > position) {
            //左划
            DateTime dateTime2 = dateTime.plusWeeks(-1);
            currView.setSelectDateTime(dateTime2);
            mSelectDateTime = dateTime2;


        }
        lastPosition = position;
        if (onWeekCalendarPageChangeListener != null) {
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
    protected void setDateTime(DateTime dateTime) {
        int i = jumpDate(dateTime, false);
        NWeekView weekView = (NWeekView) calendarAdapter.getCalendarViews().get(i);
        if (weekView == null) {
            return;
        }
        mSelectDateTime = dateTime;
        weekView.setSelectDateTime(dateTime);
    }

    @Override
    protected int jumpDate(DateTime dateTime, boolean smoothScroll) {
        SparseArray<NCalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return getCurrentItem();
        }

        DateTime initialDateTime = calendarViews.get(getCurrentItem()).getInitialDateTime();
        int weeks = Utils.getIntervalWeek(initialDateTime, dateTime);
        int i = getCurrentItem() + weeks;

        setCurrentItem(i, smoothScroll);
        return i;
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
