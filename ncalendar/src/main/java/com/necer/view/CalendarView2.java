package com.necer.view;

import android.content.Context;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.necer.calendar.BaseCalendar;
import com.necer.painter.CalendarAdapter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器构造的日历页面
 */
public abstract class CalendarView2 extends ViewGroup implements ICalendarView {


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

    private CalendarAdapter calendarAdapter;

    public CalendarView2(Context context, ViewGroup container, LocalDate initialDate, List<LocalDate> dateList) {
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

        mBgRectF = new RectF();

        calendarAdapter = mCalendar.getCalendarAdapter();

        for (int i = 0; i < mDateList.size(); i++) {
            mRectFList.add(new RectF());
            addView(calendarAdapter.getCalendarView(context));
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                int index = i * 7 + j;
                RectF rectF = mRectFList.get(index);

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


                View view = getChildAt(index);
                if (view != null) {
                    view.layout((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                    calendarAdapter.onBindCurrentMonthOrWeekView(view,mDateList.get(index),false);
                }



                //

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        setMeasuredDimension(specWidth, 1200);

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
                LocalDate clickDate = mDateList.get(i);
                if (rectF.contains((int) e.getX(), (int) e.getY())) {

                    //  dealCliFckDate(clickDate);

                    calendarAdapter.onBindCurrentMonthOrWeekView(getChildAt(i), clickDate, true);

                    Toast.makeText(getContext(), ":::" + mDateList.get(i), Toast.LENGTH_SHORT).show();

                    // break;
                } else {
                    calendarAdapter.onBindCurrentMonthOrWeekView(getChildAt(i),  clickDate,false);
                }
            }
            return true;
        }
    });


    @Override
    public LocalDate getInitialDate() {
        return mInitialDate;
    }


    @Override
    public LocalDate getMiddleLocalDate() {
        return mDateList.get((mDateList.size() / 2) + 1);
    }


    @Override
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


    @Override
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

    @Override
    public int getPivotDistanceFromTop() {
        return getDistanceFromTop(getPivotDate());
    }

    @Override
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

    @Override
    public void updateSlideDistance(int currentDistance) {
        this.mCurrentDistance = currentDistance;
        invalidate();
    }

    @Override
    public List<LocalDate> getCurrentDateList() {
        return mDateList;
    }


    @Override
    public void notifyCalendarView() {
        invalidate();
    }

    //处理当前页面的点击事件
    protected abstract void dealClickDate(LocalDate clickDate);

    //初始化的日期和绘制的日期是否是同月，周都相同
    protected abstract boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate);

    //周或者月的第一天
    public abstract LocalDate getFirstDate();
}
