package com.necer.ncalendar.calendar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.necer.ncalendar.listener.OnMonthCalendarChangedListener;
import com.necer.ncalendar.listener.OnWeekCalendarChangedListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.view.MonthView;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public class Miui9Calendar extends NCalendar {



    public Miui9Calendar(Context context) {
        this(context, null);
    }

    public Miui9Calendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Miui9Calendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 手势滑动的主要逻辑
     *
     * @param dy       y方向上的偏移量
     * @param isNest   是否是NestedScrollingChild的滑动
     * @param consumed
     */
    private void move(int dy, boolean isNest, int[] consumed) {

        monthCalendarTop = monthCalendar.getTop();
        childViewTop = childView.getTop();

        //4种情况
        if (dy > 0 && Math.abs(monthCalendarTop) < monthCalendarOffset) {
            //月日历和childView同时上滑
            int offset = getOffset(dy, monthCalendarOffset - Math.abs(monthCalendarTop));
            monthCalendar.offsetTopAndBottom(-offset);
            childView.offsetTopAndBottom(-offset);
            if (isNest) consumed[1] = dy;
        } else if (dy > 0 && childViewTop > weekHeigh) {
            //月日历滑动到位置后，childView继续上滑，覆盖一部分月日历
            int offset = getOffset(dy, childViewTop - weekHeigh);
            childView.offsetTopAndBottom(-offset);
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && monthCalendarTop != 0 && !ViewCompat.canScrollVertically(targetView, -1)) {
            //月日历和childView下滑
            int offset = getOffset(Math.abs(dy), Math.abs(monthCalendarTop));
            monthCalendar.offsetTopAndBottom(offset);
            childView.offsetTopAndBottom(offset);
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && monthCalendarTop == 0 && childViewTop != monthHeigh && !ViewCompat.canScrollVertically(targetView, -1)) {
            //月日历滑动到位置后，childView继续下滑
            int offset = getOffset(Math.abs(dy), monthHeigh - childViewTop);
            childView.offsetTopAndBottom(offset);
            if (isNest) consumed[1] = dy;
        }

        //childView滑动到周位置后，标记状态，同时周日显示
        if (childViewTop == weekHeigh) {
            STATE = WEEK;
            weekCalendar.setVisibility(VISIBLE);
        }

        //周状态，下滑显示月日历，把周日历隐掉
        if (STATE == WEEK && dy < 0 && !ViewCompat.canScrollVertically(targetView, -1)) {
            weekCalendar.setVisibility(INVISIBLE);
        }

        //彻底滑到月日历，标记状态
        if (childViewTop == monthHeigh) {
            STATE = MONTH;
        }
    }

    /**
     * 自动滑动的主要逻辑
     */
    private void scroll() {
        //停止滑动的时候，距顶部的距离
        monthCalendarTop = monthCalendar.getTop();
        childViewTop = childView.getTop();

        if (monthCalendarTop == 0 && childViewTop == monthHeigh) {
            return;
        }
        if (monthCalendarTop == -monthCalendarOffset && childViewTop == weekHeigh) {
            return;
        }

        if (STATE == MONTH) {
            if (monthHeigh - childViewTop < weekHeigh) {
                autoScroll(monthCalendarTop, 0, childViewTop, monthHeigh);
            } else {
                autoScroll(monthCalendarTop, -monthCalendarOffset, childViewTop, weekHeigh);
            }

        } else {
            if (childViewTop < weekHeigh * 2) {
                autoScroll(monthCalendarTop, -monthCalendarOffset, childViewTop, weekHeigh);
            } else {
                autoScroll(monthCalendarTop, 0, childViewTop, monthHeigh);
            }
        }
    }

    //月日历需要滑动的距离，
    private int getMonthCalendarOffset() {
        MonthView currectMonthView = monthCalendar.getCurrectMonthView();
        //该月有几行
        int rowNum = currectMonthView.getRowNum();
        //现在选中的是第几行
        int selectRowIndex = currectMonthView.getSelectRowIndex();
        //month需要移动selectRowIndex*h/rowNum ,计算时依每个行的中点计算
        int monthCalendarOffset = selectRowIndex * currectMonthView.getDrawHeight() / rowNum;

        return monthCalendarOffset;
    }



    /**
     * 跳转制定日期
     *
     * @param formatDate yyyy-MM-dd
     */
    public void setDate(String formatDate) {
        LocalDate date = new LocalDate(formatDate);
        if (STATE == MONTH) {
            monthCalendar.setDate(date);
        } else {
            weekCalendar.setDate(date);
        }
    }

}
