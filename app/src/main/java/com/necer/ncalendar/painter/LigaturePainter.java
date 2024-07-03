package com.necer.ncalendar.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;

import com.necer.ncalendar.DensityUtil;
import com.necer.painter.CalendarPainter;
import com.necer.utils.NDateUtil;
import com.necer.utils.hutool.ChineseDate;

import java.time.LocalDate;
import java.util.List;

/**
 * 连线Painter
 */
public class LigaturePainter implements CalendarPainter {


    protected Paint mTextPaint;
    protected Paint mBgPaint;
    private float mCircleRadius;
    private Context mContext;

    public LigaturePainter(Context context) {
        mContext = context;
        mTextPaint = getPaint();
        mBgPaint = getPaint();

        mCircleRadius = DensityUtil.dp2px(context, 20);
        mBgPaint.setColor(Color.parseColor("#ff7575"));
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    @Override
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        drawSelectBg(canvas, rectF, localDate, true, selectedDateList);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate), true);
        drawLunar(canvas, rectF, localDate, selectedDateList.contains(localDate), true);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        drawSelectBg(canvas, rectF, localDate, true, selectedDateList);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate), true);
        drawLunar(canvas, rectF, localDate, selectedDateList.contains(localDate), true);
    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        drawSelectBg(canvas, rectF, localDate, false, selectedDateList);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate), false);
        drawLunar(canvas, rectF, localDate, selectedDateList.contains(localDate), false);
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, RectF rect, LocalDate localDate) {

    }

    //绘制选中背景
    private void drawSelectBg(Canvas canvas, RectF rectF, LocalDate localDate, boolean isCurrectMonthOrWeek, List<LocalDate> selectedDateList) {

        mBgPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);

        LocalDate lastLocalDate = localDate.minusDays(1);
        LocalDate nextLocalDate = localDate.plusDays(1);

        if (selectedDateList.contains(localDate)) {
            if (selectedDateList.contains(lastLocalDate) && selectedDateList.contains(nextLocalDate) && NDateUtil.INSTANCE.isEqualsMonth(lastLocalDate, nextLocalDate)) {
                //画全整个矩形
                RectF rectF1 = new RectF(rectF.left - 1f, rectF.centerY() - mCircleRadius, rectF.right + 1f, rectF.centerY() + mCircleRadius);
                mBgPaint.setAntiAlias(false);
                mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawRect(rectF1, mBgPaint);

            } else if (selectedDateList.contains(lastLocalDate) && (!selectedDateList.contains(nextLocalDate) || !NDateUtil.INSTANCE.isEqualsMonth(nextLocalDate, localDate)) && NDateUtil.INSTANCE.isEqualsMonth(lastLocalDate, localDate)) {
                //左矩形 右圆
                RectF rectF1 = new RectF(rectF.left- 1f, rectF.centerY() - mCircleRadius, rectF.centerX(), rectF.centerY() + mCircleRadius);
                mBgPaint.setAntiAlias(false);
                mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawRect(rectF1, mBgPaint);

                mBgPaint.setAntiAlias(false);
                mBgPaint.setStyle(Paint.Style.FILL);
                RectF rectF2 = new RectF(rectF.centerX() - mCircleRadius, rectF.centerY() - mCircleRadius, rectF.centerX() + mCircleRadius, rectF.centerY() + mCircleRadius);
                canvas.drawArc(rectF2, -90, 180, false, mBgPaint);//右半圆

                mBgPaint.setAntiAlias(true);
                mBgPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectF2, -90, 180, false, mBgPaint);//右半圆弧

            } else if ((!selectedDateList.contains(lastLocalDate) || !NDateUtil.INSTANCE.isEqualsMonth(lastLocalDate, localDate)) && selectedDateList.contains(nextLocalDate) && NDateUtil.INSTANCE.isEqualsMonth(nextLocalDate, localDate)) {
                //右矩形 左圆
                RectF rectF1 = new RectF(rectF.centerX(), rectF.centerY() - mCircleRadius, rectF.right+1f, rectF.centerY() + mCircleRadius);
                mBgPaint.setAntiAlias(false);
                mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawRect(rectF1, mBgPaint);

                mBgPaint.setAntiAlias(false);
                mBgPaint.setStyle(Paint.Style.FILL);
                RectF rectF2 = new RectF(rectF.centerX() - mCircleRadius, rectF.centerY() - mCircleRadius, rectF.centerX() + mCircleRadius, rectF.centerY() + mCircleRadius);
                canvas.drawArc(rectF2, 90, 180, false, mBgPaint);//右半圆

                mBgPaint.setAntiAlias(true);
                mBgPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectF2, 90, 180, false, mBgPaint);//右半圆弧

            } else {
                //圆形
                mBgPaint.setAntiAlias(true);
                mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), mCircleRadius, mBgPaint);
            }
        }
    }

    //绘制公历
    private void drawSolar(Canvas canvas, RectF rectF, LocalDate date, boolean isSelected, boolean isCurrectMonthOrWeek) {
        mTextPaint.setTextSize(DensityUtil.dp2px(mContext, 16));
        mTextPaint.setColor(isSelected ? Color.WHITE : Color.BLACK);
        mTextPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);
        canvas.drawText(date.getDayOfMonth() + "", rectF.centerX(), rectF.centerY(), mTextPaint);
    }

    //绘制农历
    private void drawLunar(Canvas canvas, RectF rectF, LocalDate date, boolean isSelected, boolean isCurrectMonthOrWeek) {
        mTextPaint.setTextSize(DensityUtil.dp2px(mContext, 10));
        ChineseDate chineseDate = new ChineseDate(date);
        mTextPaint.setColor(isSelected ? Color.WHITE : Color.GRAY);
        mTextPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);
        canvas.drawText(chineseDate.getChineseMonthName(), rectF.centerX(), rectF.centerY() + DensityUtil.dp2px(mContext, 12), mTextPaint);
    }


}
