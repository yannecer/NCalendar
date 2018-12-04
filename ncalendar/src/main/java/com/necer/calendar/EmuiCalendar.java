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
        return weekHeight - monthHeight;
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
        int maxOffset = monthHeight - childLayout.getTop();
        return getOffset(Math.abs(dy), maxOffset);
    }
    @Override
    protected int getGestureChildUpOffset(int dy) {
        int maxOffset = childLayout.getTop() - weekHeight;
        return getOffset(dy, maxOffset);
    }


    @Override
    protected void onSetWeekVisible(int dy) {

        if (monthCalendar.isWeekState() && dy>0) {
            weekCalendar.setVisibility(VISIBLE);
        } else if (monthCalendar.getTop() >= -monthCalendar.getMonthCalendarOffset() && dy < 0) {
            weekCalendar.setVisibility(INVISIBLE);
        }
    }

}
