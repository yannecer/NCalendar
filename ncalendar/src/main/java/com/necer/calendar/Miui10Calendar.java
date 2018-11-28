package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


/**
 * miui10日历滑动处理的不太好，因为monthCalendar和childLayout需要同时滑到某一点，
 * 两者之间的速度有个倍数关系，但是倍数速度转化成int时，丢失了精度，造成滑动不是那么及时，
 * 快速滑动时，bug不明显，慢慢滑动，该bug就比较明显了
 * 具体处理在getGestureMonthUpOffset()和getGestureChildDownOffset()，不完美
 * Created by necer on 2018/11/12.
 */
public class Miui10Calendar extends MiuiCalendar {


    public Miui10Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 滑动距离处理的有点问题 慢慢滑动能复现
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return
     */
    @Override
    protected int getGestureMonthUpOffset(int dy) {

        int maxOffset = monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendar.getTop());
        float monthCalendarOffset = monthCalendar.getMonthCalendarOffset();
        float childLayoutOffset = childLayout.getChildLayoutOffset();

        int offset = (int) ((monthCalendarOffset * dy) / childLayoutOffset);

        if (offset == 0) {
            return getOffset(dy, maxOffset);
        } else {
            return getOffset(offset, maxOffset);
        }
    }

    /**
     * 滑动距离处理的有点问题 慢慢滑动能复现
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return
     */
    @Override
    protected int getGestureMonthDownOffset(int dy) {

        int maxOffset = Math.abs(monthCalendar.getTop());
        float monthCalendarOffset = monthCalendar.getMonthCalendarOffset();
        float childLayoutOffset = childLayout.getChildLayoutOffset();
        int offset = (int) ((monthCalendarOffset * dy) / childLayoutOffset);

        if (offset == 0) {
            return getOffset(Math.abs(dy), maxOffset);
        } else {
            return getOffset(Math.abs(offset), maxOffset);
        }
    }

    @Override
    protected int getGestureChildDownOffset(int dy) {
        int maxOffset = monthHeight - childLayout.getTop();
        return getOffset(Math.abs(dy), maxOffset);
    }

    @Override
    protected int getGestureChildUpOffset(int dy) {
        int maxOffset = childLayout.getTop() - weekHeight;
        return getOffset(dy, maxOffset);
    }

}
