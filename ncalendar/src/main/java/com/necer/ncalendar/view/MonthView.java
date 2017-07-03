package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by necer on 2017/6/9.
 */

public class MonthView extends CalendarView {

    private List<DateTime> monthDateTimeList;
    private List<String> lunarList;
    private List<String> localDateList;
    private OnClickMonthViewListener onClickMonthViewListener;



    public MonthView(Context mContext, DateTime dateTime, OnClickMonthViewListener onClickMonthViewListener) {
        super(mContext);
        this.mInitialDateTime = dateTime;
        this.onClickMonthViewListener = onClickMonthViewListener;

        Utils.NCalendar monthCalendar = Utils.getMonthCalendar(dateTime);
        lunarList = monthCalendar.lunarList;
        localDateList = monthCalendar.localDateList;
        monthDateTimeList = monthCalendar.dateTimeList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        mRectList.clear();
        //6行7列
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = new Rect(j * mWidth / 7, i * mHeight / 6, j * mWidth / 7 + mWidth / 7, i * mHeight / 6 + mHeight / 6);
                mRectList.add(rect);
                DateTime dateTime = monthDateTimeList.get(i * 7 + j);
                Paint.FontMetricsInt fontMetrics = mSorlarPaint.getFontMetricsInt();
                int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                //当月和上下月的颜色不同
                if (Utils.isEqualsMonth(dateTime, mInitialDateTime)) {
                    //当天和选中的日期不绘制农历
                    if (Utils.isToday(dateTime)) {
                        mSorlarPaint.setColor(mSelectCircleColor);
                        int radius = Math.min(Math.min(rect.width() / 2, rect.height() / 2), mSelectCircleRadius);
                        canvas.drawCircle(rect.centerX(), rect.centerY(), radius, mSorlarPaint);
                        mSorlarPaint.setColor(Color.WHITE);
                        canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                    } else if (mSelectDateTime != null && dateTime.toLocalDate().equals(mSelectDateTime.toLocalDate())) {
                        mSorlarPaint.setColor(mSelectCircleColor);
                        int radius = Math.min(Math.min(rect.width() / 2, rect.height() / 2), mSelectCircleRadius);
                        canvas.drawCircle(rect.centerX(), rect.centerY(), radius, mSorlarPaint);
                        mSorlarPaint.setColor(mHollowCircleColor);
                        canvas.drawCircle(rect.centerX(), rect.centerY(), radius - mHollowCircleStroke, mSorlarPaint);
                        mSorlarPaint.setColor(mSolarTextColor);
                        canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                    } else {
                        mSorlarPaint.setColor(mSolarTextColor);
                        canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                        drawLunar(canvas, rect, mLunarTextColor, i, j);
                    }

                } else {
                    mSorlarPaint.setColor(mHintColor);
                    canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                    drawLunar(canvas, rect, mHintColor, i, j);
                }

                if (mPointList.contains(dateTime.toLocalDate().toString())) {
                    mSorlarPaint.setColor(mPointColor);
                    canvas.drawCircle(rect.centerX(), rect.bottom-mPointSize, mPointSize, mSorlarPaint);
                }
            }
        }
    }

    /**
     * 绘制农历
     * @param canvas
     * @param rect
     * @param i
     * @param j
     */
    private void drawLunar(Canvas canvas, Rect rect, int color, int i, int j) {
        if (isShowLunar) {
            mLunarPaint.setColor(color);
            String lunar = lunarList.get(i * 7 + j);
            canvas.drawText(lunar, rect.centerX(), rect.bottom - Utils.dp2px(getContext(), 5), mLunarPaint);
        }
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
                    DateTime selectDateTime = monthDateTimeList.get(i);
                    if (Utils.isLastMonth(selectDateTime, mInitialDateTime)) {
                        onClickMonthViewListener.onClickLastMonth(selectDateTime);
                    } else if (Utils.isNextMonth(selectDateTime, mInitialDateTime)) {
                        onClickMonthViewListener.onClickNextMonth(selectDateTime);
                    } else {
                        onClickMonthViewListener.onClickCurrentMonth(selectDateTime);
                    }
                    break;
                }
            }
            return true;
        }
    });


    //选中的是一月中的第几周
    public int getWeekRow() {
        DateTime dateTime = mSelectDateTime == null ? mInitialDateTime : mSelectDateTime;
        int indexOf = localDateList.indexOf(dateTime.toLocalDate().toString());
        return indexOf / 7;
    }

}
