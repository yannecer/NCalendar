package com.necer.ncalendar.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.necer.painter.CalendarPainter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * 连线Painter
 */
public class LigaturePainter implements CalendarPainter {


    protected Paint mTextPaint;
    protected Paint mBgPaint;
    private int circleRadius;

    public LigaturePainter(Context context) {

        mTextPaint = getPaint();
        mBgPaint = getPaint();

        mTextPaint.setTextSize(CalendarUtil.dp2px(context, 20));

        mBgPaint.setColor(Color.parseColor("#ff7575"));
        circleRadius = (int) CalendarUtil.dp2px(context, 20);

    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    @Override
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {

        drawSelectBg(canvas, rectF, localDate, true, selectedDateList);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate));
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {


       // canvas.drawArc(rectF,-90,180,false,mBgPaint);//右半圆
       // canvas.drawArc(rectF,90,180,false,mBgPaint);//左半圆



        drawSelectBg(canvas, rectF, localDate, true, selectedDateList);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate));

    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        drawSelectBg(canvas, rectF, localDate, false, selectedDateList);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate));
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, RectF rect, LocalDate localDate) {

    }

    private void drawSelectBg(Canvas canvas, RectF rectF, LocalDate localDate, boolean isCurrectMonthOrWeek, List<LocalDate> selectedDateList) {

        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBgPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);

        LocalDate lastLocalDate = localDate.minusDays(1);
        LocalDate nextLocalDate = localDate.plusDays(1);


        if (selectedDateList.contains(localDate)) {
            if (selectedDateList.contains(lastLocalDate) && selectedDateList.contains(nextLocalDate) && CalendarUtil.isEqualsMonth(lastLocalDate, nextLocalDate)) {
                //画全整个矩形
                RectF rectF1 = new RectF(rectF.left, rectF.centerY() - circleRadius, rectF.right, rectF.centerY() + circleRadius);
                canvas.drawRect(rectF1, mBgPaint);
            } else if (selectedDateList.contains(lastLocalDate) && (!selectedDateList.contains(nextLocalDate) || !CalendarUtil.isEqualsMonth(nextLocalDate, localDate)) && CalendarUtil.isEqualsMonth(lastLocalDate, localDate)) {
                //左矩形 右圆
                RectF rectF1 = new RectF(rectF.left, rectF.centerY() - circleRadius, rectF.centerX(), rectF.centerY() + circleRadius);
                canvas.drawRect(rectF1, mBgPaint);
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), circleRadius, mBgPaint);

               // rectF.set(rectF.left,);
             //   canvas.drawArc(rectF1,-90,180,false,mBgPaint);

            } else if ((!selectedDateList.contains(lastLocalDate) || !CalendarUtil.isEqualsMonth(lastLocalDate, localDate)) && selectedDateList.contains(nextLocalDate) && CalendarUtil.isEqualsMonth(nextLocalDate, localDate)) {
                //右矩形 左圆
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), circleRadius, mBgPaint);
                RectF rectF1 = new RectF(rectF.centerX(), rectF.centerY() - circleRadius, rectF.right, rectF.centerY() + circleRadius);
                canvas.drawRect(rectF1, mBgPaint);
            } else {
                //圆形
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), circleRadius, mBgPaint);
            }
        }
    }

    //绘制公历
    private void drawSolar(Canvas canvas, RectF rect, LocalDate date, boolean isSelected) {
        mTextPaint.setColor(isSelected ? Color.WHITE : Color.BLACK);
        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), getBaseLineY(rect), mTextPaint);
    }

    private int getBaseLineY(RectF rect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }

}
