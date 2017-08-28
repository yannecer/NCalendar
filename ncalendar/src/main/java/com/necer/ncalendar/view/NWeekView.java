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

public class NWeekView extends View{
    public NWeekView(Context context) {
        super(context);
    }

    public NWeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NWeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLUE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, 150);


    }
}
