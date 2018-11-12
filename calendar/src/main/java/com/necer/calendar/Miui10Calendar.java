package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


/**
 * Created by necer on 2018/11/12.
 */
public class Miui10Calendar extends NCalendar {


    public Miui10Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void gestureMove(int dy, boolean isNest, int[] consumed) {


        if (dy > 0 && (!monthCalendar.isWeekState() || !childLayout.isWeekState())) {
            //月日历和childView同时上滑
            monthCalendar.offsetTopAndBottom(-getGestureMonthUpOffset(dy));
            childLayout.offsetTopAndBottom(-getGestureChildUpOffset(dy));
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && !childLayout.isMonthState() && !childLayout.canScrollVertically(-1)) {
            monthCalendar.offsetTopAndBottom(getGestureMonthDownOffset(dy));
            childLayout.offsetTopAndBottom(getChildLayoutDownOffset(dy));
            if (isNest) consumed[1] = dy;
        }
    }


    public int getGestureMonthUpOffset(int dy) {
        int maxOffset = monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendar.getTop());

        int monthCalendarOffset = monthCalendar.getMonthCalendarOffset();
        int childLayoutOffset = childLayout.getChildLayoutOffset();

        dy = (monthCalendarOffset * dy) / childLayoutOffset;

        if (dy > maxOffset) {
            return maxOffset;
        } else {
            return dy;
        }

    }


    public int getGestureMonthDownOffset(int dy) {
        int maxOffset = Math.abs(monthCalendar.getTop());


        int monthCalendarOffset = monthCalendar.getMonthCalendarOffset();
        int childLayoutOffset = childLayout.getChildLayoutOffset();

        dy = (monthCalendarOffset * dy) / childLayoutOffset;

        dy = Math.abs(dy);
        if (dy > maxOffset) {
            return maxOffset;
        } else {
            return dy;
        }

    }

    public int getChildLayoutDownOffset(int dy) {
        int maxOffset = monthHeigh - childLayout.getTop();

        dy = Math.abs(dy);
        if (dy > maxOffset) {
            return maxOffset;
        } else {
            return dy;
        }

    }


    public int getGestureChildUpOffset(int dy) {
        int maxOffset = childLayout.getTop() - weekHeigh;

        if (dy > maxOffset) {
            return maxOffset;
        } else {
            return dy;
        }
    }
}
