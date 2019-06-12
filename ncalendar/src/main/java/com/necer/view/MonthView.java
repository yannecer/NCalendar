package com.necer.view;

import android.content.Context;

import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthView extends CalendarView {
    public MonthView(Context context, LocalDate initialDate, List<LocalDate> dateList) {
        super(context, initialDate, dateList);

    }
    @Override
    protected void onClickDate(LocalDate localDate, LocalDate initialDate) {
        if (Util.isLastMonth(localDate, initialDate)) {
            mCalendar.onClickLastMonthDate(localDate);
        } else if (Util.isNextMonth(localDate, initialDate)) {
            mCalendar.onClickNextMonthDate(localDate);
        } else {
            mCalendar.onClickCurrectMonthOrWeekDate(localDate);
        }
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return Util.isEqualsMonth(date, initialDate);
    }

    @Override
    public LocalDate getFirstDate() {
        return new LocalDate(mInitialDate.getYear(), mInitialDate.getMonthOfYear(), 1);
    }
}
