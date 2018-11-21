package com.necer.calendar;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 华为日历
 * Created by necer on 2018/11/14.
 */
public class EmuiCalendar extends NCalendar {
    public EmuiCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onAutoToMonthState() {
        monthCalendar.autoToMonth();
        childLayout.autoToMonth();
    }

    @Override
    protected void onAutoToWeekState() {
        monthCalendar.autoToEMUIWeek();
        childLayout.autoToWeek();
    }

    @Override
    protected int getMonthTopOnWeekState() {
        return weekHeigh - monthHeigh;
    }

    @Override
    protected int getGestureMonthUpOffset(int dy) {
        return getGestureChildUpOffset(dy);
    }

    protected int getGestureMonthDownOffset(int dy) {
        return getGestureChildDownOffset(dy);
    }
    @Override
    protected int getGestureChildDownOffset(int dy) {
        int maxOffset = monthHeigh - childLayout.getTop();
        return getOffset(Math.abs(dy), maxOffset);
    }
    @Override
    protected int getGestureChildUpOffset(int dy) {
        int maxOffset = childLayout.getTop() - weekHeigh;
        return getOffset(dy, maxOffset);
    }


    @Override
    protected void onSetWeekVisible() {
        if (monthCalendar.isWeekState()) {
            weekCalendar.setVisibility(VISIBLE);
        } else if (monthCalendar.getTop() >= -monthCalendar.getMonthCalendarOffset()) {
            weekCalendar.setVisibility(INVISIBLE);
        }
    }

}
