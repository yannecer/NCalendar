package com.necer.calendar;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;

import com.necer.enumeration.CalendarState;

/**
 * 仿miui9日历
 *
 * @author necer
 * @date 2018/11/7
 */
public class Miui9Calendar extends MiuiCalendar {

    public Miui9Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected float getGestureMonthUpOffset(float dy) {
        float maxOffset;
        if (calendarState == CalendarState.MONTH) {
            //月  月日历有选中则选中为 中心点，如果没有选中则第一行
            maxOffset = monthCalendar.getPivotDistanceFromTop() - Math.abs(monthCalendar.getY());
        } else {
            //周的情况，按照周的第一个数据为中心点
            maxOffset = monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate()) - Math.abs(monthCalendar.getY());
        }
        return getOffset(dy, maxOffset);
    }

    @Override
    protected float getGestureMonthDownOffset(float dy) {
        float maxOffset = Math.abs(monthCalendar.getY());
        return getOffset(Math.abs(dy), maxOffset);
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
