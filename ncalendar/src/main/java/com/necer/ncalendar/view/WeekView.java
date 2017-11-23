package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

import java.util.List;


/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public class WeekView extends CalendarView {


    private OnClickWeekViewListener mOnClickWeekViewListener;
    private List<String> lunarList;

    public WeekView(Context context, DateTime dateTime, OnClickWeekViewListener onClickWeekViewListener) {
        super(context);

        this.mInitialDateTime = dateTime;
        Utils.NCalendar weekCalendar2 = Utils.getWeekCalendar2(dateTime, Attrs.firstDayOfWeek);

        dateTimes = weekCalendar2.dateTimeList;
        lunarList = weekCalendar2.lunarList;
        mOnClickWeekViewListener = onClickWeekViewListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        //mHeight = getHeight();
        //为了与月日历保持一致，往上压缩一下,5倍的关系
        mHeight = (int) (getHeight() - Utils.dp2px(getContext(), 2));
        mRectList.clear();

        for (int i = 0; i < 7; i++) {
            Rect rect = new Rect(i * mWidth / 7, 0, i * mWidth / 7 + mWidth / 7, mHeight);
            mRectList.add(rect);
            DateTime dateTime = dateTimes.get(i);
            Paint.FontMetricsInt fontMetrics = mSorlarPaint.getFontMetricsInt();
            int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;

            if (Utils.isToday(dateTime)) {
                mSorlarPaint.setColor(mSelectCircleColor);
                canvas.drawCircle(rect.centerX(), rect.centerY(), mSelectCircleRadius, mSorlarPaint);
                mSorlarPaint.setColor(Color.WHITE);
                canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
            } else if (mSelectDateTime != null && dateTime.equals(mSelectDateTime)) {
                mSorlarPaint.setColor(mSelectCircleColor);
                canvas.drawCircle(rect.centerX(), rect.centerY(), mSelectCircleRadius, mSorlarPaint);
                mSorlarPaint.setColor(mHollowCircleColor);
                canvas.drawCircle(rect.centerX(), rect.centerY(), mSelectCircleRadius - mHollowCircleStroke, mSorlarPaint);
                mSorlarPaint.setColor(mSolarTextColor);
                canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
            } else {
                mSorlarPaint.setColor(mSolarTextColor);
                canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                //绘制农历
                drawLunar(canvas, rect, baseline,i);
                //绘制节假日
                drawHolidays(canvas, rect, dateTime, baseline);
                //绘制圆点
                drawPoint(canvas, rect, dateTime, baseline);

            }
        }
    }

    private void drawLunar(Canvas canvas, Rect rect, int baseline, int i) {
        if (isShowLunar) {
            mLunarPaint.setColor(mLunarTextColor);
            String lunar = lunarList.get(i);
            canvas.drawText(lunar, rect.centerX(), baseline + getHeight() / 4, mLunarPaint);
        }
    }


    private void drawHolidays(Canvas canvas, Rect rect, DateTime dateTime, int baseline) {
        if (isShowHoliday) {
            if (holidayList.contains(dateTime.toLocalDate().toString())) {
                mLunarPaint.setColor(mHolidayColor);
                canvas.drawText("休", rect.centerX() + rect.width() / 4, baseline - getHeight() / 4, mLunarPaint);

            } else if (workdayList.contains(dateTime.toLocalDate().toString())) {
                mLunarPaint.setColor(mWorkdayColor);
                canvas.drawText("班", rect.centerX() + rect.width() / 4, baseline - getHeight() / 4, mLunarPaint);
            }
        }
    }

    public void drawPoint(Canvas canvas, Rect rect, DateTime dateTime, int baseline) {
        if (pointList != null && pointList.contains(dateTime.toLocalDate().toString())) {
            mLunarPaint.setColor(mPointColor);
            canvas.drawCircle(rect.centerX(), baseline - getHeight() / 3, mPointSize, mLunarPaint);
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
                    DateTime selectDateTime = dateTimes.get(i);
                    mOnClickWeekViewListener.onClickCurrentWeek(selectDateTime);
                    break;
                }
            }
            return true;
        }
    });


    public boolean contains(DateTime dateTime) {
        return dateTimes.contains(dateTime);
    }
}
