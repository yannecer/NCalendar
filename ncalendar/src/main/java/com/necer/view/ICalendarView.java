package com.necer.view;

import com.necer.enumeration.CalendarType;
import com.necer.painter.CalendarBackground;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * @author necer
 */
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
    List<LocalDate> getCurrPagerCheckDateList();

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
    List<LocalDate> getCurrPagerDateList();


    /**
     * 刷新当前页面
     */
    void notifyCalendarView();


    /**
     * 获取当前页面的第一个数据
     * @return
     */
    LocalDate getCurrPagerFirstDate();

    /**
     * 获取日历的类型
     * @return CalendarType.MONTH -> 月日历
     *         CalendarType.WEEK -> 周日历
     */
    CalendarType getCalendarType();

}
