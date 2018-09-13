package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.necer.MyLog;
import com.necer.entity.NCalendar;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class BaseCalendarView extends View {


    private Attrs mAttrs;
    protected int mWidth;
    protected int mHeight;
    private int mLineNum;//行数
    private List<String> mLunarList;
    protected List<LocalDate> mLocalDateList;
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected Paint mSorlarPaint;
    protected List<Rect> mRectList;//点击用的矩形集合

    private List<LocalDate> mPointList;

    private LocalDate mSelectDate;//点击选中的日期

    public BaseCalendarView(Context context, Attrs attrs, LocalDate localDate) {
        super(context);

        this.mAttrs = attrs;
        this.mInitialDate = localDate;

        NCalendar nCalendar = getNCalendar(localDate,attrs.firstDayOfWeek);
        mLunarList = nCalendar.lunarList;
        mLocalDateList = nCalendar.dateList;

        mRectList = new ArrayList<>();
        mLineNum = mLocalDateList.size() / 7;//天数/7

        mSorlarPaint = getPaint(attrs.solarTextColor, attrs.solarTextSize);

    }


    @Override
    protected void onDraw(Canvas canvas) {
       // MyLog.d("当前绘制canvas::::::" + this);

        mWidth = getWidth();
        //绘制高度
        mHeight = getHeight();
        mRectList.clear();
        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = new Rect(j * mWidth / 7, i * mHeight / mLineNum, j * mWidth / 7 + mWidth / 7, i * mHeight / mLineNum + mHeight / mLineNum);
                mRectList.add(rect);
                LocalDate date = mLocalDateList.get(i * 7 + j);
                Paint.FontMetricsInt fontMetrics = mSorlarPaint.getFontMetricsInt();

                int baseline;//让6行的第一行和5行的第一行在同一直线上，处理选中第一行的滑动

                if (mLineNum == 6) {//月的情况，跨6周
                    baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2 + (mHeight / 5 - mHeight / 6) / 2;
                }else{//月5行和周1行同样处理
                    baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                }

                //当月和上下月的颜色不同
                if (isEqualsMonthOrWeek(date,mInitialDate)) {
                    //当天和选中的日期不绘制农历
                    if (Util.isToday(date)) {
                        mSorlarPaint.setColor(mAttrs.selectCircleColor);
                        int centerY = mLineNum == 6 ? (rect.centerY() + (mHeight / 5 - mHeight / 6) / 2) : rect.centerY();
                        canvas.drawCircle(rect.centerX(), centerY, mAttrs.selectCircleRadius, mSorlarPaint);
                        mSorlarPaint.setColor(Color.WHITE);
                        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                    } else if (mAttrs.isDefaultSelect && mSelectDate != null && date.equals(mSelectDate)) {
                        mSorlarPaint.setColor(mAttrs.selectCircleColor);
                        int centerY = mLineNum == 6 ? (rect.centerY() + (mHeight / 5 - mHeight / 6) / 2) : rect.centerY();
                        canvas.drawCircle(rect.centerX(), centerY, mAttrs.selectCircleRadius, mSorlarPaint);
                        mSorlarPaint.setColor(mAttrs.hollowCircleColor);
                        canvas.drawCircle(rect.centerX(), centerY, mAttrs.selectCircleRadius - mAttrs.hollowCircleStroke, mSorlarPaint);
                        mSorlarPaint.setColor(mAttrs.solarTextColor);
                        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                    } else {
                        mSorlarPaint.setColor(mAttrs.solarTextColor);
                        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                        //  drawLunar(canvas, rect, baseline, mLunarTextColor, i, j);
                        //绘制节假日
                        //   drawHolidays(canvas, rect, date, baseline);
                        //绘制圆点
                        drawPoint(canvas, rect, date, baseline);
                    }

                } else {
                    mSorlarPaint.setColor(mAttrs.hintColor);
                    canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                   // drawLunar(canvas, rect, baseline, mHintColor, i, j);
                    //绘制节假日
                  //  drawHolidays(canvas, rect, date, baseline);
                    //绘制圆点
                  //  drawPoint(canvas, rect, date, baseline);
                }
            }
        }

    }


    //绘制圆点
    public void drawPoint(Canvas canvas, Rect rect, LocalDate date, int baseline) {
        if (mPointList != null && mPointList.contains(date)) {
            mSorlarPaint.setColor(mAttrs.pointColor);
            canvas.drawCircle(rect.centerX(), baseline - getMeasuredHeight() / 15, mAttrs.pointSize, mSorlarPaint);
        }
    }


    private Paint getPaint(int paintColor, float paintSize) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setTextSize(paintSize);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    /**
     * 得到当前页面的数据，周和月
     * @param initialDate 初始化当前页面数据的date
     * @param type 一周开始是周日还是周一
     * @return
     */
    protected abstract NCalendar getNCalendar(LocalDate initialDate,int type);


    /**
     * 点击事件
     * @param clickData 点击的date
     * @param initialDate 初始化当前页面的date
     */
    protected abstract void onClick(LocalDate clickData,LocalDate initialDate);


    /**
     * 初始化的日期和绘制的日期是否是同月，周都相同
     * @param date
     * @param initialDate
     * @return
     */
    protected abstract boolean isEqualsMonthOrWeek(LocalDate date,LocalDate initialDate);


    /**
     * 当前页面的初始值
     * @return
     */
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
                    LocalDate clickDate = mLocalDateList.get(i);
                    onClick(clickDate,mInitialDate);
                    break;
                }
            }
            return true;
        }
    });


    public void setSelectDate(LocalDate localDate,List<LocalDate> pointList) {
        this.mSelectDate = localDate;
        this.mPointList = pointList;
        invalidate();
    }

    /*public void clear() {
        this.mSelectDate = null;
        invalidate();
    }*/
}
