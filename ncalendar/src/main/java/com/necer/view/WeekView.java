package com.necer.view;

import android.content.Context;
import android.view.ViewGroup;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekView extends CalendarView {


    public WeekView(Context context, ViewGroup container, LocalDate initialDate, List<LocalDate> dateList) {
        super(context, container, initialDate, dateList);
    }

    @Override
    protected void dealClickDate(LocalDate localDate) {
        mCalendar.onClickCurrectMonthOrWeekDate(localDate);
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return mDateList.contains(date);
    }

    @Override
    public LocalDate getFirstDate() {
        return mDateList.get(0);
    }
}
