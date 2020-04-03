package com.necer.calendar;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.enumeration.CalendarState;

import org.joda.time.LocalDate;


/**
 * 仿miui日历抽象类
 *
 * @author necer
 * @date 2018/11/12
 */
public abstract class MiuiCalendar extends NCalendar {

    public MiuiCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected float getMonthYOnWeekState(LocalDate localDate) {
        return -monthCalendar.getDistanceFromTop(localDate);
    }

    @Override
    protected float getMonthCalendarAutoWeekEndY() {
        float end;
        if (calendarState == CalendarState.MONTH) {
            //月  月日历有选中则选中为 中心点，如果没有选中则第一行
            end = -monthCalendar.getPivotDistanceFromTop();
        } else {
            //周的情况，按照周的第一个数据为中心点
            end = -monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate());
        }

        return end;
    }

    @Override
    protected void setWeekVisible(boolean isUp) {

        if (isChildWeekState()) {
            if (weekCalendar.getVisibility() != VISIBLE) {
                weekCalendar.setVisibility(VISIBLE);
            }

            if (monthCalendar.getVisibility() != INVISIBLE) {
                monthCalendar.setVisibility(INVISIBLE);
            }

        } else {
            if (weekCalendar.getVisibility() != INVISIBLE) {
                weekCalendar.setVisibility(INVISIBLE);
            }

            if (monthCalendar.getVisibility() != VISIBLE) {
                monthCalendar.setVisibility(VISIBLE);
            }
        }
    }

}
