package com.necer.painter;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import org.joda.time.LocalDate;


/**
 * @author necer
 * @date 2020/3/27
 */
public interface CalendarBackground {

    /**
     * 月日历和周日历的背景，背景用 Drawable 实现
     *
     * @param localDate       当前页面 中心点的日期
     * @param currentDistance 月周折叠日历中当前滑动的距离
     * @param totalDistance   月周折叠日历中可滑动的总距离
     * @return 返回日历背景Drawable
     */
    Drawable getBackgroundDrawable(LocalDate localDate, int currentDistance, int totalDistance);

}
