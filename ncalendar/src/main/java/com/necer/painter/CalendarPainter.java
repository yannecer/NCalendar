package com.necer.painter;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.necer.calendar.BaseCalendar;
import com.necer.view.CalendarView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2019/1/3.
 */
public interface CalendarPainter {

    //绘制月日历或这日历背景，如数字背景等
    void onDrawCalendarBackground(CalendarView calendarView, Canvas canvas, RectF rectF, LocalDate localDate, int totalDistance, int currentDistance);

    //绘制今天的日期
    void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    //绘制当前月或周的日期
    void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    //绘制上一月，下一月的日期，周日历不用实现
    void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    //绘制不可用的日期，和方法setDateInterval(startFormatDate, endFormatDate)对应,如果没有使用setDateInterval设置日期范围 此方法不用实现
    void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate);



}
