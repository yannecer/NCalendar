package com.necer.view;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.necer.entity.NCalendar;
import com.necer.listener.OnClickWeekViewListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekView extends BaseCalendarView {

    private OnClickWeekViewListener mOnClickWeekViewListener;

    public WeekView(Context context, Attrs attrs, LocalDate localDate, OnClickWeekViewListener onClickWeekViewListener) {
        super(context, attrs, localDate);
        this.mOnClickWeekViewListener = onClickWeekViewListener;
    }

    @Override
    protected NCalendar getNCalendar(LocalDate localDate, int type) {
        return Util.getWeekCalendar2(localDate,type);
    }

    @Override
    protected void onClick(LocalDate clickData, LocalDate initialDate) {
        mOnClickWeekViewListener.onClickCurrentWeek(clickData);
    }

    @Override
    protected boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return true;
    }
}
