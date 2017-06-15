package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by necer on 2017/6/13.
 */

public class WeekView extends CalendarView {

    private List<DateTime> weekDateTimeList;
    private List<String> lunarList;

    private OnClickWeekViewListener onClickWeekViewListener;

    public WeekView(Context context, DateTime dateTime, OnClickWeekViewListener onClickWeekViewListener) {
        super(context);
        this.onClickWeekViewListener = onClickWeekViewListener;
        this.mInitialDateTime = dateTime;
        Utils.NCalendar monthCalendar = Utils.getWeekCalendar(dateTime);
        lunarList = monthCalendar.lunarList;
        weekDateTimeList = monthCalendar.dateTimeList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        mRectList.clear();
        for (int i = 0; i < 7; i++) {
            Rect rect = new Rect(i * mWidth / 7, 0, i * mWidth / 7 + mWidth / 7, mHeight);
            mRectList.add(rect);
            DateTime dateTime = weekDateTimeList.get(i);
            Paint.FontMetricsInt fontMetrics = mSorlarPaint.getFontMetricsInt();
            int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;

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
                if (isShowLunar) {
                    String lunar = lunarList.get(i);
                    canvas.drawText(lunar, rect.centerX(), rect.bottom - Utils.dp2px(getContext(), 5), mLunarPaint);
                }
            }
            if (mPointList.contains(dateTime.toLocalDate().toString())) {
                mSorlarPaint.setColor(mPointColor);
                canvas.drawCircle(rect.centerX(), rect.bottom-mPointSize, mPointSize, mSorlarPaint);
            }
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
                    DateTime selectDateTime = weekDateTimeList.get(i);
                    onClickWeekViewListener.onClickCurrentWeek(selectDateTime);
                    break;
                }
            }
            return true;
        }
    });
}
