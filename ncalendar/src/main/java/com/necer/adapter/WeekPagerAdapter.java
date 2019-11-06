package com.necer.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.necer.enumeration.CalendarBuild;
import com.necer.utils.Attrs;
import com.necer.utils.CalendarUtil;
import com.necer.view.CalendarView;
import com.necer.view.WeekView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekPagerAdapter extends BasePagerAdapter {


    public WeekPagerAdapter(Context context, CalendarBuild calendarBuild, LocalDate initializeDate, int count, int currIndex, int firstDayOfWeek, boolean isAllMonthSixLine) {
        super(context, calendarBuild, initializeDate, count, currIndex, firstDayOfWeek, isAllMonthSixLine);
    }

    @Override
    protected CalendarView getCalendarView(ViewGroup container, int position) {
        LocalDate localDate = mInitializeDate.plusDays((position - mCurrIndex) * 7);
        List<LocalDate> dateList = CalendarUtil.getWeekCalendar(localDate, mFirstDayOfWeek);
        return new WeekView(mContext, container, localDate, dateList);
    }

    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {
        return CalendarUtil.getIntervalWeek(startDate, endDate, type);
    }
}
