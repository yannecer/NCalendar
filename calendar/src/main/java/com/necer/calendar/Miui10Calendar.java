package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


/**
 * Created by necer on 2018/11/12.
 */
public class Miui10Calendar extends MiuiCalendar {


    public Miui10Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected int getGestureMonthUpOffset(int dy) {

        int maxOffset = monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendar.getTop());
        float monthCalendarOffset = monthCalendar.getMonthCalendarOffset();
        float childLayoutOffset = childLayout.getChildLayoutOffset();

        int offset = (int) ((monthCalendarOffset * dy) / childLayoutOffset);

        if (offset == 0) {
            return getOffset(dy, maxOffset);
        } else {
            return getOffset(offset, maxOffset);
        }
    }

    @Override
    protected int getGestureMonthDownOffset(int dy) {

        int maxOffset = Math.abs(monthCalendar.getTop());
        float monthCalendarOffset = monthCalendar.getMonthCalendarOffset();
        float childLayoutOffset = childLayout.getChildLayoutOffset();
        int offset = (int) ((monthCalendarOffset * dy) / childLayoutOffset);

        if (offset == 0) {
            return getOffset(Math.abs(dy), maxOffset);
        } else {
            return getOffset(Math.abs(offset), maxOffset);
        }
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

}
