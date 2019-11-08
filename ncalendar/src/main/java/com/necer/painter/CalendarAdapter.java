package com.necer.painter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.necer.R;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

import java.util.List;

public abstract class CalendarAdapter {


    public View getCalendarBackgroundView(Context context) {
        return null;
    }

    public void onBindCalendarBackgroundView(ICalendarView iCalendarView, View calendarBackgroundView, LocalDate localDate, int totalDistance, int currentDistance) {

    }

    public abstract View getCalendarItemView(Context context);


    public abstract void onBindToadyView(View view, LocalDate localDate, List<LocalDate> selectedDateList);

    public abstract void onBindCurrentMonthOrWeekView(View view, LocalDate localDate, List<LocalDate> selectedDateList);

    public abstract void onBindLastOrNextMonthView(View view, LocalDate localDate, List<LocalDate> selectedDateList);

    public void onBindDisableDateView(View view, LocalDate localDate) {

    }

}
