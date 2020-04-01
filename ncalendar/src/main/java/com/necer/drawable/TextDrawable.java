package com.necer.drawable;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *
 * @author necer
 * @date 2020/3/27
 */
public class TextDrawable extends Drawable {


    private Paint mTextPaint;
    private String mText;

    public TextDrawable(float textSize) {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    public void setText(String text) {
        this.mText = text;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect rect = getBounds();
        canvas.drawColor(Color.WHITE);
        canvas.drawText(mText, rect.centerX(), getTextBaseLineY(rect.centerY()), mTextPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mTextPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mTextPaint.setColorFilter(colorFilter);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }


    private float getTextBaseLineY(float centerY) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return centerY - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
    }
}
