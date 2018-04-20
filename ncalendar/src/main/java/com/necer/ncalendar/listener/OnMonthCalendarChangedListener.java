package com.necer.ncalendar.listener;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/9/20.
 * QQ群:127278900
 */

public interface OnMonthCalendarChangedListener {
    void onMonthCalendarChanged(LocalDate date);
}
