package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by necer on 2018/11/7.
 */
public class Miui9Calendar extends MiuiCalendar {


    public Miui9Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getGestureMonthUpOffset(int dy) {
        int maxOffset = monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendar.getTop());
        return getOffset(dy, maxOffset);
    }

    @Override
    protected int getGestureMonthDownOffset(int dy) {
        int maxOffset = Math.abs(monthCalendar.getTop());
        return getOffset(Math.abs(dy), maxOffset);
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


}
