package com.necer.painter;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * @author necer
 * @date 2019/1/3
 */
public interface CalendarPainter {


    /**
     * 绘制今天的日期
     *
     * @param canvas          画布
     * @param rectF           当前日期得位置矩形
     * @param localDate       当前日期
     * @param checkedDateList 全部选中的日期集合
     */
    void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList);

    /**
     * 绘制当前月或周的日期
     *
     * @param canvas          画布
     * @param rectF           当前日期得位置矩形
     * @param localDate       当前日期
     * @param checkedDateList 全部选中的日期集合
     */
    void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList);

    /**
     * 绘制上一月，下一月的日期，周日历不用实现
     *
     * @param canvas          画布
     * @param rectF           当前日期得位置矩形
     * @param localDate       当前日期
     * @param checkedDateList 全部选中的日期集合
     */
    void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList);

    /**
     * 绘制不可用的日期，和方法setDateInterval(startFormatDate, endFormatDate)对应,
     * 如果没有使用setDateInterval设置日期范围 此方法不用实现
     *
     * @param canvas    画布
     * @param rectF     当前日期得位置矩形
     * @param localDate 当前日期
     */
    default void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate) {

    }


}
