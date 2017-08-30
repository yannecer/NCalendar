package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NWeekView extends NCalendarView{

    private List<DateTime> weekDateTimeList;
    private OnClickWeekViewListener mOnClickWeekViewListener;

    public NWeekView(Context context) {
        this(context,null);
    }

    public NWeekView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NWeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NWeekView(Context context, DateTime dateTime, OnClickWeekViewListener onClickWeekViewListener) {
        this(context);



        this.mInitialDateTime = dateTime;
        Utils.NCalendar weekCalendar2 = Utils.getWeekCalendar2(dateTime, 0);

        weekDateTimeList = weekCalendar2.dateTimeList;
        mOnClickWeekViewListener = onClickWeekViewListener;
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

            //canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);


            if (mSelectDateTime != null && dateTime.toLocalDate().toString().equals(mSelectDateTime.toLocalDate().toString())) {
                int radius = Math.min(Math.min(rect.width() / 2, rect.height() / 2), 30);
                canvas.drawCircle(rect.centerX(), rect.centerY(), 30, mSorlarPaint);
                canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
            } else {
                canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
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
                    mOnClickWeekViewListener.onClickCurrentWeek(selectDateTime);
                    break;
                }
            }
            return true;
        }
    });
}
