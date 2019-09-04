package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.necer.calendar.BaseCalendar;
import com.necer.painter.CalendarPainter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class CalendarView extends View {


    private int mLineNum;//行数
    protected LocalDate mInitialDate;//当前页面的初始化日期
    protected List<RectF> mRectFList;//点击用的矩形集合
    protected RectF mBgRectF;//日历背景集合
    protected List<LocalDate> mDateList;//页面的数据集合
    private List<LocalDate> mAllSelectListDate;//当前页面选中的日期
    protected BaseCalendar mCalendar;
    protected LocalDate mStartDate;
    protected LocalDate mEndDate;

    private int mCurrentDistance;//折叠日历滑动当前的距离

    public CalendarView(Context context, ViewGroup container, LocalDate initialDate, List<LocalDate> dateList) {
        super(context);
        this.mInitialDate = initialDate;
        this.mDateList = dateList;
        mRectFList = new ArrayList<>();
        mLineNum = mDateList.size() / 7;//天数/7

        mCalendar = (BaseCalendar) container;
        mAllSelectListDate = mCalendar.getAllSelectDateList();
        mStartDate = mCalendar.getStartDate();
        mEndDate = mCalendar.getEndDate();

        //为每一个日期分配一个矩形，便于计算位置
        for (int i = 0; i < mDateList.size(); i++) {
            mRectFList.add(new RectF());
        }
        mBgRectF = new RectF();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        CalendarPainter calendarPainter = mCalendar.getCalendarPainter();
        //绘制背景
        drawBg(canvas, calendarPainter);
        //绘制日期
        drawDate(canvas, calendarPainter);
    }

    //绘制背景
    private void drawBg(Canvas canvas, CalendarPainter calendarPainter) {
        mBgRectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        calendarPainter.onDrawCalendarBackground(this, canvas, mBgRectF, getMiddleLocalDate(), getMeasuredHeight(), mCurrentDistance);
    }


    //绘制日期
    private void drawDate(Canvas canvas, CalendarPainter calendarPainter) {

        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                RectF rectF = mRectFList.get(i * 7 + j);
                //矩形确定位置
                float width = getMeasuredWidth();
                float height = getMeasuredHeight();
                //为每个矩形确定位置
                if (mLineNum == 5 || mLineNum == 1) {
                    //5行的月份，5行矩形平分view的高度  mLineNum==1是周的情况
                    float rectHeight = height / mLineNum;
                    rectF.set(j * width / 7, i * rectHeight, j * width / 7 + width / 7, i * rectHeight + rectHeight);
                } else {
                    //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
                    //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
                    float rectHeight5 = height / 5;
                    float rectHeight6 = (height / 5) * 4 / 5;
                    rectF.set(j * width / 7, i * rectHeight6 + (rectHeight5 - rectHeight6) / 2, j * width / 7 + width / 7, i * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2);
                }

                //开始绘制
                LocalDate localDate = mDateList.get(i * 7 + j);
                //在可用区间内的正常绘制，
                if (!(localDate.isBefore(mStartDate) || localDate.isAfter(mEndDate))) {
                    if (isEqualsMonthOrWeek(localDate, mInitialDate)) {  //当月和上下月的颜色不同
                        if (CalendarUtil.isToday(localDate) && mAllSelectListDate.contains(localDate)) {  //当天且选中的当天
                            calendarPainter.onDrawToday(canvas, rectF, localDate, mAllSelectListDate);
                        } else if (CalendarUtil.isToday(localDate) && !mAllSelectListDate.contains(localDate)) { //当天但选中的不是今天
                            calendarPainter.onDrawToday(canvas, rectF, localDate, mAllSelectListDate);
                        } else if (mAllSelectListDate.contains(localDate)) { //如果默认选择，就绘制，如果默认不选择且不是点击，就不绘制
                            calendarPainter.onDrawCurrentMonthOrWeek(canvas, rectF, localDate, mAllSelectListDate);
                        } else { //当月其他的日历绘制
                            calendarPainter.onDrawCurrentMonthOrWeek(canvas, rectF, localDate, mAllSelectListDate);
                        }
                    } else {  //不是当月的日历
                        if (mAllSelectListDate.contains(localDate)) {
                            calendarPainter.onDrawLastOrNextMonth(canvas, rectF, localDate, mAllSelectListDate);
                        } else {
                            calendarPainter.onDrawLastOrNextMonth(canvas, rectF, localDate, mAllSelectListDate);
                        }
                    }
                } else { //日期区间之外的日期
                    calendarPainter.onDrawDisableDate(canvas, rectF, localDate);
                }
            }
        }
    }


    //获取当前页面的初始化日期
    public LocalDate getInitialDate() {
        return mInitialDate;
    }


    //获取中间的日期，周日历以中间的日期判断当前页面的年和月
    public LocalDate getMiddleLocalDate() {
        return mDateList.get((mDateList.size() / 2) + 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
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
                    dealClickDate(clickDate);
                    break;
                }
            }
            return true;
        }
    });


    //选中的日期到顶部的距离
    public int getDistanceFromTop(LocalDate localDate) {
        int monthCalendarOffset;
        //选中的是第几行   对于没有选中的默认折叠中心是第一行，有选中的默认折叠中心是选中的第一个日期
        int selectIndex = mDateList.indexOf(localDate) / 7;
        if (mLineNum == 5) {
            //5行的月份
            monthCalendarOffset = getMeasuredHeight() / 5 * selectIndex;
        } else {
            int rectHeight6 = (getMeasuredHeight() / 5) * 4 / 5;
            monthCalendarOffset = rectHeight6 * selectIndex;
        }
        return monthCalendarOffset;
    }


    //获取折叠的中心点 如果有当前页面有选中 返回选中的日期，如果没有选中看是否包含今天，如果没有就返回当前页面第一个日期
    public LocalDate getPivotDate() {
        LocalDate today = new LocalDate();
        if (getCurrentSelectDateList().size() != 0) {
            return getCurrentSelectDateList().get(0);
        } else if (mDateList.contains(today)) {
            return today;
        } else {
            return mDateList.get(0);
        }
    }

    //获取中心点到顶部的距离
    public int getPivotDistanceFromTop() {
        return getDistanceFromTop(getPivotDate());
    }

    //获取当前页面选中的日期
    public List<LocalDate> getCurrentSelectDateList() {
        List<LocalDate> currentSelectDateList = new ArrayList<>();
        for (int i = 0; i < mDateList.size(); i++) {
            LocalDate localDate = mDateList.get(i);
            if (mAllSelectListDate != null && mAllSelectListDate.contains(localDate)) {
                currentSelectDateList.add(localDate);
            }
        }
        return currentSelectDateList;
    }

    public void updateSlideDistance(int currentDistance) {
        this.mCurrentDistance = currentDistance;
        invalidate();
    }

    //获取当前页面的日期
    public List<LocalDate> getCurrentDateList() {
        return mDateList;
    }

    //处理当前页面的点击事件
    protected abstract void dealClickDate(LocalDate clickDate);

    //初始化的日期和绘制的日期是否是同月，周都相同
    protected abstract boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate);

    //周或者月的第一天
    public abstract LocalDate getFirstDate();

}
