package com.necer.listener;

import org.joda.time.LocalDate;


/**
 * Created by necer on 2017/6/13.
 */

public interface OnClickMonthViewListener {

    void onClickCurrentMonth(LocalDate localDate);

    void onClickLastMonth(LocalDate localDate);

    void onClickNextMonth(LocalDate localDate);

}
