package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.utils.Attrs;
import com.necer.utils.AttrsUtil;

import org.joda.time.LocalDate;

/**
 * 华为日历
 * Created by necer on 2018/11/14.
 */
public class EmuiCalendar extends NCalendar {
    public EmuiCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Attrs attrss = AttrsUtil.getAttrs(context, attrs);
        monthCalendar.setBackgroundColor(attrss.bgEmuiCalendarColor);
        weekCalendar.setBackgroundColor(attrss.bgEmuiCalendarColor);
    }

    @Override
    protected float getAutoWeekEndY() {
        return -monthHeight * 4 / 5;
    }


    @Override
    protected float getMonthYOnWeekState(LocalDate localDate) {
        return weekHeight - monthHeight;
    }

    @Override
    protected float getGestureMonthUpOffset(int dy) {
        return getGestureChildUpOffset(dy);
    }

    protected float getGestureMonthDownOffset(int dy) {
        return getGestureChildDownOffset(dy);
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


    @Override
    protected void onSetWeekVisible(int dy) {
        monthCalendar.setVisibility(VISIBLE);
        if (monthCalendar.isWeekState() && dy > 0) {
            weekCalendar.setVisibility(VISIBLE);
        } else if (monthCalendar.getY() >= -monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate()) && dy < 0) {
            weekCalendar.setVisibility(INVISIBLE);

        }
    }

}
