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
            //  int monthOffset = monthCalendar.getGestureUpOffset(dy);
            //  int childLayoutOffset = childLayout.getGestureUpOffset((dy*2));
            monthCalendar.offsetTopAndBottom(-getGestureMonthUpOffset(dy/2));
            childLayout.offsetTopAndBottom(-getGestureChildUpOffset(dy/2));
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && !childLayout.isMonthState() && !childLayout.canScrollVertically(-1)) {
            // int monthOffset = monthCalendar.getGestureDownOffset(dy);
            // int childLayoutOffset = childLayout.getGestureDownOffset(dy*2);
            monthCalendar.offsetTopAndBottom(getGestureMonthDownOffset(dy/2));
            childLayout.offsetTopAndBottom(getChildLayoutDownOffset(dy/2));
            if (isNest) consumed[1] = dy;
        }
    }


    public int getGestureMonthUpOffset(int dy) {
        int monthCalendarOffset = monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendar.getTop());

        if (dy > monthCalendarOffset) {
            return monthCalendarOffset;
        } else {
            return dy;
        }

    }


    public int getGestureMonthDownOffset(int dy) {
        int maxOffset = Math.abs(monthCalendar.getTop());
        dy = Math.abs(dy);
        if (dy > maxOffset) {
            return maxOffset;
        } else {
            return dy;
        }

    }

    public int getChildLayoutDownOffset(int dy) {
        int maxOffset = monthHeigh - childLayout.getTop();

        int monthCalendarOffset1 = monthCalendar.getMonthCalendarOffset();
        int childLayoutOffset1 = childLayout.getChildLayoutOffset();

        if (monthCalendarOffset1 != 0) {
            dy = (childLayoutOffset1 * dy) / monthCalendarOffset1;
        }


        dy = Math.abs(dy);
        if (dy > maxOffset) {
            return maxOffset;
        } else {
            return dy;
        }

    }


    public int getGestureChildUpOffset(int dy) {
        int childLayoutOffset = childLayout.getTop() - weekHeigh;
        int monthCalendarOffset1 = monthCalendar.getMonthCalendarOffset();
        int childLayoutOffset1 = childLayout.getChildLayoutOffset();


        if (monthCalendarOffset1 != 0) {
            dy = (childLayoutOffset1 * dy) / monthCalendarOffset1;
        }

        if (dy > childLayoutOffset) {
            return childLayoutOffset;
        } else {
            return dy;
        }
    }


}
