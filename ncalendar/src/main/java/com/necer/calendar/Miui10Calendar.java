package com.necer.calendar;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;

import com.necer.enumeration.CalendarState;


/**
 * 仿miui10日历
 *
 * @author necer
 * @date 2018/11/12
 */
public class Miui10Calendar extends MiuiCalendar {


    public Miui10Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     */
    @Override
    protected float getGestureMonthUpOffset(float dy) {
        float maxOffset;
        float monthCalendarOffset;
        if (calendarState == CalendarState.MONTH) {
            maxOffset = monthCalendar.getPivotDistanceFromTop() - Math.abs(monthCalendar.getY());
            monthCalendarOffset = monthCalendar.getPivotDistanceFromTop();
        } else {
            maxOffset = monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate()) - Math.abs(monthCalendar.getY());
            monthCalendarOffset = monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate());
        }
        float childLayoutOffset = monthHeight - weekHeight;
        float offset = ((monthCalendarOffset * dy) / childLayoutOffset);
        return getOffset(offset, maxOffset);
    }

    /**
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     */
    @Override
    protected float getGestureMonthDownOffset(float dy) {
        float maxOffset = Math.abs(monthCalendar.getY());
        float monthCalendarOffset;
        if (calendarState == CalendarState.MONTH) {
            monthCalendarOffset = monthCalendar.getPivotDistanceFromTop();
        } else {
            monthCalendarOffset = monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate());
        }
        float childLayoutOffset = monthHeight - weekHeight;
        float offset = ((monthCalendarOffset * dy) / childLayoutOffset);
        return getOffset(Math.abs(offset), maxOffset);
    }

    @Override
    protected float getGestureChildDownOffset(float dy) {
        float maxOffset = monthHeight - childView.getY();
        return getOffset(Math.abs(dy), maxOffset);
    }

    @Override
    protected float getGestureChildUpOffset(float dy) {
        float maxOffset = childView.getY() - weekHeight;
        return getOffset(dy, maxOffset);
    }
}
