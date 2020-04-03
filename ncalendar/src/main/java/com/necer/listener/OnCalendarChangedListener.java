package com.necer.listener;

import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.DateChangeBehavior;

import org.joda.time.LocalDate;

/**
 * 日期变化回调
 *
 * @author necer
 * @date 2017/7/4
 */

public interface OnCalendarChangedListener {

    /**
     * 单选模式 日历变化回调，月日历、周日历、折叠日历（NCalendar）都用这一个回调
     *
     * @param baseCalendar       日历对象，MonthCalendar和WeekCalendar
     * @param year               日历当前页面中间日期->年
     * @param month              日历当前页面中间日期->月
     * @param localDate          日历当前页面选中日期，有选中则返回选中日期，无选中则返回null
     * @param dateChangeBehavior 日历变化行为 参照 DateChangeBehavior
     */
    void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior);
}
