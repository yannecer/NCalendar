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
     * @param rectF           今天日期的位置矩形
     * @param localDate       今天的日期
     * @param checkedDateList 所有选中日期的集合，
     *                        单选模式中，集合中最多只有一个元素，
     *                        多选模式中，可以有多个元素
     */
    void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList);

    /**
     * 绘制当前月或周的日期
     *
     * @param canvas          画布
     * @param rectF           日期的位置矩形
     * @param localDate       月日历-> 当前页面月份中的每个日期（内部是循环处理）
     *                        周日历-> 当前页面一周中的每个日期（内部是循环处理）
     * @param checkedDateList 所有选中日期的集合，
     *                        单选模式中，集合中最多只有一个元素，
     *                        多选模式中，可以有多个元素
     */
    void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList);

    /**
     * 绘制上一月，下一月的日期，周日历不用实现
     *
     * @param canvas          画布
     * @param rectF           日期的位置矩形
     * @param localDate       月日历-> 当前页面上一月、下一月中的每个日期（内部是循环处理）
     *                        周日历-> 周日历无需实现此方法
     * @param checkedDateList 所有选中日期的集合，
     *                        单选模式中，集合中最多只有一个元素，
     *                        多选模式中，可以有多个元素
     */
    void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList);

    /**
     * 绘制不可用的日期，和方法setDateInterval(startFormatDate, endFormatDate)对应,
     * 如果没有使用setDateInterval设置日期范围 此方法不用实现
     *
     * @param canvas    画布
     * @param rectF     日期的位置矩形
     * @param localDate 不可用的日期
     */
    default void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate) {

    }


}
