package com.necer.view;

import com.necer.enumeration.CalendarType;

import org.joda.time.LocalDate;

import java.util.List;

public interface ICalendarView {


    /**
     * 获取当前页面的初始化日期
     *
     * @return
     */
    LocalDate getPagerInitialDate();

    /**
     * 获取中间的日期，周日历以中间的日期判断当前页面的年和月
     *
     * @return
     */
    LocalDate getMiddleLocalDate();


    /**
     * 选中的日期到顶部的距离
     *
     * @return
     */
    int getDistanceFromTop(LocalDate localDate);


    /**
     * 获取折叠的中心点 如果有当前页面有选中 返回选中的日期，如果没有选中看是否包含今天，如果没有就返回当前页面第一个日期
     *
     * @return
     */
    LocalDate getPivotDate();

    /**
     * 获取中心点到顶部的距离
     *
     * @return
     */
    int getPivotDistanceFromTop();


    /**
     * 获取当前页面选中的日期
     *
     * @return
     */
    List<LocalDate> getCurrentSelectDateList();

    /**
     * 月周切换时滑动的距离
     *
     * @param currentDistance
     */
    void updateSlideDistance(int currentDistance);


    /**
     * 获取当前页面的日期
     *
     * @return
     */
    List<LocalDate> getCurrentDateList();


    void notifyCalendarView();


    LocalDate getFirstDate();

    CalendarType getCalendarType();

}
