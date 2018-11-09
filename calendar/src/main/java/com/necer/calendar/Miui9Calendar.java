package com.necer.calendar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.MyLog;
import com.necer.listener.OnDateChangedListener;
import com.necer.utils.Attrs;
import com.necer.view.ChildLayout;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/11/7.
 */
public class Miui9Calendar extends FrameLayout implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener, OnDateChangedListener {


    protected WeekCalendar weekCalendar;
    protected MonthCalendar monthCalendar;

    protected int duration;//动画时间
    protected int weekHeigh;//周日历的高度
    protected int monthHeigh;//月日历的高度,是日历整个的高

    protected int STATE;//默认月

    protected ChildLayout childLayout;//NCalendar内部包含的直接子view，直接子view并不一定是NestScrillChild



    protected int monthCalendarTop; //月日历的getTop 距离父view顶部的距离
    protected int childLayoutTop; // childView的getTop 距离父view顶部的距离


    protected ValueAnimator monthValueAnimator;//月日历动画
    protected ValueAnimator childViewValueAnimator;//childView动画


    public Miui9Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        monthCalendar = new MonthCalendar(context, attrs);
        weekCalendar = new WeekCalendar(context, attrs);

        childLayout = new ChildLayout(getContext());

        Attrs attrss = monthCalendar.getAttrs();

        duration = attrss.duration;
        monthHeigh = attrss.monthCalendarHeight;
        STATE = attrss.defaultCalendar;

        weekHeigh = monthHeigh / 5;

    //    monthCalendar.setOnDateChangedListener(this);
      //  weekCalendar.setOnDateChangedListener(this);

        weekCalendar.setVisibility(STATE == Attrs.MONTH ? INVISIBLE : VISIBLE);

