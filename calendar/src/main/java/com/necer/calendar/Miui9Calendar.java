package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by necer on 2018/11/7.
 */
public class Miui9Calendar extends NCalendar {


    public Miui9Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
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


}
