package com.necer.view;
import android.content.Context;
import com.necer.entity.NDate;
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
    protected List<NDate> getNCalendar(LocalDate localDate, int type) {
        return Util.getWeekCalendar(localDate,type);
    }

    @Override
    protected void onClick(NDate nDate, LocalDate initialDate) {
        mOnClickWeekViewListener.onClickCurrentWeek(nDate);
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return mDateList.contains(new NDate(date));
    }
}
