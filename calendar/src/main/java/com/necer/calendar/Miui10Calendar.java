package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.necer.MyLog;


/**
 * Created by necer on 2018/11/12.
 */
public class Miui10Calendar extends NCalendar {


    public Miui10Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    public int getGestureMonthUpOffset(int dy) {


        int maxOffset = monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendar.getTop());

        int monthCalendarOffset = monthCalendar.getMonthCalendarOffset();
        int childLayoutOffset = childLayout.getChildLayoutOffset();
        int offset = (monthCalendarOffset * dy) / childLayoutOffset;
        if (offset == 0) {
            return getOffset(dy, maxOffset);
        } else {
            return getOffset(offset, maxOffset);
        }
    }

    @Override
    public int getGestureMonthDownOffset(int dy) {

        int maxOffset = Math.abs(monthCalendar.getTop());
        int monthCalendarOffset = monthCalendar.getMonthCalendarOffset();
        int childLayoutOffset = childLayout.getChildLayoutOffset();
        int offset = (monthCalendarOffset * dy) / childLayoutOffset;
        if (offset == 0) {
            return getOffset(Math.abs(dy), maxOffset);
        } else {
            return getOffset(Math.abs(offset), maxOffset);
        }
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
