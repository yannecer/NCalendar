package com.necer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by necer on 2020/3/20.
 */
public class CalendarViewPager extends ViewPager {


    private boolean mScrollEnable = true;


    public CalendarViewPager(@NonNull Context context) {
        super(context);
    }

    public CalendarViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollEnable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    public boolean scrollEnable() {
        return mScrollEnable;
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.mScrollEnable = scrollEnable;
    }

}
