package com.necer.painter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.necer.R;

import org.joda.time.LocalDate;

import java.util.List;

public abstract class CalendarAdapter {


    public abstract View getCalendarItemView(Context context);
    public View getCalendarBgView(Context context){
        return null;
    }

    public abstract void onBindToadyView(View view, LocalDate localDate, List<LocalDate> selectedDateList);

    public abstract void onBindCurrentMonthOrWeekView(View view, LocalDate localDate, List<LocalDate> selectedDateList);

    public abstract void onBindLastOrNextMonthView(View view, LocalDate localDate, List<LocalDate> selectedDateList);

    public abstract void onBindDisableDateView(View view, LocalDate localDate);

}
