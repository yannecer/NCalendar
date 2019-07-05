package com.necer.ncalendar.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.necer.MyLog;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Util;

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


        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(Util.dp2px(context, 20));

        mBgPaint.setColor(Color.parseColor("#ff7575"));
        circleRadius = (int) Util.dp2px(context, 20);

    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    @Override
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        LocalDate lastLocalDate = localDate.minusDays(1);
        LocalDate nextLocalDate = localDate.plusDays(1);

        if (selectedDateList.contains(localDate)) {
            if (selectedDateList.contains(lastLocalDate) && selectedDateList.contains(nextLocalDate)) {
                drawSelectBg(canvas, rectF, localDate, 2);
            } else if (selectedDateList.contains(lastLocalDate) && !selectedDateList.contains(nextLocalDate)) {
                drawSelectBg(canvas, rectF, localDate, 4);
            } else if (!selectedDateList.contains(lastLocalDate) && selectedDateList.contains(nextLocalDate)) {
                drawSelectBg(canvas, rectF, localDate, 3);
            } else {
                drawSelectBg(canvas, rectF, localDate, 1);
            }
        }

        drawSolar(canvas, rectF, localDate);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {


        LocalDate lastLocalDate = localDate.minusDays(1);
        LocalDate nextLocalDate = localDate.plusDays(1);

        if (selectedDateList.contains(localDate)) {
            if (selectedDateList.contains(lastLocalDate) && selectedDateList.contains(nextLocalDate)) {
                drawSelectBg(canvas, rectF, localDate, 2);
            } else if (selectedDateList.contains(lastLocalDate) && !selectedDateList.contains(nextLocalDate)) {
                drawSelectBg(canvas, rectF, localDate, 4);
            } else if (!selectedDateList.contains(lastLocalDate) && selectedDateList.contains(nextLocalDate)) {
                drawSelectBg(canvas, rectF, localDate, 3);
            } else {
                drawSelectBg(canvas, rectF, localDate, 1);
            }
        }


        drawSolar(canvas, rectF, localDate);


        new RectF();
    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rect, LocalDate localDate, List<LocalDate> selectedDateList) {
        drawSolar(canvas, rect, localDate);
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, RectF rect, LocalDate localDate) {

    }

    //bgType 1==圆  2全矩形 3左半边圆右半边矩形    4左半边矩形右半边圆
    private void drawSelectBg(Canvas canvas, RectF rectF, LocalDate localDate, int bgType) {

        if (bgType == 1) {
            mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), circleRadius, mBgPaint);
        } else if (bgType == 2) {
            RectF rectF1 = new RectF(rectF.left, rectF.centerY() - circleRadius, rectF.right, rectF.centerY() + circleRadius);
            canvas.drawRect(rectF1, mBgPaint);
        } else if (bgType == 3) {
            mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), circleRadius, mBgPaint);
            RectF rectF1 = new RectF(rectF.centerX(), rectF.centerY() - circleRadius, rectF.right, rectF.centerY() + circleRadius);
            canvas.drawRect(rectF1, mBgPaint);
        } else if (bgType == 4) {
            RectF rectF1 = new RectF(rectF.left, rectF.centerY() - circleRadius, rectF.centerX(), rectF.centerY() + circleRadius);
            canvas.drawRect(rectF1, mBgPaint);
            mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), circleRadius, mBgPaint);
        }
    }

    //绘制公历
    private void drawSolar(Canvas canvas, RectF rect, LocalDate date) {

        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), getBaseLineY(rect), mTextPaint);
    }

    private void drawLunar() {

    }


    private int getBaseLineY(RectF rect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }


}
