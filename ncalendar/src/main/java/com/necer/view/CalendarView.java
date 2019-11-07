package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarType;
import com.necer.helper.CalendarHelper;
import com.necer.painter.CalendarAdapter;
import com.necer.painter.CalendarPainter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class CalendarView extends View implements ICalendarView {


    private CalendarHelper mCalendarHelper;

    private int mCurrentDistance;//折叠日历滑动当前的距离

    public CalendarView(Context context, CalendarHelper calendarHelper) {
        super(context);
        mCalendarHelper = calendarHelper;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        CalendarPainter calendarPainter = mCalendarHelper.getCalendarPainter();
        //绘制背景
        drawBg(canvas, calendarPainter);
        //绘制日期
        drawDate(canvas, calendarPainter);
    }

    //绘制背景
    private void drawBg(Canvas canvas, CalendarPainter calendarPainter) {
        RectF bgRectF = mCalendarHelper.getBgRectF();
        //bgRectF.set();
        calendarPainter.onDrawCalendarBackground(this, canvas, bgRectF, getMiddleLocalDate(), getMeasuredHeight(), mCurrentDistance);
    }


    //绘制日期
    private void drawDate(Canvas canvas, CalendarPainter calendarPainter) {

        List<RectF> rectFList = mCalendarHelper.getRectFList();
        List<LocalDate> dateList = mCalendarHelper.getDateList();

        for (int i = 0; i < rectFList.size(); i++) {

            LocalDate localDate = dateList.get(i);
            RectF rectF = rectFList.get(i);

            if (mCalendarHelper.isAvailableDate(localDate)) { //可用的日期
                if (mCalendarHelper.isCurrentMonthOrWeek(localDate)) {  //当月日期
                    if (CalendarUtil.isToday(localDate)) {  //当天
                        calendarPainter.onDrawToday(canvas, rectF, localDate, mCalendarHelper.getAllSelectListDate());
                    } else { //非当天的当月日期
                        calendarPainter.onDrawCurrentMonthOrWeek(canvas, rectF, localDate, mCalendarHelper.getAllSelectListDate());
                    }
                } else { //上下月日期
                    calendarPainter.onDrawLastOrNextMonth(canvas, rectF, localDate, mCalendarHelper.getAllSelectListDate());
                }
            } else { //不可用日期
                calendarPainter.onDrawDisableDate(canvas, rectF, localDate);
            }
        }

    }


    @Override
    public LocalDate getInitialDate() {
        return mCalendarHelper.getInitialDate();
    }


    @Override
    public LocalDate getMiddleLocalDate() {
        return mCalendarHelper.getMiddleLocalDate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mCalendarHelper.getGestureDetector().onTouchEvent(event);
    }

    @Override
    public int getDistanceFromTop(LocalDate localDate) {
        return mCalendarHelper.getDistanceFromTop(localDate);
    }


    @Override
    public LocalDate getPivotDate() {
        return mCalendarHelper.getPivotDate();
    }

    @Override
    public int getPivotDistanceFromTop() {
        return mCalendarHelper.getPivotDistanceFromTop();
    }

    @Override
    public List<LocalDate> getCurrentSelectDateList() {
        return mCalendarHelper.getCurrentSelectDateList();
    }

    @Override
    public void updateSlideDistance(int currentDistance) {
        this.mCurrentDistance = currentDistance;
        invalidate();
    }

    @Override
    public List<LocalDate> getCurrentDateList() {
        return mCalendarHelper.getCurrentDateList();
    }

    @Override
    public void notifyCalendarView() {
        invalidate();
    }

    //周或者月的第一天
    @Override
    public LocalDate getFirstDate() {
        return mCalendarHelper.getFirstDate();
    }

}
