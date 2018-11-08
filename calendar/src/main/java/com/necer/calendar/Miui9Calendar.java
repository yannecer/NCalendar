package com.necer.calendar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.MyLog;
import com.necer.listener.OnDateChangedListener;
import com.necer.utils.Attrs;
import com.necer.view.MonthView;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/11/7.
 */
public class Miui9Calendar extends FrameLayout  implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener, OnDateChangedListener {


    protected WeekCalendar weekCalendar;
    protected MonthCalendar monthCalendar;

    protected int duration;//动画时间
    protected int weekHeigh;//周日历的高度
    protected int monthHeigh;//月日历的高度,是日历整个的高

    protected int STATE;//默认月

    protected View childView;//NCalendar内部包含的直接子view，直接子view并不一定是NestScrillChild
    protected View targetView;//嵌套滑动的目标view，即RecyclerView等


    protected int monthCalendarTop; //月日历的getTop 距离父view顶部的距离
    protected int childViewTop; // childView的getTop 距离父view顶部的距离


    protected ValueAnimator monthValueAnimator;//月日历动画
    protected ValueAnimator childViewValueAnimator;//childView动画


    public Miui9Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        monthCalendar = new MonthCalendar(context, attrs);
        weekCalendar = new WeekCalendar(context, attrs);

        Attrs attrss = monthCalendar.getAttrs();

        duration = attrss.duration;
        monthHeigh = attrss.monthCalendarHeight;
        STATE = attrss.defaultCalendar;

        weekHeigh = monthHeigh / 5;

        addView(monthCalendar);
        addView(weekCalendar);

        ViewGroup.LayoutParams monthLayoutParams = monthCalendar.getLayoutParams();
        ViewGroup.LayoutParams weekLayoutParams = weekCalendar.getLayoutParams();
        monthLayoutParams.height = monthHeigh;
        weekLayoutParams.height = weekHeigh;
        monthCalendar.setLayoutParams(monthLayoutParams);
        weekCalendar.setLayoutParams(weekLayoutParams);

        monthCalendar.setOnDateChangedListener(this);
        weekCalendar.setOnDateChangedListener(this);

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
        move(dy, true, consumed);
    }

    @Override
    public void onStopNestedScroll(View target) {
        //嵌套滑动结束，自动滑动
        scroll();



        MyLog.d("onStopNestedScroll");

    }

    private void scroll() {
        //停止滑动的时候，距顶部的距离
        monthCalendarTop = monthCalendar.getTop();
        childViewTop = childView.getTop();

       /* if (monthCalendarTop == 0 && childViewTop == monthHeigh) {
            return;
        }
        if (monthCalendarTop == -monthCalendar.getMonthCalendarOffset() && childViewTop == weekHeigh) {
            return;
        }*/

        if (STATE == Attrs.MONTH) {
            if (monthHeigh - childViewTop < weekHeigh) {
                autoScroll(monthCalendarTop, 0, childViewTop, monthHeigh);
            } else {
                autoScroll(monthCalendarTop, -monthCalendar.getMonthCalendarOffset(), childViewTop, weekHeigh);
            }

        } else {
            if (childViewTop < weekHeigh * 2) {
                autoScroll(monthCalendarTop, -monthCalendar.getMonthCalendarOffset(), childViewTop, weekHeigh);
            } else {
                autoScroll(monthCalendarTop, 0, childViewTop, monthHeigh);
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
        childViewTop = childView.getTop();

        //4种情况
        if (dy > 0 && Math.abs(monthCalendarTop) < monthCalendar.getMonthCalendarOffset()) {
            //月日历和childView同时上滑
            int offset = getOffset(dy, monthCalendar.getMonthCalendarOffset() - Math.abs(monthCalendarTop));
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
        if (childViewTop == weekHeigh ) {
            STATE = Attrs.WEEK;
            weekCalendar.setVisibility(VISIBLE);
        }

        //周状态，下滑显示月日历，把周日历隐掉
        if (STATE == Attrs.WEEK && dy < 0 && !ViewCompat.canScrollVertically(targetView, -1)) {
            weekCalendar.setVisibility(INVISIBLE);
        }

        //彻底滑到月日历，标记状态
        if (childViewTop == monthHeigh) {
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
        ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
        layoutParams.height = getMeasuredHeight() - weekHeigh;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //总共有三个view，0,1,2
        childView = getChildAt(2);
        if (childView instanceof NestedScrollingChild) {
            targetView = childView;
        } else {
            targetView = getNestedScrollingChild(childView);
        }
        if (targetView == null) {
            throw new RuntimeException("NCalendar中的子类中必须要有NestedScrollingChild的实现类！");
        }
    }

    private View getNestedScrollingChild(View view) {
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ((ViewGroup) view).getChildAt(i);
                if (childAt instanceof NestedScrollingChild) {
                    return childAt;
                } else {
                    getNestedScrollingChild(((ViewGroup) view).getChildAt(i));
                }
            }
        }
        return null;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (STATE == Attrs.MONTH ) {
            monthCalendarTop = monthCalendar.getTop();
            childViewTop = childView.getTop() == 0 ? monthHeigh : childView.getTop();
        } else {
            monthCalendarTop = -monthCalendar.getMonthCalendarOffset();
            childViewTop = childView.getTop() == 0 ? weekHeigh : childView.getTop();
        }

        monthCalendar.layout(0, monthCalendarTop, r, monthHeigh + monthCalendarTop);
        ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
        childView.layout(0, childViewTop, r, layoutParams.height + childViewTop);

        weekCalendar.layout(0, 0, r, weekHeigh);

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
            int top = childView.getTop();
            int i = animatedValue - top;
            childView.offsetTopAndBottom(i);
        }
    }

    @Override
    public void onDateChanged(BaseCalendar baseCalendar, LocalDate localDate,boolean isDraw) {
        if (baseCalendar instanceof MonthCalendar) {
            //月日历 变化
            if (STATE == Attrs.MONTH) {



                weekCalendar.jumpDate(localDate, isDraw);
                /*if (onCalendarChangedListener != null) {
                    onCalendarChangedListener.onCalendarChanged(date);
                }*/
            }

        } else {
            if (STATE == Attrs.WEEK) {
                monthCalendar.jumpDate(localDate, isDraw);
                requestLayout();
               /* if (onCalendarChangedListener != null) {
                    onCalendarChangedListener.onCalendarChanged(date);
                }*/
            }
        }
    }
}
