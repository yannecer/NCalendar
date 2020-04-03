package com.necer.listener;

import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.DateChangeBehavior;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * @author necer
 * @date 2017/7/4
 */

public interface OnCalendarMultipleChangedListener {


    /**
     * 多选模式 日历变化回调，月日历、周日历、折叠日历（miui9，miui10，emui）都用这一个回调
     *
     * @param baseCalendar         日历对象，MonthCalendar和WeekCalendar
     * @param year                 日历当前页面中间日期->年
     * @param month                日历当前页面中间日期->月
     * @param currPagerCheckedList 当前页面选中的日期集合，无选中则为空集合
     * @param totalCheckedList     日历总共的选中集合，无选中则为空集合
     * @param dateChangeBehavior   日历变化行为 参照 DateChangeBehavior
     */
    void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior);
}
