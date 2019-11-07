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

    protected List<LocalDate> mDateList;
    protected List<RectF> mRectFList;

    public CalendarView(Context context, CalendarHelper calendarHelper) {
        super(context);
        mCalendarHelper = calendarHelper;
        mDateList = calendarHelper.getDateList();
        mRectFList = new ArrayList<>();

        for (int i = 0; i < mDateList.size(); i++) {
            mRectFList.add(new RectF());
        }

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


        int lineNum = mCalendarHelper.getLineNum();


        for (int i = 0; i < lineNum; i++) {
            for (int j = 0; j < 7; j++) {
                int index = i * 7 + j;
                RectF rectF = mRectFList.get(index);
                //矩形确定位置
                float width = getMeasuredWidth();
                float height = getMeasuredHeight();
                //为每个矩形确定位置
                if (lineNum == 5 || lineNum == 1) {
                    //5行的月份，5行矩形平分view的高度  mLineNum==1是周的情况
                    float rectHeight = height / lineNum;
                    rectF.set(j * width / 7, i * rectHeight, j * width / 7 + width / 7, i * rectHeight + rectHeight);
                } else {
                    //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
                    //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
                    float rectHeight5 = height / 5;
                    float rectHeight6 = (height / 5) * 4 / 5;
                    rectF.set(j * width / 7, i * rectHeight6 + (rectHeight5 - rectHeight6) / 2, j * width / 7 + width / 7, i * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2);
                }

                //
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

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < mRectFList.size(); i++) {
                RectF rectF = mRectFList.get(i);
                if (rectF.contains((int) e.getX(), (int) e.getY())) {
                    LocalDate clickDate = mDateList.get(i);
                    mCalendarHelper.dealClickDate(clickDate);
                    break;
                }
            }
            return true;
        }
    });


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
        return mGestureDetector.onTouchEvent(event);
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

        Log.e("updateSlideDistance", "updateSlideDistance::33333:;");
        invalidate();
    }

    @Override
    public List<LocalDate> getCurrentDateList() {
        return mCalendarHelper.getCurrentDateList();
    }

    @Override
    public void notifyCalendarView() {
        invalidate();

        Log.e("invalidate", "invalidate:::;");
    }

    //周或者月的第一天
    @Override
    public LocalDate getFirstDate() {
        return mCalendarHelper.getFirstDate();
    }

}
