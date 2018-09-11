package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.necer.MyLog;
import com.necer.entity.NCalendar;
import com.necer.listener.OnClickMonthViewListener;
import com.necer.listener.OnClickWeekViewListener;
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
    protected NCalendar getNCalendar(LocalDate localDate, int type) {
        return Util.getMonthCalendar2(localDate,type);
    }

    @Override
    protected void onClick(LocalDate clickData, LocalDate initialDate) {
        if (Util.isLastMonth(clickData, initialDate)) {
            mOnClickMonthViewListener.onClickLastMonth(clickData);
        } else if (Util.isNextMonth(clickData, initialDate)) {
            mOnClickMonthViewListener.onClickNextMonth(clickData);
        } else {
            mOnClickMonthViewListener.onClickCurrentMonth(clickData);
        }
    }
}
