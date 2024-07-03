package com.necer.listener;


import com.necer.enumeration.DateChangeBehavior;

import java.time.LocalDate;

/**
 * 日期变化回调
 *
 * @author necer
 * @date 2017/7/4
 */

public interface OnCalendarChangedListener {

    void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior);
}
