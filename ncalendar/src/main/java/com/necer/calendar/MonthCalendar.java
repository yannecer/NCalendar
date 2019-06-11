package com.necer.calendar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.MyLog;
import com.necer.adapter.BaseCalendarAdapter;
import com.necer.adapter.MonthCalendarAdapter;
import com.necer.adapter.WeekCalendarAdapter;
import com.necer.entity.NDate;
import com.necer.listener.OnClickMonthViewListener;
import com.necer.listener.OnMonthAnimatorListener;
import com.necer.listener.OnMonthSelectListener;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;
import com.necer.utils.Util;
import com.necer.view.BaseCalendarView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendar extends BaseCalendar {


    private OnMonthSelectListener onMonthSelectListener;

    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, int firstDayOfWeek) {
        return new MonthCalendarAdapter(context, startDate, endDate, initializeDate, firstDayOfWeek);
    }

    public MonthCalendar(Context context, Attrs attrs, CalendarPainter calendarPainter) {
        super(context, attrs, calendarPainter);
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate);
    }

    @Override
    protected LocalDate getDate(LocalDate localDate, int count) {
        LocalDate date = localDate.plusMonths(count);
        return date;
    }

    @Override
    protected LocalDate getLastSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusMonths(-1);
    }

    @Override
    protected LocalDate getNextSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusMonths(1);
    }

    @Override
    protected void onSelcetDate(NDate nDate) {
        if (onMonthSelectListener != null) {
            onMonthSelectListener.onMonthSelect(nDate);
        }
    }

    public void setOnMonthSelectListener(OnMonthSelectListener onMonthSelectListener) {
        this.onMonthSelectListener = onMonthSelectListener;
    }




    public void autoToMonth() {
        float top = getY();//起始位置
        int end = 0;
        //   monthValueAnimator.setFloatValues(top, end);
        //   monthValueAnimator.start();
    }


    public void autoToMIUIWeek() {
        float top = getY();//起始位置


        int end = -getPivotDistanceFromTop(); //结束位置
        // monthValueAnimator.setFloatValues(top, end);
        //  monthValueAnimator.start();
    }

    public void autoToEMUIWeek() {
        float top = getY();//起始位置
        int end = -getHeight() * 4 / 5; //结束位置
        //monthValueAnimator.setFloatValues(top, end);
        // monthValueAnimator.start();
    }


    public boolean isMonthState() {
        return getY() >= 0;
    }

    public boolean isWeekState() {
        return getY() <= -getPivotDistanceFromTop();
    }

}
