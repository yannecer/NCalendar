package com.necer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.necer.entity.NDate;
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
    private int mLineNum;//行数
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected Paint mTextPaint;
    protected Paint mCirclePaint;
    protected List<Rect> mRectList;//点击用的矩形集合

    protected List<NDate> mDateList;//页面的数据集合

    private List<LocalDate> mPointList;
    protected List<String> mHolidayList;
    protected List<String> mWorkdayList;

    private LocalDate mSelectDate;//点击选中的日期
    private boolean isDraw;//是否会之这个选中的日期

    public BaseCalendarView(Context context, Attrs attrs, LocalDate localDate) {
        super(context);

        this.mAttrs = attrs;
        this.mInitialDate = localDate;

        mDateList = getNCalendar(localDate, attrs.firstDayOfWeek);

        mRectList = new ArrayList<>();
        mLineNum = mDateList.size() / 7;//天数/7

        mTextPaint = getPaint();
        mCirclePaint = getPaint();

        mHolidayList = Util.getHolidayList();
        mWorkdayList = Util.getWorkdayList();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        mRectList.clear();

        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = getRect(i, j);
                mRectList.add(rect);
                NDate nDate = mDateList.get(i * 7 + j);
                LocalDate date = nDate.localDate;

                //每个矩形的竖直中心，但不是文字的绘制中心
                int centerY = rect.centerY();
                //每个日期水平方向的中点 centerX
                int centerX = rect.centerX();

                //当月和上下月的颜色不同
                if (isEqualsMonthOrWeek(date, mInitialDate)) {
                    //当天且选中的当天
                    if (Util.isToday(date) && date.equals(mSelectDate)) {
                        drawSolidCircle(canvas, rect.centerX(), centerY);
                        drawSolar(canvas, centerX, centerY, Color.WHITE, date.getDayOfMonth() + "");
                        drawLunar(canvas, centerX, centerY, true, true, nDate);
                        drawPoint(canvas, centerX, centerY, Color.WHITE, date);
                        drawHolidays(canvas, centerX, centerY, true, date);

                    } else if (Util.isToday(date) && !date.equals(mSelectDate)) {
                        //当天但选中的不是今天
                        drawSolar(canvas, centerX, centerY, mAttrs.todaySolarTextColor, date.getDayOfMonth() + "");
                        drawLunar(canvas, centerX, centerY, false, true, nDate);
                        drawPoint(canvas, centerX, centerY, mAttrs.todaySolarTextColor, date);
                        drawHolidays(canvas, centerX, centerY, false, date);

                    } else if (isDraw && date.equals(mSelectDate)) {
                        //如果默认选择，就绘制，如果默认不选择且不是点击，就不绘制
                        drawHollowCircle(canvas, centerX, centerY);
                        drawSolar(canvas, centerX, centerY, mAttrs.solarTextColor, date.getDayOfMonth() + "");
                        drawLunar(canvas, centerX, centerY, false, true, nDate);
                        drawPoint(canvas, centerX, centerY, mAttrs.pointColor, date);
                        drawHolidays(canvas, centerX, centerY, false, date);
                    } else {

                        drawSolar(canvas, centerX, centerY, mAttrs.solarTextColor, date.getDayOfMonth() + "");
                        //农历
                        drawLunar(canvas, centerX, centerY, false, true, nDate);
                        //绘制圆点
                        drawPoint(canvas, centerX, centerY, mAttrs.pointColor, date);
                        drawHolidays(canvas, centerX, centerY, false, date);
                    }

                } else {
                    //公历
                    drawSolar(canvas, centerX, centerY, mAttrs.hintColor, date.getDayOfMonth() + "");
                    //农历
                    drawLunar(canvas, centerX, centerY, false, false, nDate);
                    //绘制圆点
                    drawPoint(canvas, centerX, centerY, mAttrs.pointColor, date);
                    //绘制节假日
                    drawHolidays(canvas, centerX, centerY, false, date);
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


    //空心圆
    private void drawHollowCircle(Canvas canvas, int centerX, int centerY) {
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        mCirclePaint.setColor(mAttrs.hollowCircleColor);
        canvas.drawCircle(centerX, mAttrs.isShowLunar ? centerY : getSolarTexyCenterY(centerY), mAttrs.selectCircleRadius, mCirclePaint);
    }

    //实心圆
    private void drawSolidCircle(Canvas canvas, int centerX, int centerY) {
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        mCirclePaint.setColor(mAttrs.selectCircleColor);
        canvas.drawCircle(centerX, mAttrs.isShowLunar ? centerY : getSolarTexyCenterY(centerY), mAttrs.selectCircleRadius, mCirclePaint);

    }

    //绘制公历
    private void drawSolar(Canvas canvas, int centerX, int centerY, int color, String solar) {
        mTextPaint.setColor(color);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(solar, centerX, centerY, mTextPaint);
    }

    //绘制圆点
    private void drawPoint(Canvas canvas, int centerX, int centerY, int color, LocalDate date) {
        if (mPointList != null && mPointList.contains(date)) {
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(color);
            int solarTexyCenterY = getSolarTexyCenterY(centerY);
            canvas.drawCircle(centerX, mAttrs.pointLocation == Attrs.DOWN ? (solarTexyCenterY + mAttrs.pointDistance) : (solarTexyCenterY - mAttrs.pointDistance), mAttrs.pointSize, mCirclePaint);
        }
    }

    //绘制农历
    private void drawLunar(Canvas canvas, int centerX, int centerY, boolean isWhite, boolean isCurrMonth, NDate nDate) {
        if (mAttrs.isShowLunar) {
            mTextPaint.setTextSize(mAttrs.lunarTextSize);
            //优先顺序 农历节日、节气、公历节日、正常农历日期
            String lunarString;
            if (!TextUtils.isEmpty(nDate.lunarHoliday)) {
                mTextPaint.setColor(isWhite ? Color.WHITE : mAttrs.lunarHolidayTextColor);
                lunarString = nDate.lunarHoliday;
            } else if (!TextUtils.isEmpty(nDate.solarTerm)) {
                mTextPaint.setColor(isWhite ? Color.WHITE : mAttrs.solarTermTextColor);
                lunarString = nDate.solarTerm;
            } else if (!TextUtils.isEmpty(nDate.solarHoliday)) {
                mTextPaint.setColor(isWhite ? Color.WHITE : mAttrs.solarHolidayTextColor);
                lunarString = nDate.solarHoliday;
            } else {
                mTextPaint.setColor(isWhite ? Color.WHITE : mAttrs.lunarTextColor);
                lunarString = nDate.lunar.lunarDrawStr;
            }

            if (!isCurrMonth) {
                mTextPaint.setColor(isWhite ? Color.WHITE : mAttrs.lunarTextColor);
            }

            canvas.drawText(lunarString, centerX, centerY + mAttrs.lunarDistance, mTextPaint);
        }
    }


    //绘制节假日
    private void drawHolidays(Canvas canvas, int centerX, int centerY, boolean isWhite, LocalDate date) {
        if (mAttrs.isShowHoliday) {

            int[] holidayLocation = getHolidayLocation(centerX, centerY);
            mTextPaint.setTextSize(mAttrs.holidayTextSize);

            if (mHolidayList.contains(date.toString())) {
                mTextPaint.setColor(isWhite ? Color.WHITE : mAttrs.holidayColor);
                canvas.drawText("休", holidayLocation[0], holidayLocation[1], mTextPaint);

            } else if (mWorkdayList.contains(date.toString())) {
                mTextPaint.setColor(isWhite ? Color.WHITE : mAttrs.workdayColor);
                canvas.drawText("班", holidayLocation[0], holidayLocation[1], mTextPaint);

            }
        }
    }


    private int[] getHolidayLocation(int centerX, int centerY) {
        int[] location = new int[2];
        int solarTexyCenterY = getSolarTexyCenterY(centerY);

        switch (mAttrs.holidayLocation) {

            case Attrs.TOP_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayDistance);
                location[1] = solarTexyCenterY;
                break;
            case Attrs.BOTTOM_RIGHT:
                location[0] = (int) (centerX + mAttrs.holidayDistance);
                location[1] = centerY;
                break;
            case Attrs.BOTTOM_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayDistance);
                location[1] = centerY;
                break;
            case Attrs.TOP_RIGHT:
            default:
                location[0] = (int) (centerX + mAttrs.holidayDistance);
                location[1] = solarTexyCenterY;
                break;
        }


        return location;

    }


    //公历文字的竖直中心y
    private int getSolarTexyCenterY(int centerY) {
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int ascent = fontMetricsInt.ascent;
        int descent = fontMetricsInt.descent;
        int textCenterY = descent / 2 + centerY + ascent / 2;//文字的中心y
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
    protected abstract List<NDate> getNCalendar(LocalDate initialDate, int type);


    /**
     * 点击事件
     *
     * @param clickNData  点击的date
     * @param initialDate 初始化当前页面的date
     */
    protected abstract void onClick(NDate clickNData, LocalDate initialDate);


    /**
     * 初始化的日期和绘制的日期是否是同月，周都相同
     *
     * @param date
     * @param initialDate
     * @return
     */
    public abstract boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate);


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
                    NDate clickDate = mDateList.get(i);
                    onClick(clickDate, mInitialDate);
                    break;
                }
            }
            return true;
        }
    });


    public void setSelectDate(LocalDate localDate, List<LocalDate> pointList, boolean isDraw) {
        //默认选中和isDraw满足其一就绘制
        this.isDraw = mAttrs.isDefaultSelect || isDraw;
        this.mSelectDate = localDate;
        this.mPointList = pointList;
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
}
