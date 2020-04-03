package com.necer.painter;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import com.necer.drawable.TextDrawable;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2020/3/27.
 */
public class NumBackground implements CalendarBackground {


    private TextDrawable mTextDrawable;
    private int mAlphaColor;

    public NumBackground(float textSize, int color, int alphaColor) {
        this.mAlphaColor = alphaColor;
        mTextDrawable = new TextDrawable(textSize);
        mTextDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    @Override
    public Drawable getBackgroundDrawable(LocalDate localDate, int currentDistance, int totalDistance) {
        int alphaColor = mAlphaColor * currentDistance / totalDistance;
        mTextDrawable.setAlpha(alphaColor);
        mTextDrawable.setText(String.valueOf(localDate.getMonthOfYear()));
        return mTextDrawable;
    }
}
