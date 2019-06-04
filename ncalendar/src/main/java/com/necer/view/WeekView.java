package com.necer.view;

import android.content.Context;

import com.necer.entity.NDate;
import com.necer.listener.OnClickWeekViewListener;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekView extends BaseCalendarView {

   // private OnClickWeekViewListener mOnClickWeekViewListener;

    public WeekView(Context context,LocalDate initialDate, List<LocalDate> dateList) {
        super(context,initialDate, dateList);

    }

    @Override
    protected void onClickDate(LocalDate localDate, LocalDate initialDate) {
//        if (mOnClickWeekViewListener == null) {
//            mOnClickWeekViewListener = (OnClickWeekViewListener) mCalendar;
//        }
//        mOnClickWeekViewListener.onClickCurrentWeek(nDate);
        mCalendar.onClickCurrectWeekDate(localDate);
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return mDateList.contains(new NDate(date));
    }
}
