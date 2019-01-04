package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.necer.calendar.BaseCalendar;
import com.necer.entity.NDate;
import com.necer.painter.Painter;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class BaseCalendarView extends View {

    private int mLineNum;//行数
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected List<Rect> mRectList;//点击用的矩形集合
    protected List<NDate> mDateList;//页面的数据集合
    private LocalDate mSelectDate;//点击选中的日期
    private boolean isDraw;//是否绘制这个选中的日期

    public BaseCalendarView(Context context, LocalDate localDate, int weekFirstDayType) {
        super(context);
        this.mInitialDate = this.mSelectDate = localDate;
        mDateList = getNCalendar(localDate, weekFirstDayType);
        mRectList = new ArrayList<>();
        mLineNum = mDateList.size() / 7;//天数/7
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制时获取区间开始结束日期和绘制类Painter
        BaseCalendar calendar = (BaseCalendar) getParent();
        LocalDate startDate = calendar.getStartDate();
        LocalDate endDate = calendar.getEndDate();
        Painter painter = calendar.getPainter();

        mRectList.clear();

        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = getRect(i, j);
                mRectList.add(rect);
                NDate nDate = mDateList.get(i * 7 + j);
                LocalDate date = nDate.localDate;

                //在可用区间内的正常绘制，
                if (!(date.isBefore(startDate) || date.isAfter(endDate))) {
                    if (isEqualsMonthOrWeek(date, mInitialDate)) {  //当月和上下月的颜色不同
                        if (Util.isToday(date) && date.equals(mSelectDate)) {  //当天且选中的当天
                            painter.onDrawToday(canvas, rect, nDate, isDraw);
                        } else if (Util.isToday(date) && !date.equals(mSelectDate)) { //当天但选中的不是今天
                            painter.onDrawToday(canvas, rect, nDate, false);
                        } else if (isDraw && date.equals(mSelectDate)) { //如果默认选择，就绘制，如果默认不选择且不是点击，就不绘制
                            painter.onDrawCurrentMonthOrWeek(canvas, rect, nDate, true);
                        } else { //当月其他的日历绘制
                            painter.onDrawCurrentMonthOrWeek(canvas, rect, nDate, false);
                        }
                    } else {  //不是当月的日历
                        painter.onDrawNotCurrentMonth(canvas, rect, nDate);
                    }
                } else { //日期区间之外的日期
                    painter.onDrawDisableDate(canvas, rect, nDate);
                }
            }
        }
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
     * 得到当前页面的数据，周和月
     *
     * @param initialDate 初始化当前页面数据的date
     * @param type        一周开始是周日还是周一
     * @return
     */
    protected abstract List<NDate> getNCalendar(LocalDate initialDate, int type);


    /**
     * 点击事件
     *
     * @param clickNData  点击的date
     * @param initialDate 初始化当前页面的date
     */
    protected abstract void onClick(NDate clickNData, LocalDate initialDate);


    //初始化的日期和绘制的日期是否是同月，周都相同
    public abstract boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate);


    //获取当前页面的初始日期
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
                    NDate clickDate = mDateList.get(i);
                    onClick(clickDate, mInitialDate);
                    break;
                }
            }
            return true;
        }
    });


    //设置选中的日期 并绘制
    public void setSelectDate(LocalDate localDate, boolean isDraw) {
        //默认选中和isDraw满足其一就绘制
        this.isDraw = isDraw;
        this.mSelectDate = localDate;
        invalidate();
    }

    //选中的日期到顶部的距离
    public int getMonthCalendarOffset() {
        int monthCalendarOffset;
        //选中的是第几行
        int selectIndex = mDateList.indexOf(new NDate(mSelectDate)) / 7;
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