        monthValueAnimator = new ValueAnimator();
        childViewValueAnimator = new ValueAnimator();
        monthValueAnimator.addUpdateListener(this);
        childViewValueAnimator.addUpdateListener(this);

    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //跟随手势滑动
        // move(dy, true, consumed);
    }

    @Override
    public void onStopNestedScroll(View target) {
        //嵌套滑动结束，自动滑动
        // scroll();



    }

    private void scroll() {
        //停止滑动的时候，距顶部的距离
        monthCalendarTop = monthCalendar.getTop();
        childLayoutTop = childLayout.getTop();

       /* if (monthCalendarTop == 0 && childViewTop == monthHeigh) {
            return;
        }
        if (monthCalendarTop == -monthCalendar.getMonthCalendarOffset() && childViewTop == weekHeigh) {
            return;
        }*/

        if (STATE == Attrs.MONTH) {
            if (monthHeigh - childLayoutTop < weekHeigh) {
                autoScroll(monthCalendarTop, 0, childLayoutTop, monthHeigh);
            } else {
                autoScroll(monthCalendarTop, -monthCalendar.getMonthCalendarOffset(), childLayoutTop, weekHeigh);
            }

        } else {
            if (childLayoutTop < weekHeigh * 2) {
                autoScroll(monthCalendarTop, -monthCalendar.getMonthCalendarOffset(), childLayoutTop, weekHeigh);
            } else {
                autoScroll(monthCalendarTop, 0, childLayoutTop, monthHeigh);
            }
        }
    }


    //自动滑动
    protected void autoScroll(int startMonth, int endMonth, int startChild, int endChild) {
        monthValueAnimator.setIntValues(startMonth, endMonth);
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.start();

        childViewValueAnimator.setIntValues(startChild, endChild);
        childViewValueAnimator.setDuration(duration);
        childViewValueAnimator.start();
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
        childLayoutTop = childLayout.getTop();

        //4种情况
        if (dy > 0 && Math.abs(monthCalendarTop) < monthCalendar.getMonthCalendarOffset()) {
            //月日历和childView同时上滑
            int offset = getOffset(dy, monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendarTop));
            monthCalendar.offsetTopAndBottom(-offset);
            childLayout.offsetTopAndBottom(-offset);
            if (isNest) consumed[1] = dy;
        } else if (dy > 0 && childLayoutTop > weekHeigh) {
            //月日历滑动到位置后，childView继续上滑，覆盖一部分月日历
            int offset = getOffset(dy, childLayoutTop - weekHeigh);
            childLayout.offsetTopAndBottom(-offset);
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && monthCalendarTop != 0 && !childLayout.canScrollVertically( -1)) {
            //月日历和childView下滑
            int offset = getOffset(Math.abs(dy), Math.abs(monthCalendarTop));
            monthCalendar.offsetTopAndBottom(offset);
            childLayout.offsetTopAndBottom(offset);
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && monthCalendarTop == 0 && childLayoutTop != monthHeigh && !childLayout.canScrollVertically(-1)) {
            //月日历滑动到位置后，childView继续下滑
            int offset = getOffset(Math.abs(dy), monthHeigh - childLayoutTop);
            childLayout.offsetTopAndBottom(offset);
            if (isNest) consumed[1] = dy;
        }

        //childView滑动到周位置后，标记状态，同时周日显示
        if (childLayoutTop == weekHeigh) {
            STATE = Attrs.WEEK;
            weekCalendar.setVisibility(VISIBLE);
        }

        //周状态，下滑显示月日历，把周日历隐掉
        if (STATE == Attrs.WEEK && dy < 0 && !childLayout.canScrollVertically(-1)) {
            weekCalendar.setVisibility(INVISIBLE);
        }

        //彻底滑到月日历，标记状态
        if (childLayoutTop == monthHeigh) {
            STATE = Attrs.MONTH;
        }
    }


    //防止滑动过快越界
    protected int getOffset(int offset, int maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        }
        return offset;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams childLayoutLayoutParams = childLayout.getLayoutParams();
        childLayoutLayoutParams.height = getMeasuredHeight() - weekHeigh;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 1) {
            throw new RuntimeException("NCalendar中的只能有一个直接子view");
        }


        childLayout.addView(getChildAt(0));
        addView(childLayout);

        addView(monthCalendar,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,monthHeigh));
        addView(weekCalendar,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,weekHeigh));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);

        int top = monthCalendar.getTop();
        MyLog.d("top::" + top);

        ViewGroup.LayoutParams layoutParams = childLayout.getLayoutParams();
        childLayout.layout(0, monthHeigh, r, layoutParams.height + monthHeigh);

       /* if (STATE == Attrs.MONTH) {
            monthCalendarTop = monthCalendar.getTop();
            childLayoutTop = childLayout.getTop() == 0 ? monthHeigh : childLayout.getTop();
        } else {
            monthCalendarTop = -monthCalendar.getMonthCalendarOffset();
            childLayoutTop = childLayout.getTop() == 0 ? weekHeigh : childLayout.getTop();
        }

        monthCalendar.layout(0, monthCalendarTop, r, monthHeigh + monthCalendarTop);
        ViewGroup.LayoutParams layoutParams = childLayout.getLayoutParams();
        childLayout.layout(0, childLayoutTop, r, layoutParams.height + childLayoutTop);

        weekCalendar.layout(0, 0, r, weekHeigh);*/

      /*  int top = monthCalendar.getTop();

        MyLog.d("top::" + top);


        monthCalendar.layout(0, -100, r, monthHeigh);

        ViewGroup.LayoutParams layoutParams = childLayout.getLayoutParams();
        childLayout.layout(0, monthHeigh, r, layoutParams.height + monthHeigh);*/


    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == monthValueAnimator) {
            int animatedValue = (int) animation.getAnimatedValue();
            int top = monthCalendar.getTop();
            int i = animatedValue - top;
            monthCalendar.offsetTopAndBottom(i);
        } else {
            int animatedValue = (int) animation.getAnimatedValue();
            int top = childLayout.getTop();
            int i = animatedValue - top;
            childLayout.offsetTopAndBottom(i);
        }
    }

    @Override
    public void onDateChanged(BaseCalendar baseCalendar, LocalDate localDate, boolean isDraw) {
       /* if (baseCalendar instanceof MonthCalendar) {
            //月日历 变化
            if (STATE == Attrs.MONTH) {
                weekCalendar.jumpDate(localDate, isDraw);
                *//*if (onCalendarChangedListener != null) {
                    onCalendarChangedListener.onCalendarChanged(date);
                }*//*
            }

        } else {
            if (STATE == Attrs.WEEK) {
                monthCalendar.jumpDate(localDate, isDraw);
                requestLayout();
               *//* if (onCalendarChangedListener != null) {
                    onCalendarChangedListener.onCalendarChanged(date);
                }*//*
            }
        }*/
    }
}
