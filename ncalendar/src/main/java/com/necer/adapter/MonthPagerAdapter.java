package com.necer.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.necer.enumeration.CalendarBuild;
import com.necer.utils.Attrs;
import com.necer.utils.CalendarUtil;
import com.necer.view.ICalendarView;
import com.necer.view.MonthView;
import com.necer.view.MonthView2;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthPagerAdapter extends BasePagerAdapter {


    public MonthPagerAdapter(Context context, CalendarBuild calendarBuild, LocalDate initializeDate, int count, int currIndex, int firstDayOfWeek, boolean isAllMonthSixLine) {
        super(context, calendarBuild, initializeDate, count, currIndex, firstDayOfWeek, isAllMonthSixLine);
    }

    @Override
    protected ICalendarView getCalendarView(ViewGroup container, int position) {
        LocalDate localDate = mInitializeDate.plusMonths(position - mCurrIndex);
        List<LocalDate> dateList = CalendarUtil.getMonthCalendar(localDate, mFirstDayOfWeek, mIsAllMonthSixLine);

       // return new MonthView(mContext, container, localDate, dateList);

        if (mCalendarBuild == CalendarBuild.DRAW) {
            return new MonthView(mContext, container, localDate, dateList);
        } else {
            return new MonthView2(mContext, container, localDate, dateList);
        }
       // return new MonthView2(mContext, container, localDate, dateList);
    }

    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {
        return CalendarUtil.getIntervalMonths(startDate, endDate);
    }
}
