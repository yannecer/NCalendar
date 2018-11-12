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
    protected void gestureMove(int dy, boolean isNest, int[] consumed) {
        if (dy > 0 && !monthCalendar.isWeekState()) {
            //月日历和childView同时上滑
            int offset = monthCalendar.getGestureUpOffset(dy);
            monthCalendar.offsetTopAndBottom(-offset);
            childLayout.offsetTopAndBottom(-offset);
            if (isNest) consumed[1] = dy;
        } else if (dy > 0 && !childLayout.isWeekState()) {
            int offset = childLayout.getGestureUpOffset(dy);
            childLayout.offsetTopAndBottom(-offset);
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && !monthCalendar.isMonthState() && !childLayout.canScrollVertically(-1)) {
            int offset = monthCalendar.getGestureDownOffset(dy);
            monthCalendar.offsetTopAndBottom(offset);
            childLayout.offsetTopAndBottom(offset);
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && !childLayout.isMonthState() && !childLayout.canScrollVertically(-1)) {
            int offset = childLayout.getGestureDownOffset(dy);
            childLayout.offsetTopAndBottom(offset);
            if (isNest) consumed[1] = dy;
        }

        if (childLayout.getTop() == weekHeigh && weekCalendar.getVisibility() == INVISIBLE) {
            weekCalendar.setVisibility(VISIBLE);
        } else if (weekCalendar.getVisibility() == VISIBLE) {
            weekCalendar.setVisibility(INVISIBLE);
        }
    }

}
