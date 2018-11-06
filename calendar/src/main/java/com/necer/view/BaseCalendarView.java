package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

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
    protected Paint mTextPaint;
    protected Paint mCirclePaint;
    protected List<Rect> mRectList;//点击用的矩形集合

    private List<LocalDate> mPointList;

    private LocalDate mSelectDate;//点击选中的日期

    public BaseCalendarView(Context context, Attrs attrs, LocalDate localDate) {
        super(context);

        this.mAttrs = attrs;
        this.mInitialDate = localDate;

        NCalendar nCalendar = getNCalendar(localDate, attrs.firstDayOfWeek);
        mLunarList = nCalendar.lunarList;
        mLocalDateList = nCalendar.dateList;

        mRectList = new ArrayList<>();
        mLineNum = mLocalDateList.size() / 7;//天数/7

        mTextPaint = getPaint();
        mCirclePaint = getPaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        mWidth = getWidth();
        //绘制高度
        mHeight = getHeight();
        mRectList.clear();
        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = new Rect(j * mWidth / 7, i * mHeight / mLineNum, j * mWidth / 7 + mWidth / 7, i * mHeight / mLineNum + mHeight / mLineNum);
                mRectList.add(rect);
                LocalDate date = mLocalDateList.get(i * 7 + j);

                //公历text的baseline,5、6行的处理是为了让第一行的文字对齐
                int baseline = mLineNum == 6 ? (rect.centerY() + (mHeight / 5 - mHeight / 6) / 2) : rect.centerY();
                //每个日期水平方向的中点 centerX
                int centerX = rect.centerX();

                //当月和上下月的颜色不同
                if (isEqualsMonthOrWeek(date, mInitialDate)) {

                    //当天且选中的当天
                    if (Util.isToday(date) && date.equals(mSelectDate)) {

                        drawSolidCircle(canvas, rect.centerX(), baseline);
                        drawSolar(canvas, centerX, baseline, Color.WHITE, date.getDayOfMonth() + "");
                        drawLunar(canvas, centerX, baseline, Color.WHITE, mLunarList.get(i * 7 + j));
                        drawPoint(canvas, centerX, baseline,Color.WHITE, date);

                    } else if (Util.isToday(date) && !date.equals(mSelectDate)) {
                        //当天但选中的不是今天

                        drawSolar(canvas, centerX, baseline, Color.RED, date.getDayOfMonth() + "");

                        drawLunar(canvas, centerX, baseline, mAttrs.lunarTextColor, mLunarList.get(i * 7 + j));

                        drawPoint(canvas, centerX, baseline, mAttrs.pointColor,date);

                    } else if (mAttrs.isDefaultSelect && date.equals(mSelectDate)) {


                        drawHollowCircle(canvas, centerX, baseline);

                        drawSolar(canvas, centerX, baseline, mAttrs.solarTextColor, date.getDayOfMonth() + "");

                        drawLunar(canvas, centerX, baseline, mAttrs.lunarTextColor, mLunarList.get(i * 7 + j));

                        drawPoint(canvas, centerX, baseline,mAttrs.pointColor, date);
                    } else {

                        drawSolar(canvas, centerX, baseline, mAttrs.solarTextColor, date.getDayOfMonth() + "");

                        //农历
                        drawLunar(canvas, centerX, baseline, mAttrs.lunarTextColor, mLunarList.get(i * 7 + j));

                        //绘制节假日
                        //drawHolidays(canvas, rect, date, baseline);
                        //绘制圆点
                        drawPoint(canvas, centerX, baseline,mAttrs.pointColor, date);
                    }

                } else {

                    drawSolar(canvas, centerX, baseline, mAttrs.hintColor, date.getDayOfMonth() + "");
                    //农历
                    drawLunar(canvas, centerX, baseline, mAttrs.lunarTextColor, mLunarList.get(i * 7 + j));
                    //绘制节假日
                    //  drawHolidays(canvas, rect, date, baseline);
                    //绘制圆点
                    drawPoint(canvas, centerX, baseline,mAttrs.pointColor, date);
                }
            }
        }

    }


    //空心圆
    private void drawHollowCircle(Canvas canvas, int centerX, int baseline) {
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        mCirclePaint.setColor(mAttrs.hollowCircleColor);
        canvas.drawCircle(centerX, mAttrs.isShowLunar ? baseline : getSolarTexyCenterY(baseline), mAttrs.selectCircleRadius, mCirclePaint);
    }

    //实心圆
    private void drawSolidCircle(Canvas canvas, int centerX, int baseline) {
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        mCirclePaint.setColor(mAttrs.selectCircleColor);
        canvas.drawCircle(centerX, mAttrs.isShowLunar ? baseline : getSolarTexyCenterY(baseline), mAttrs.selectCircleRadius, mCirclePaint);

    }

    //绘制公历
    private void drawSolar(Canvas canvas, int centerX, int baseline, int color, String solar) {
        mTextPaint.setColor(color);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(solar, centerX, baseline, mTextPaint);
    }

    //绘制圆点
    private void drawPoint(Canvas canvas, int centerX, int baseline,int color, LocalDate date) {
        if (mPointList != null && mPointList.contains(date)) {
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(color);
            int solarTexyCenterY = getSolarTexyCenterY(baseline);
            canvas.drawCircle(centerX, mAttrs.pointLocation == 0 ? (solarTexyCenterY - getMeasuredHeight() / 24) : (solarTexyCenterY + getMeasuredHeight() / 24), mAttrs.pointSize, mCirclePaint);
        }
    }

    //绘制农历
    private void drawLunar(Canvas canvas, int centerX, int baseline, int color, String lunar) {
        if (mAttrs.isShowLunar) {
            mTextPaint.setColor(color);
            mTextPaint.setTextSize(mAttrs.lunarTextSize);
            canvas.drawText(lunar, centerX, baseline + getMeasuredHeight() / 20, mTextPaint);
        }
    }


    //公历文字的竖直中心y
    private int getSolarTexyCenterY(int baseline) {
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int ascent = fontMetricsInt.ascent;
        int descent = fontMetricsInt.descent;
        int textCenterY = descent / 2 + baseline + ascent / 2;//文字的中心y
        return textCenterY;
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    /**
     * 得到当前页面的数据，周和月
     *
     * @param initialDate 初始化当前页面数据的date
     * @param type        一周开始是周日还是周一
     * @return
     */
    protected abstract NCalendar getNCalendar(LocalDate initialDate, int type);


    /**
     * 点击事件
     *
     * @param clickData   点击的date
     * @param initialDate 初始化当前页面的date
     */
    protected abstract void onClick(LocalDate clickData, LocalDate initialDate);


    /**
     * 初始化的日期和绘制的日期是否是同月，周都相同
     *
     * @param date
     * @param initialDate
     * @return
     */
    protected abstract boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate);


    /**
     * 当前页面的初始值
     *
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
                    onClick(clickDate, mInitialDate);
                    break;
                }
            }
            return true;
        }
    });


    public void setSelectDate(LocalDate localDate, List<LocalDate> pointList) {
        this.mSelectDate = localDate;
        this.mPointList = pointList;
        invalidate();
    }
}
