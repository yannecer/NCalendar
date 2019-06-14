package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.utils.Attrs;


/**
 * Created by necer on 2018/11/12.
 */
public class Miui10Calendar extends MiuiCalendar {


    public Miui10Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return
     */
    @Override
    protected float getGestureMonthUpOffset(int dy) {


        float maxOffset;
        float monthCalendarOffset;

        if (STATE == Attrs.MONTH) {
            maxOffset = monthCalendar.getPivotDistanceFromTop() - Math.abs(monthCalendar.getY());
            monthCalendarOffset = monthCalendar.getPivotDistanceFromTop();
        } else {
            maxOffset = monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate()) - Math.abs(monthCalendar.getY());
            monthCalendarOffset = monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate());
        }

        float childLayoutOffset = monthHeight - weekHeight;

        float offset = ((monthCalendarOffset * dy) / childLayoutOffset);
        return getOffset(offset, maxOffset);
    }

    /**
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return
     */
    @Override
    protected float getGestureMonthDownOffset(int dy) {
        float maxOffset = Math.abs(monthCalendar.getY());

        float monthCalendarOffset;
        if (STATE == Attrs.MONTH) {
            monthCalendarOffset = monthCalendar.getPivotDistanceFromTop();
        } else {
            monthCalendarOffset = monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate());
        }

        float childLayoutOffset = monthHeight - weekHeight;
        float offset = ((monthCalendarOffset * dy) / childLayoutOffset);
        return getOffset(Math.abs(offset), maxOffset);
    }

    @Override
    protected float getGestureChildDownOffset(int dy) {
        float maxOffset = monthHeight - childView.getY();
        return getOffset(Math.abs(dy), maxOffset);
    }

    @Override
    protected float getGestureChildUpOffset(int dy) {
        float maxOffset = childView.getY() - weekHeight;
        return getOffset(dy, maxOffset);
    }

}
