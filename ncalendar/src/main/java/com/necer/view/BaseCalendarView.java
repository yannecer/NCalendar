package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.entity.NDate;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class BaseCalendarView extends View {


    /** 新需求
     * 1、支持多选，选中的第一个为中心切换
     * 2、支持不选中月周切换/
     * 3、支持viewpager滑动
     * 4、自定义简单化 比如绘制单个的日期，可用，不可用，选中不选中，逻辑部分不对外
     * 5、支持滑动默认选中第一天
     * 6、不选中支持月周切换
     */

    /***
     * 思路  BaseCalendarView中没有选中日期，全部的日期都在BaseCalendar中统一管理，再根据条件传过来，可实现默认选中第一个 和跳转的管理
     *
     * 选中日期在 BaseCalendar 中用list统一管理
     *
     * 从父类中获取属性，不再传递
     *
     */



    private int mLineNum;//行数
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected List<Rect> mRectList;//点击用的矩形集合
    protected List<LocalDate> mDateList;//页面的数据集合

    private List<LocalDate> mAllSelectListDate;//当前页面选中的日期
    protected BaseCalendar mCalendar;

    private List<LocalDate> mCurrentSelectDateList;//当前月份选中的日期

    public BaseCalendarView(Context context, LocalDate initialDate, List<LocalDate> dateList) {
        super(context);
        this.mInitialDate = initialDate;
        this.mDateList = dateList;
      //  mSelectListDate = new ArrayList<>();
        mRectList = new ArrayList<>();
        mLineNum = mDateList.size() / 7;//天数/7

        mCurrentSelectDateList = new ArrayList<>();



    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (mCalendar == null) {
            mCalendar = (BaseCalendar) getParent();
            mAllSelectListDate = mCalendar.getAllSelectDateList();
        }

        LocalDate startDate = mCalendar.getStartDate();
        LocalDate endDate = mCalendar.getEndDate();
        CalendarPainter calendarPainter = mCalendar.getCalendarPainter();

        mRectList.clear();
        mCurrentSelectDateList.clear();

        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = getRect(i, j);
                mRectList.add(rect);
                LocalDate date = mDateList.get(i * 7 + j);

                //在可用区间内的正常绘制，
                if (!(date.isBefore(startDate) || date.isAfter(endDate))) {
                    if (isEqualsMonthOrWeek(date, mInitialDate)) {  //当月和上下月的颜色不同
                        if (Util.isToday(date) && mAllSelectListDate.contains(date)) {  //当天且选中的当天
                            mCurrentSelectDateList.add(date);
                            calendarPainter.onDrawToday(canvas, rect, Util.getNDate(date),true);
                        } else if (Util.isToday(date) && !mAllSelectListDate.contains(date)) { //当天但选中的不是今天
                            calendarPainter.onDrawToday(canvas, rect, Util.getNDate(date),false);
                        } else if (mAllSelectListDate.contains(date)) { //如果默认选择，就绘制，如果默认不选择且不是点击，就不绘制
                            mCurrentSelectDateList.add(date);
                            calendarPainter.onDrawCurrentMonthOrWeek(canvas, rect, Util.getNDate(date), true);
                        } else { //当月其他的日历绘制
                            calendarPainter.onDrawCurrentMonthOrWeek(canvas, rect, Util.getNDate(date), false);
                        }
                    } else {  //不是当月的日历
                        if (mAllSelectListDate.contains(date)) {
                            calendarPainter.onDrawNotCurrentMonth(canvas, rect, Util.getNDate(date),true);
                        } else {
                            calendarPainter.onDrawNotCurrentMonth(canvas, rect, Util.getNDate(date),false);
                        }

                    }
                } else { //日期区间之外的日期
                    calendarPainter.onDrawDisableDate(canvas, rect, Util.getNDate(date));
                }
            }
        }


        MyLog.d("mCurrentSelectDateList::" + mCurrentSelectDateList);
    }

    //获取每个元素矩形
    private Rect getRect(int i, int j) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Rect rect;
        //5行的月份，5行矩形平分view的高度  mLineNum==1是周的情况
        if (mLineNum == 5 || mLineNum == 1) {
            int rectHeight = height / mLineNum;
            rect = new Rect(j * width / 7, i * rectHeight, j * width / 7 + width / 7, i * rectHeight + rectHeight);
        } else {
            //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
            //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
            int rectHeight5 = height / 5;
            int rectHeight6 = (height / 5) * 4 / 5;
            rect = new Rect(j * width / 7, i * rectHeight6 + (rectHeight5 - rectHeight6) / 2, j * width / 7 + width / 7, i * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2);
        }
        return rect;
    }


    /**
     * 点击事件
     *
     * @param clickNData  点击的date
     * @param initialDate 初始化当前页面的date
     */
    protected abstract void onClickDate(LocalDate clickNData, LocalDate initialDate);


    //初始化的日期和绘制的日期是否是同月，周都相同
    public abstract boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate);


    //获取当前页面的初始化日期
    public LocalDate getInitialDate() {
        return mInitialDate;
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
            for (int i = 0; i < mRectList.size(); i++) {
                Rect rect = mRectList.get(i);
                if (rect.contains((int) e.getX(), (int) e.getY())) {
                    LocalDate clickDate = mDateList.get(i);
                    onClickDate(clickDate, mInitialDate);
                    break;
                }
            }
            return true;
        }
    });


    //选中的日期到顶部的距离
    public int getMonthCalendarOffset(LocalDate localDate) {


        int monthCalendarOffset;
        //选中的是第几行   对于没有选中的默认折叠中心是第一行，有选中的默认折叠中心是选中的第一个日期
     //   int selectIndex = mCurrentSelectDateList.size() == 0 ? 0 : mDateList.indexOf(mCurrentSelectDateList.get(0)) / 7;
        int selectIndex = mDateList.indexOf(localDate)/7;
        if (mLineNum == 5) {
            //5行的月份
            monthCalendarOffset = getMeasuredHeight() / 5 * selectIndex;
        } else {
            // int rectHeight5 = getMeasuredHeight() / 5;
            int rectHeight6 = (getMeasuredHeight() / 5) * 4 / 5;
            monthCalendarOffset = rectHeight6 * selectIndex;
        }
        return monthCalendarOffset;
    }


    public LocalDate getFirstDate() {
        return mDateList.get(0);
    }

    public LocalDate getPivot() {

        MyLog.d("mCurrentSelectDateListmCurrentSelectDateListmCurrentSelectDateList::" + mCurrentSelectDateList.size());

        return mCurrentSelectDateList.size() == 0 ? mDateList.get(0) : mCurrentSelectDateList.get(0);
    }

    public int getMonthCalendarOffset() {
        if (mCurrentSelectDateList.size() == 0) {
            return getMonthCalendarOffset(mDateList.get(0));
        } else {
            return getMonthCalendarOffset(mCurrentSelectDateList.get(0));
        }

    }


    public List<LocalDate> getCurrentSelectDateList() {
        return mCurrentSelectDateList;
    }



    //是否是当月日期
    public boolean contains(LocalDate localDate) {
        if (localDate == null) {
            return false;
        } else if (isEqualsMonthOrWeek(localDate, mInitialDate)) {
            return true;
        } else {
            return false;
        }
    }

}
