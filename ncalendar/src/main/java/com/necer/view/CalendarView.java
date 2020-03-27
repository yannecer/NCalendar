package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.necer.R;
import com.necer.calendar.BaseCalendar;
import com.necer.drawable.TextDrawable;
import com.necer.enumeration.CalendarType;
import com.necer.helper.CalendarHelper;
import com.necer.painter.CalendarBackground;
import com.necer.painter.CalendarPainter;
import com.necer.utils.CalendarUtil;
import com.necer.utils.DrawableUtil;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * @author necer
 * @date 2018/9/11
 * qq群：127278900
 */
public class CalendarView extends View implements ICalendarView {


    private CalendarHelper mCalendarHelper;

    private int mCurrentDistance = -1;//折叠日历滑动当前的距离 为区分初始化为0和滑动结束为0，故此处初始化为-1

    protected List<LocalDate> mDateList;

    public CalendarView(Context context, BaseCalendar calendar, LocalDate initialDate, CalendarType calendarType) {
        super(context);
        mCalendarHelper = new CalendarHelper(calendar, initialDate, calendarType);
        mDateList = mCalendarHelper.getDateList();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //绘制背景
        CalendarBackground calendarBackground = mCalendarHelper.getCalendarBackground();
        drawBackground(canvas, calendarBackground);

        //绘制日期
        CalendarPainter calendarPainter = mCalendarHelper.getCalendarPainter();
        drawDates(canvas, calendarPainter);
    }

    //绘制背景
    private void drawBackground(Canvas canvas, CalendarBackground calendarBackground) {
        int currentDistance = mCurrentDistance == -1 ? mCalendarHelper.getInitialDistance() : mCurrentDistance;
        Drawable backgroundDrawable = calendarBackground.getBackgroundDrawable(mCalendarHelper.getMiddleLocalDate(), currentDistance, mCalendarHelper.getCalendarHeight());
        Rect backgroundRectF = mCalendarHelper.getBackgroundRectF();
        backgroundDrawable.setBounds(DrawableUtil.getDrawableBounds(backgroundRectF.centerX(), backgroundRectF.centerY(), backgroundDrawable));
        backgroundDrawable.draw(canvas);
    }

    //绘制日期
    private void drawDates(Canvas canvas, CalendarPainter calendarPainter) {

        for (int i = 0; i < mCalendarHelper.getLineNum(); i++) {
            for (int j = 0; j < 7; j++) {
                RectF rectF = mCalendarHelper.getRealRectF(i, j);
                int index = i * 7 + j;
                LocalDate localDate = mDateList.get(index);
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
    }



    @Override
    public LocalDate getPagerInitialDate() {
        return mCalendarHelper.getPagerInitialDate();
    }

    @Override
    public LocalDate getMiddleLocalDate() {
        return mCalendarHelper.getMiddleLocalDate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mCalendarHelper.onTouchEvent(event);
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
    public List<LocalDate> getCurrPagerCheckDateList() {
        return mCalendarHelper.getCurrentSelectDateList();
    }

    @Override
    public void updateSlideDistance(int currentDistance) {
        this.mCurrentDistance = currentDistance;
        invalidate();
    }

    @Override
    public List<LocalDate> getCurrPagerDateList() {
        return mCalendarHelper.getCurrentDateList();
    }

    @Override
    public void notifyCalendarView() {
        invalidate();
    }

    //周或者月的第一天
    @Override
    public LocalDate getCurrPagerFirstDate() {
        return mCalendarHelper.getCurrPagerFirstDate();
    }

    @Override
    public CalendarType getCalendarType() {
        return mCalendarHelper.getCalendarType();
    }


}
