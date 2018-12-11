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
    protected float getGestureMonthUpOffset(int dy) {
        float maxOffset = monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendar.getY());
        return getOffset(dy, maxOffset);
    }

    @Override
    protected float getGestureMonthDownOffset(int dy) {
        float maxOffset = Math.abs(monthCalendar.getY());
        return getOffset(Math.abs(dy), maxOffset);
    }

    @Override
    protected float getGestureChildDownOffset(int dy) {
        float maxOffset = monthHeight - childLayout.getY();
        return getOffset(Math.abs(dy), maxOffset);
    }



    @Override
    protected float getGestureChildUpOffset(int dy) {
        float maxOffset = childLayout.getY() - weekHeight;
        return getOffset(dy, maxOffset);
    }


}
