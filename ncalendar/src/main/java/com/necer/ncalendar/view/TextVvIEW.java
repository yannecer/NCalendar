package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class TextVvIEW extends AppCompatTextView {
    public TextVvIEW(Context context) {
        super(context);
    }

    public TextVvIEW(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextVvIEW(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextVvIEW(Context context, int color) {
        super(context);
        this.color = color;
    }

    private int color;


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(color);
    }
}
