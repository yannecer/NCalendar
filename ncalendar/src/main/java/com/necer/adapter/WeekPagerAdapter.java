package com.necer.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarBuild;
import com.necer.enumeration.CalendarType;
import com.necer.helper.CalendarHelper;
import com.necer.view.CalendarView;
import com.necer.view.CalendarView2;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekPagerAdapter extends BasePagerAdapter {


    public WeekPagerAdapter(Context context, BaseCalendar baseCalendar) {
        super(context, baseCalendar);
    }

    @Override
    protected ICalendarView getCalendarView(ViewGroup container, int position) {
        LocalDate localDate = mInitializeDate.plusDays((position - mPageCurrIndex) * 7);
       // List<LocalDate> dateList = CalendarUtil.getWeekCalendar(localDate, mFirstDayOfWeek);
        //return new CalendarView(mContext, container, localDate, dateList);

        if (mCalendar.getCalendarBuild() == CalendarBuild.DRAW) {
            // return new MonthView(mContext, container, localDate, dateList);

            // return new CalendarView(mContext, container, localDate, dateList, CalendarType.MONTH);
            return new CalendarView(mContext, new CalendarHelper((BaseCalendar) container, localDate, CalendarType.WEEK));
        } else {
            // return new MonthView2(mContext, container, localDate, dateList);
            return new CalendarView2(mContext, new CalendarHelper((BaseCalendar) container, localDate, CalendarType.WEEK));
        }


        //return new CalendarView(mContext,new CalendarHelper((BaseCalendar) container,localDate, CalendarType.WEEK));
    }

}
