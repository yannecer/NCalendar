package com.necer.ncalendar.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;

import com.necer.ncalendar.DensityUtil;
import com.necer.painter.CalendarPainter;
import com.necer.utils.CalendarUtil;
import com.necer.view.CalendarView;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

import java.util.List;

public class StretchPainter implements CalendarPainter {
    protected Paint mTextPaint;
    protected Paint mBgPaint;

    public StretchPainter(Context context) {
        mBgPaint = getPaint();
        mTextPaint = getPaint();
        mBgPaint.setColor(Color.parseColor("#ff7575"));
        mTextPaint.setTextSize(DensityUtil.dp2px(context, 10));
        mTextPaint.setColor(Color.parseColor("#000000"));
    }


    @Override
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        canvas.drawRect(rectF, mBgPaint);
        canvas.drawText(localDate.getDayOfMonth() + "", rectF.centerX(), getBaseLineY(rectF), mTextPaint);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        canvas.drawRect(rectF, mBgPaint);
        canvas.drawText(localDate.getDayOfMonth() + "", rectF.centerX(), getBaseLineY(rectF), mTextPaint);
    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        canvas.drawRect(rectF, mBgPaint);
        canvas.drawText(localDate.getDayOfMonth() + "", rectF.centerX(), getBaseLineY(rectF), mTextPaint);
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate) {
        canvas.drawRect(rectF, mBgPaint);
        canvas.drawText(localDate.getDayOfMonth() + "", rectF.centerX(), getBaseLineY(rectF), mTextPaint);
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    private int getBaseLineY(RectF rectF) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }

}
