package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NMonthView extends View{
    public NMonthView(Context context) {
        super(context);
    }

    public NMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

    }
}
