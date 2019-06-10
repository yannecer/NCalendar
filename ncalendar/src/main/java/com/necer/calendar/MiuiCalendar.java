package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.MyLog;

import org.joda.time.LocalDate;


/**
 * Created by necer on 2018/11/15.
 */
public abstract class MiuiCalendar extends NCalendar{

    public MiuiCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onAutoToMonthState() {
        monthCalendar.autoToMonth();
        childLayout.autoToMonth();
    }

    @Override
    protected void onAutoToWeekState() {







        monthCalendar.autoToMIUIWeek();
        childLayout.autoToWeek();
    }

    @Override
    protected float getMonthYOnWeekState(LocalDate localDate) {




        return -monthCalendar.getMonthCalendarOffset(localDate);
    }

    @Override
    protected void onSetWeekVisible(int dy) {

        if (childLayout.isWeekState() ) {
            weekCalendar.setVisibility(VISIBLE);
            monthCalendar.setVisibility(INVISIBLE);
        } else  {
            weekCalendar.setVisibility(INVISIBLE);
            monthCalendar.setVisibility(VISIBLE);
        }
    }

}
