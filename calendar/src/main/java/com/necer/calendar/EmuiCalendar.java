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
    public int getGestureMonthUpOffset(int dy) {
        int maxOffset = monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendar.getTop());
        return getOffset(dy, maxOffset);
    }

    @Override
    public int getGestureMonthDownOffset(int dy) {
        int maxOffset = Math.abs(monthCalendar.getTop());
        return getOffset(Math.abs(dy), maxOffset);
    }

    @Override
    public int getChildLayoutDownOffset(int dy) {
        int maxOffset = monthHeigh - childLayout.getTop();
        return getOffset(Math.abs(dy), maxOffset);
    }

    @Override
    public int getGestureChildUpOffset(int dy) {
        int maxOffset = childLayout.getTop() - weekHeigh;
        return getOffset(dy, maxOffset);
    }


    @Override
    protected void gestureMove(int dy, boolean isNest, int[] consumed) {
      //  super.gestureMove(dy, isNest, consumed);


        if (dy > 0 && !childLayout.isWeekState()) {
            monthCalendar.offsetTopAndBottom(-getGestureChildUpOffset(dy));
            childLayout.offsetTopAndBottom(-getGestureChildUpOffset(dy));
            if (isNest) consumed[1] = dy;
            if (monthCalendar.getTop() <= -monthCalendar.getMonthCalendarOffset()) {
                weekCalendar.setVisibility(VISIBLE);
            }

        } else if (dy < 0 && !childLayout.isMonthState()) {
            monthCalendar.offsetTopAndBottom(getChildLayoutDownOffset(dy));
            childLayout.offsetTopAndBottom(getChildLayoutDownOffset(dy));
            if (isNest) consumed[1] = dy;
            if (monthCalendar.getTop() >= -monthCalendar.getMonthCalendarOffset()) {
                weekCalendar.setVisibility(INVISIBLE);
            }
        }









    }
}
