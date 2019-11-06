package com.necer.painter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.necer.R;

import org.joda.time.LocalDate;

public abstract class CalendarAdapter {


    public abstract View getCalendarView(Context context);

    public abstract void onBindToadyView(View view, LocalDate localDate, boolean isCheck);

    public abstract void onBindCurrentMonthOrWeekView(View view, LocalDate localDate,boolean isCheck);



}
