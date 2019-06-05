package com.necer.adapter;

import android.content.Context;

import com.necer.MyLog;
import com.necer.listener.OnClickWeekViewListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;
import com.necer.view.BaseCalendarView;
import com.necer.view.WeekView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekCalendarAdapter extends BaseCalendarAdapter {
    public WeekCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, int firstDayOfWeek) {
        super(context, startDate, endDate, initializeDate, firstDayOfWeek);
    }

    @Override
    protected BaseCalendarView getCalendarView(int position) {
        LocalDate localDate = mInitializeDate.plusDays((position - mCurr) * 7);
        List<LocalDate> dateList = Util.getWeekCalendar(localDate, mFirstDayOfWeek);

        MyLog.d("localDate:::" + mInitializeDate);
        MyLog.d("localDate:::" + localDate);
        MyLog.d("localDate:::" + dateList);

        return new WeekView(mContext, localDate, dateList);
    }

    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {

        MyLog.d("startDate::::111:::" + startDate);
        MyLog.d("startDate::::333:::" + endDate);


        return Util.getIntervalWeek(startDate, endDate, type);
    }
}
