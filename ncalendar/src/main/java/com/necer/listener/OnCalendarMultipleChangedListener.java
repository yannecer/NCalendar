package com.necer.listener;

import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.DateChangeBehavior;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2017/7/4.
 */

public interface OnCalendarMultipleChangedListener {
    void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior);
}
