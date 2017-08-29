package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

/**
 * Created by 闫彬彬 on 2017/8/29.
 * QQ:619008099
 */

public abstract class NCalendarView extends View {

    protected DateTime mSelectDateTime;//被选中的datetime
    protected DateTime mInitialDateTime;//初始传入的datetime，
    protected int mWidth;
    protected int mHeight;

    protected Paint mSorlarPaint;
    protected Paint mLunarPaint;




    public NCalendarView(Context context) {
        this(context,null);
    }

    public NCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSorlarPaint = getPaint(Color.parseColor("#000000"), Utils.dp2px(context,15));
    }




    private Paint getPaint(int paintColor, float paintSize) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setTextSize(paintSize);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }
    public DateTime getInitialDateTime() {
        return mInitialDateTime;
    }

    public DateTime getSelectDateTime() {
        return mSelectDateTime;
    }
    public void setSelectDateTime(DateTime dateTime) {
        this.mSelectDateTime = dateTime;
        invalidate();
    }

    public void clear() {
        this.mSelectDateTime = null;
        invalidate();
    }
}
