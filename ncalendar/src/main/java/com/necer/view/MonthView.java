package com.necer.view;

import android.content.Context;
import com.necer.entity.NDate;
import com.necer.listener.OnClickMonthViewListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthView extends BaseCalendarView {

    private OnClickMonthViewListener mOnClickMonthViewListener;

    public MonthView(Context context, Attrs attrs, LocalDate localDate, OnClickMonthViewListener onClickMonthViewListener) {
        super(context,attrs,localDate);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }

    @Override
    protected List<NDate> getNCalendar(LocalDate localDate, int type) {
        return Util.getMonthCalendar(localDate,type);
    }

    @Override
    protected void onClick(NDate nDate, LocalDate initialDate) {
        if (Util.isLastMonth(nDate.localDate, initialDate)) {
            mOnClickMonthViewListener.onClickLastMonth(nDate);
        } else if (Util.isNextMonth(nDate.localDate, initialDate)) {
            mOnClickMonthViewListener.onClickNextMonth(nDate);
        } else {
            mOnClickMonthViewListener.onClickCurrentMonth(nDate);
        }
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return Util.isEqualsMonth(date, initialDate);
    }
}
