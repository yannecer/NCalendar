package com.necer.ncalendar.listener;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/6/13.
 */

public interface OnClickMonthViewListener {

    void onClickCurrentMonth(LocalDate date);

    void onClickLastMonth(LocalDate date);

    void onClickNextMonth(LocalDate date);

}
