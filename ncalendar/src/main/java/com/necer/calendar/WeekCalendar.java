package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.MyLog;
import com.necer.adapter.BaseCalendarAdapter;
import com.necer.adapter.WeekCalendarAdapter;
import com.necer.entity.NDate;
import com.necer.listener.OnClickWeekViewListener;
import com.necer.listener.OnWeekSelectListener;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;
import com.necer.utils.Util;
import com.necer.view.BaseCalendarView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekCalendar extends BaseCalendar{


    private OnWeekSelectListener onWeekSelectListener;


    public WeekCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WeekCalendar(Context context,Attrs attrs,CalendarPainter calendarPainter) {
        super(context,attrs,calendarPainter);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, int firstDayOfWeek) {
        return new WeekCalendarAdapter(context, startDate, endDate, initializeDate, firstDayOfWeek);
    }

//    @Override
//    protected BaseCalendarAdapter getCalendarAdapter(Context context, Attrs attrs, LocalDate initializeDate) {
//        return new WeekCalendarAdapter(context, attrs.startDateString, initializeDate,this);
//    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalWeek(startDate, endDate, type);
    }

    @Override
    protected LocalDate getDate(LocalDate localDate, int count) {
        return localDate.plusWeeks(count);
    }

    @Override
    protected LocalDate getLastSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusWeeks(-1);
    }

    @Override
    protected LocalDate getNextSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusWeeks(1);
    }

    @Override
    protected void onSelcetDate(NDate date) {
        MyLog.d("onMonthSelectListener:周::" + date.localDate);
        if (onWeekSelectListener != null) {
            onWeekSelectListener.onWeekSelect(date);
        }
    }

    public void setOnWeekSelectListener(OnWeekSelectListener onWeekSelectListener) {
        this.onWeekSelectListener = onWeekSelectListener;
    }

    public LocalDate getPivot() {
        BaseCalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        return currectCalendarView.getPivot();
    }
}
