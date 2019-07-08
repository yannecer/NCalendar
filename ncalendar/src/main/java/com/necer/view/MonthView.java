package com.necer.view;

import android.content.Context;
import android.view.ViewGroup;

import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthView extends CalendarView {
    public MonthView(Context context, ViewGroup container, LocalDate initialDate, List<LocalDate> dateList) {
        super(context, container, initialDate, dateList);

    }

    @Override
    protected void dealClickDate(LocalDate localDate) {
        if (CalendarUtil.isLastMonth(localDate, mInitialDate)) {
            mCalendar.onClickLastMonthDate(localDate);
        } else if (CalendarUtil.isNextMonth(localDate, mInitialDate)) {
            mCalendar.onClickNextMonthDate(localDate);
        } else {
            mCalendar.onClickCurrectMonthOrWeekDate(localDate);
        }
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return CalendarUtil.isEqualsMonth(date, initialDate);
    }

    @Override
    public LocalDate getFirstDate() {
        return new LocalDate(mInitialDate.getYear(), mInitialDate.getMonthOfYear(), 1);
    }
}
