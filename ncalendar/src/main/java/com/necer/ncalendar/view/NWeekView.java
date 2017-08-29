package com.necer.ncalendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NWeekView extends NCalendarView{

    public NWeekView(Context context) {
        this(context,null);
    }

    public NWeekView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NWeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        Utils.NCalendar weekCalendar2 = Utils.getWeekCalendar2(new DateTime(), 0);

        List<DateTime> dateTimeList = weekCalendar2.dateTimeList;

/*
        for (int i = 0; i < dateTimeList.size(); i++) {
            MyLog.d("dateTimeList::" + dateTimeList.get(i));
        }
        this.mInitialDateTime = dateTime;

        mRectList = new ArrayList<>();
        dateTimes = Utils.getMonthCalendar2(dateTime, 0);
        mRowNum = dateTimes.size() / 7;
        mOnClickMonthViewListener = onClickMonthViewListener;*/

    }




}
