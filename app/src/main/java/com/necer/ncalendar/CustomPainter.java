package com.necer.ncalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.necer.entity.NDate;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Util;

/**
 * Created by necer on 2019/1/3.
 */
public class CustomPainter implements CalendarPainter {

    private Paint paint;
    private Context context;


    public CustomPainter(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setTextSize(Util.sp2px(context, 15));
        paint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    public void onDrawToday(Canvas canvas, Rect rect, NDate nDate,boolean is) {

        if (false) {
            paint.setColor(Color.RED);
            canvas.drawRect(getNewRect(context,rect), paint);
            paint.setColor(Color.WHITE);
        } else {
            paint.setColor(Color.LTGRAY);
            canvas.drawRect(getNewRect(context,rect), paint);
            paint.setColor(Color.GREEN);
        }
        canvas.drawText(nDate.localDate.getDayOfMonth() + "", rect.centerX(), getBaseLineY(rect), paint);


    }

    @Override
    public void onDrawDisableDate(Canvas canvas, Rect rect, NDate nDate) {

    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, Rect rect, NDate nDate, boolean isSelect) {
        if (isSelect) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(getNewRect(context,rect), paint);
            paint.setColor(Color.WHITE);
        } else {
            paint.setColor(Color.BLACK);
        }
        canvas.drawText(nDate.localDate.getDayOfMonth() + "", rect.centerX(), getBaseLineY(rect), paint);

    }

    @Override
    public void onDrawNotCurrentMonth(Canvas canvas, Rect rect, NDate nDate) {
        paint.setColor(Color.LTGRAY);
        canvas.drawText(nDate.localDate.getDayOfMonth() + "", rect.centerX(), getBaseLineY(rect), paint);
    }


    private int getBaseLineY(Rect rect) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }

    private Rect getNewRect(Context context,Rect rect) {
        int rectDistance = (int) Util.dp2px(context, 20);
        return new Rect(rect.centerX() - rectDistance, rect.centerY() - rectDistance, rect.centerX() + rectDistance, rect.centerY() + rectDistance);
    }
}
