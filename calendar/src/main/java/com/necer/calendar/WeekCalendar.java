package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Toast;

import com.necer.adapter.BaseCalendarAdapter;
import com.necer.adapter.WeekCalendarAdapter;
import com.necer.listener.OnClickWeekViewListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekCalendar extends BaseCalendar implements OnClickWeekViewListener {
    public WeekCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, Attrs attrs, int calendarSize, int currNum) {
        return new WeekCalendarAdapter(context,attrs,calendarSize,currNum,this);
    }

    @Override
    protected int getCalendarSize(LocalDate startDate, LocalDate endDate,int type) {
        return Util.getIntervalWeek(startDate, endDate, type) + 1;
    }

    @Override
    protected int getCurrNum(LocalDate startDate,int type) {
        return Util.getIntervalWeek(startDate, new LocalDate(), type);
    }

    @Override
    public void onClickCurrentWeek(LocalDate date) {

        Toast.makeText(getContext(),date.toString(),Toast.LENGTH_SHORT).show();

    }
}
