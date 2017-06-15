package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.necer.ncalendar.utils.Attrs;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2017/6/9.
 */

public abstract class CalendarView extends View {


    protected DateTime mSelectDateTime;//被选中的datetime
    protected DateTime mInitialDateTime;//初始传入的datetime，

    protected int mSolarTextColor;//公历字体颜色
    protected int mLunarTextColor;//农历字体颜色
    protected int mHintColor;//不是当月的颜色
    protected float mSolarTextSize;
    protected float mLunarTextSize;
    protected Paint mSorlarPaint;
    protected Paint mLunarPaint;
    protected int mSelectCircleRadius;//选中圆的半径
    protected int mSelectCircleColor;//选中圆的颜色
    protected boolean isShowLunar;//是否显示农历

    protected List<Rect> mRectList;//点击用的矩形集合
    protected int mPointColor ;//圆点颜色
    protected List<String> mPointList;//圆点集合
    protected float mPointSize;//圆点大小

    protected int mHollowCircleColor;//空心圆颜色
    protected int mHollowCircleStroke;//空心圆粗细


    protected int mWidth;
    protected int mHeight;

    public CalendarView(Context context) {
        super(context);

        mSolarTextColor = Attrs.solarTextColor;
        mLunarTextColor = Attrs.lunarTextColor;
        mHintColor = Attrs.hintColor;
        mSolarTextSize = Attrs.solarTextSize;
        mLunarTextSize = Attrs.lunarTextSize;
        mSelectCircleRadius = Attrs.selectCircleRadius;
        mSelectCircleColor = Attrs.selectCircleColor;
        isShowLunar = Attrs.isShowLunar;

        mPointSize = Attrs.pointSize;
        mPointColor = Attrs.pointColor;
        mHollowCircleColor = Attrs.hollowCircleColor;
        mHollowCircleStroke = Attrs.hollowCircleStroke;


        mRectList = new ArrayList<>();
        mPointList = new ArrayList<>();
        mSorlarPaint = getPaint(mSolarTextColor, mSolarTextSize);
        mLunarPaint = getPaint(mLunarTextColor, mLunarTextSize);


    }

    public void setSelectDateTime(DateTime dateTime) {
        this.mSelectDateTime = dateTime;
        invalidate();
    }

    public void clear() {
        this.mSelectDateTime = null;
        invalidate();
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

    public void setPointList(List<String> pointList) {
        mPointList.clear();
        mPointList.addAll(pointList);
        invalidate();
    }

}
