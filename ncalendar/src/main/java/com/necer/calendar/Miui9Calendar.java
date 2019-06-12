package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.utils.Attrs;

/**
 * Created by necer on 2018/11/7.
 */
public class Miui9Calendar extends MiuiCalendar {


    public Miui9Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected float getGestureMonthUpOffset(int dy) {
        float maxOffset;
        if (STATE == Attrs.MONTH) {
            //月  月日历有选中则选中为 中心点，如果没有选中则第一行
            maxOffset = monthCalendar.getPivotDistanceFromTop() - Math.abs(monthCalendar.getY()); //结束位置
        } else {
            //周的情况，按照周的第一个数据为中心点
            maxOffset = monthCalendar.getDistanceFromTop(weekCalendar.getFirstDate()) - Math.abs(monthCalendar.getY());
        }
        return getOffset(dy, maxOffset);
    }

    @Override
    protected float getGestureMonthDownOffset(int dy) {
        float maxOffset = Math.abs(monthCalendar.getY());
        return getOffset(Math.abs(dy), maxOffset);
    }

    @Override
    protected float getGestureChildDownOffset(int dy) {
        float maxOffset = monthHeight - childLayout.getY();
        return getOffset(Math.abs(dy), maxOffset);
    }


    @Override
    protected float getGestureChildUpOffset(int dy) {
        float maxOffset = childLayout.getY() - weekHeight;
        return getOffset(dy, maxOffset);
    }


}
