package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Toast;

import com.necer.adapter.BaseCalendarAdapter;
import com.necer.adapter.WeekCalendarAdapter;
import com.necer.entity.NDate;
import com.necer.listener.OnClickWeekViewListener;
import com.necer.listener.OnWeekSelectListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekCalendar extends BaseCalendar implements OnClickWeekViewListener {


    private OnWeekSelectListener onWeekSelectListener;

    public WeekCalendar(Context context,Attrs attrs) {
        super(context,attrs);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, Attrs attrs) {
        return new WeekCalendarAdapter(context, attrs, this);
    }

    public WeekCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalWeek(startDate, endDate, type);
    }

    @Override
    protected LocalDate getDate(LocalDate localDate, int count) {
        return localDate.plusWeeks(count);
    }

    @Override
    protected LocalDate getLastSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusWeeks(-1);
    }

    @Override
    protected LocalDate getNextSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusWeeks(1);
    }

    @Override
    protected void onSelcetDate(NDate date) {
        mOnClickDate = date.localDate;
        if (onWeekSelectListener != null) {
            onWeekSelectListener.onWeekSelect(date);
        }
    }

    @Override
    public void onClickCurrentWeek(LocalDate localDate) {
        if (isClickDateEnable(localDate)) {
            onSelcetDate(Util.getNDate(localDate));
            onDateChanged(localDate, true);
            onYearMonthChanged(localDate.getYear(), localDate.getMonthOfYear());
            notifyView(localDate, true);
        } else {
            Toast.makeText(getContext(),"不可用",Toast.LENGTH_SHORT).show();
        }

    }


    public void setOnWeekSelectListener(OnWeekSelectListener onWeekSelectListener) {
        this.onWeekSelectListener = onWeekSelectListener;
    }
}
