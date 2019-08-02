package com.necer.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.necer.utils.Attrs;
import com.necer.utils.CalendarUtil;
import com.necer.view.CalendarView;
import com.necer.view.MonthView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendarAdapter extends BaseCalendarAdapter {


    public MonthCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, Attrs attrs) {
        super(context, startDate, endDate, initializeDate, attrs);
    }

    @Override
    protected CalendarView getCalendarView(ViewGroup container, int position) {
        LocalDate localDate = mInitializeDate.plusMonths(position - mCurr);
        List<LocalDate> dateList = CalendarUtil.getMonthCalendar(localDate, mAttrs.firstDayOfWeek, mAttrs.isAllMonthSixLine);
        return new MonthView(mContext, container, localDate, dateList);
    }

    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {
        return CalendarUtil.getIntervalMonths(startDate, endDate);
    }
}
