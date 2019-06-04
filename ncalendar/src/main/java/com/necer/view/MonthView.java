package com.necer.view;

import android.content.Context;

import com.necer.entity.NDate;
import com.necer.listener.OnClickMonthViewListener;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthView extends BaseCalendarView {

    // private OnClickMonthViewListener mOnClickMonthViewListener;

    public MonthView(Context context, LocalDate initialDate, List<LocalDate> dateList) {
        super(context, initialDate, dateList);

    }


    @Override
    protected void onClickDate(LocalDate localDate, LocalDate initialDate) {
//        if (mOnClickMonthViewListener == null) {
//            mOnClickMonthViewListener = (OnClickMonthViewListener) mCalendar;
//        }
        if (Util.isLastMonth(localDate, initialDate)) {
            //mOnClickMonthViewListener.onClickLastMonth(localDate);
            mCalendar.onClickLastMonthDate(localDate);
        } else if (Util.isNextMonth(localDate, initialDate)) {
            // mOnClickMonthViewListener.onClickNextMonth(localDate);
            mCalendar.onClickNextMonthDate(localDate);
        } else {
            //  mOnClickMonthViewListener.onClickCurrentMonth(localDate);
            mCalendar.onClickCurrectMonthDate(localDate);
        }
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return Util.isEqualsMonth(date, initialDate);
    }
}
