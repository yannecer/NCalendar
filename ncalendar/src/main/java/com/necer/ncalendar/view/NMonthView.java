package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.utils.Utils;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NMonthView extends NCalendarView {


    private int mWidth;
    private int mHeight;

    private List<DateTime> dateTimes;
    private List<Rect> mRectList;
    private int mRowNum;
    private int mSelectRowIndex;

    private OnClickMonthViewListener mOnClickMonthViewListener;

    public NMonthView(Context context) {
        super(context, null);
    }

    public NMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NMonthView(Context context, DateTime dateTime, OnClickMonthViewListener onClickMonthViewListener) {
        this(context);
        this.mInitialDateTime = dateTime;

        mRectList = new ArrayList<>();
        dateTimes = Utils.getMonthCalendar2(dateTime, 0);

        mRowNum = dateTimes.size() / 7;

        mOnClickMonthViewListener = onClickMonthViewListener;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = getHeight();

        for (int i = 0; i < mRowNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = new Rect(j * mWidth / 7, i * mHeight / mRowNum, j * mWidth / 7 + mWidth / 7, i * mHeight / mRowNum + mHeight / mRowNum);
                mRectList.add(rect);
                DateTime dateTime = dateTimes.get(i * 7 + j);
                Paint.FontMetricsInt fontMetrics = mSorlarPaint.getFontMetricsInt();

                int baseline;//让6行的第一行和5行的第一行在同一直线上，处理选中第一行的滑动
                if (mRowNum == 5) {
                    baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                } else {
                    baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2 + (mHeight / 10 - mHeight / 12);
                }
                if (mSelectDateTime != null && dateTime.toLocalDate().toString().equals(mSelectDateTime.toLocalDate().toString())) {
                    mSelectRowIndex = i;//选中的行
                    int radius = Math.min(Math.min(rect.width() / 2, rect.height() / 2), 30);
                    canvas.drawCircle(rect.centerX(), rect.centerY(), 30, mSorlarPaint);
                    canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
                } else {
                    canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSorlarPaint);
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
            for (int i = 0; i < mRectList.size(); i++) {
                Rect rect = mRectList.get(i);
                if (rect.contains((int) e.getX(), (int) e.getY())) {
                    DateTime selectDateTime = dateTimes.get(i);
                    if (Utils.isLastMonth(selectDateTime, mInitialDateTime)) {
                        mOnClickMonthViewListener.onClickLastMonth(selectDateTime);
                    } else if (Utils.isNextMonth(selectDateTime, mInitialDateTime)) {
                        mOnClickMonthViewListener.onClickNextMonth(selectDateTime);
                    } else {
                        mOnClickMonthViewListener.onClickCurrentMonth(selectDateTime);
                    }
                    break;
                }
            }
            return true;
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public int getRowNum() {
        return mRowNum;
    }
    public int getSelectRowIndex() {
        return mSelectRowIndex;
    }

}
