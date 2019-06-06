package com.necer.listener;
import com.necer.calendar.BaseCalendar;
import org.joda.time.LocalDate;
import java.util.List;

public interface OnCalendarChangeListener {
    void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currentSelectDateList,List<LocalDate> allSelectDateList);
}

