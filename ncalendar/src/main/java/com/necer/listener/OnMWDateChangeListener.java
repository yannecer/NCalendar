package com.necer.listener;

import com.necer.calendar.BaseCalendar;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * 月周切换时，中心点变化的回调
 *
 * @author necer
 */
public interface OnMWDateChangeListener {

    /**
     * 月周切换时，中心点变化的回调
     *
     * @param baseCalendar 日历对象
     * @param localDate 当前页面中心点日期
     * @param totalCheckedDateList 所有选中的集合
     */
    void onMwDateChange(BaseCalendar baseCalendar, LocalDate localDate, List<LocalDate> totalCheckedDateList);
}
