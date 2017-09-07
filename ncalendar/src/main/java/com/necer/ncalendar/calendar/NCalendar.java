package com.necer.ncalendar.calendar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.necer.ncalendar.R;
import com.necer.ncalendar.listener.OnCalendarChangeListener;
import com.necer.ncalendar.listener.OnClickMonthCalendarListener;
import com.necer.ncalendar.listener.OnClickWeekCalendarListener;
import com.necer.ncalendar.listener.OnMonthCalendarPageChangeListener;
import com.necer.ncalendar.listener.OnWeekCalendarPageChangeListener;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.NMonthView;

import org.joda.time.DateTime;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NCalendar extends FrameLayout implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener, OnClickMonthCalendarListener, OnClickWeekCalendarListener, OnWeekCalendarPageChangeListener, OnMonthCalendarPageChangeListener {

    private NWeekCalendar weekCalendar;
    private NMonthCalendar monthCalendar;
    private View nestedScrollingChild;

    public static final int MONTH = 100;
    public static final int WEEK = 200;
    private static int STATE = 100;//默认月
    private int weekHeigh;//周日历的高度
    private int monthHeigh;//月日历的高度
    private int duration = 240;//动画时间

    private int monthCalendarTop;//月日历到顶部的距离，0到负monthHeigh的值
    private int childViewTop;//nestedScrollingChild到顶部的距离 ，这两个变量用于在动画结束后，重新确定月日历和childview的位置

    private int monthCalendarOffset;//月日历需要滑动的距离

    private ValueAnimator monthValueAnimator;//月日历动画
    private ValueAnimator nestedScrollingChildValueAnimator;//nestedScrollingChild动画

    public NCalendar(Context context) {
        this(context, null);
    }

    public NCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        monthHeigh = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, Utils.dp2px(context, 300));
        duration = ta.getInt(R.styleable.NCalendar_duration, 240);
        ta.recycle();

        monthCalendar = new NMonthCalendar(context, attrs);
        weekCalendar = new NWeekCalendar(context, attrs);

        weekHeigh = monthHeigh / 5;
        monthCalendar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, monthHeigh));
        weekCalendar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, weekHeigh));

        addView(monthCalendar);

        //初始的位置
        monthCalendarTop = 0;
        childViewTop = monthHeigh;

        monthCalendar.setOnClickMonthCalendarListener(this);
        monthCalendar.setOnMonthCalendarPageChangeListener(this);
        weekCalendar.setOnClickWeekCalendarListener(this);
        weekCalendar.setOnWeekCalendarPageChangeListener(this);

        post(new Runnable() {
            @Override
            public void run() {
                ViewParent parent = getParent();
                if (!(parent instanceof RelativeLayout)) {
                    throw new RuntimeException("MWCalendar的父view必须是RelativeLayout");
                }
                ((RelativeLayout) parent).addView(weekCalendar);
                weekCalendar.setVisibility(INVISIBLE);
            }
        });


        monthValueAnimator = new ValueAnimator();
        nestedScrollingChildValueAnimator = new ValueAnimator();

        monthValueAnimator.addUpdateListener(this);
        nestedScrollingChildValueAnimator.addUpdateListener(this);
        nestedScrollingChildValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int top = nestedScrollingChild.getTop();
                if (top == monthHeigh) {
                    STATE = MONTH;
                } else {
                    STATE = WEEK;
                    weekCalendar.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onStopNestedScroll(View target) {
        int monthCalendarTop = monthCalendar.getTop();
        int nestedScrollingChildTop = nestedScrollingChild.getTop();

        if (monthCalendarTop == 0 && nestedScrollingChildTop == monthHeigh) {
            return;
        }
        if (monthCalendarTop == -monthCalendarOffset && nestedScrollingChildTop == weekHeigh) {
            return;
        }

        if (STATE == MONTH) {
            if (monthHeigh - nestedScrollingChildTop < weekHeigh) {
                autoOpen(monthCalendarTop, 0, nestedScrollingChildTop, monthHeigh);
            } else {
                autoClose(monthCalendarTop, -monthCalendarOffset, nestedScrollingChildTop, weekHeigh);
            }

        } else {
            if (nestedScrollingChildTop < weekHeigh * 2) {
                autoClose(monthCalendarTop, -monthCalendarOffset, nestedScrollingChildTop, weekHeigh);
            } else {
                autoOpen(monthCalendarTop, 0, nestedScrollingChildTop, monthHeigh);
            }
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        int monthTop = monthCalendar.getTop();
        int nestedScrollingChildTop = nestedScrollingChild.getTop();

        monthCalendarOffset = getMonthCalendarOffset();

        //4种情况
        if (dy > 0 && Math.abs(monthTop) < monthCalendarOffset) {
            //月日历和nestedScrollingChild上滑上滑

            int offset = getOffset(dy, monthCalendarOffset - Math.abs(monthTop));
            monthCalendar.offsetTopAndBottom(-offset);
            nestedScrollingChild.offsetTopAndBottom(-offset);
            consumed[1] = dy;
        } else if (dy > 0 && nestedScrollingChildTop > weekHeigh) {
            //月日历滑动到位置后，nestedScrollingChild继续上滑，覆盖一部分月日历

            int offset = getOffset(dy, nestedScrollingChildTop - weekHeigh);
            nestedScrollingChild.offsetTopAndBottom(-offset);
            consumed[1] = dy;
        } else if (dy < 0 && monthTop != 0 && !ViewCompat.canScrollVertically(target, -1)) {
            //月日历和nestedScrollingChild下滑

            int offset = getOffset(Math.abs(dy), Math.abs(monthTop));
            monthCalendar.offsetTopAndBottom(offset);
            nestedScrollingChild.offsetTopAndBottom(offset);
            consumed[1] = dy;
        } else if (dy < 0 && monthTop == 0 && nestedScrollingChildTop != monthHeigh && !ViewCompat.canScrollVertically(target, -1)) {
            //月日历滑动到位置后，nestedScrollingChild继续下滑

            int offset = getOffset(Math.abs(dy), monthHeigh - nestedScrollingChildTop);
            nestedScrollingChild.offsetTopAndBottom(offset);
            consumed[1] = dy;
        }


        //nestedScrollingChild滑动到周位置后，标记状态，同时周日显示
        if (nestedScrollingChildTop == weekHeigh) {
            STATE = WEEK;
            weekCalendar.setVisibility(VISIBLE);
        }

        //周状态，下滑显示月日历，把周日历隐掉
        if (STATE == WEEK && dy < 0 && !ViewCompat.canScrollVertically(target, -1)) {
            weekCalendar.setVisibility(INVISIBLE);
        }

        //彻底滑到月日历，标记状态
        if (nestedScrollingChildTop == monthHeigh) {
            STATE = MONTH;
        }
    }


    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //防止快速滑动
        int nestedScrollingChildTop = nestedScrollingChild.getTop();
        if (nestedScrollingChildTop > weekHeigh) {
            return true;
        }
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = nestedScrollingChild.getLayoutParams();
        layoutParams.height = getMeasuredHeight() - weekHeigh;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nestedScrollingChild = getChildAt(1);

        if (!(nestedScrollingChild instanceof NestedScrollingChild)) {
            throw new RuntimeException("子view必须实现NestedScrollingChild");
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //  super.onLayout(changed, l, t, r, b);

        if (STATE == MONTH) {
            monthCalendarTop = 0;
            childViewTop = monthHeigh;
        } else {
            monthCalendarTop = -getMonthCalendarOffset();
            childViewTop = nestedScrollingChild.getTop();
        }

        monthCalendar.layout(0, monthCalendarTop, r, monthHeigh + monthCalendarTop);
        ViewGroup.LayoutParams layoutParams = nestedScrollingChild.getLayoutParams();
        nestedScrollingChild.layout(0, childViewTop, r, layoutParams.height + childViewTop);
    }



    //月日历需要滑动的距离，
    private int getMonthCalendarOffset() {
        NMonthView currectMonthView = monthCalendar.getCurrectMonthView();
        //该月有几行
        int rowNum = currectMonthView.getRowNum();
        //现在选中的是第几行
        int selectRowIndex = currectMonthView.getSelectRowIndex();
        //month需要移动selectRowIndex*h/rowNum ,计算时依每个行高的中点计算
        int monthCalendarOffset = selectRowIndex * monthHeigh / rowNum;
        return monthCalendarOffset;
    }

    //自动开
    private void autoOpen(int startMonth, int endMonth, int startChild, int endChild) {
        monthValueAnimator.setIntValues(startMonth, endMonth);
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.start();

        nestedScrollingChildValueAnimator.setIntValues(startChild, endChild);
        nestedScrollingChildValueAnimator.setDuration(duration);
        nestedScrollingChildValueAnimator.start();
    }

    //自动闭
    private void autoClose(int startMonth, int endMonth, int startChild, int endChild) {
        monthValueAnimator.setIntValues(startMonth, endMonth);
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.start();

        nestedScrollingChildValueAnimator.setIntValues(startChild, endChild);
        nestedScrollingChildValueAnimator.setDuration(duration);
        nestedScrollingChildValueAnimator.start();
    }

    private OnCalendarChangeListener onClickCalendarListener;

    public void setOnClickCalendarListener(OnCalendarChangeListener onClickCalendarListener) {
        this.onClickCalendarListener = onClickCalendarListener;
    }


    private int getOffset(int offset, int maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        }
        return offset;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == monthValueAnimator) {
            int animatedValue = (int) animation.getAnimatedValue();
            int top = monthCalendar.getTop();
            int i = animatedValue - top;
            monthCalendar.offsetTopAndBottom(i);
        }

        if (animation == nestedScrollingChildValueAnimator) {
            int animatedValue = (int) animation.getAnimatedValue();
            int top = nestedScrollingChild.getTop();
            int i = animatedValue - top;
            nestedScrollingChild.offsetTopAndBottom(i);
        }
    }

    //requestLayout();用于重新定位位置，不然当日历setdateTime()的时候，如果当前页面未初始化，
    // 会出现月日历的位置重新返回原位置的问题，所以需要requestLayout()重新执行onLayout()确定位置
    @Override
    public void onClickMonthCalendar(DateTime dateTime) {
        if (STATE == MONTH) {
            requestLayout();
            weekCalendar.setDateTime(dateTime);
        }
    }

    @Override
    public void onClickWeekCalendar(DateTime dateTime) {
        if (STATE == WEEK) {
            requestLayout();
            monthCalendar.setDateTime(dateTime);
        }
    }

    @Override
    public void onWeekCalendarPageSelected(DateTime dateTime) {
        if (STATE == WEEK) {
            requestLayout();
            monthCalendar.setDateTime(dateTime);
        }

    }

    @Override
    public void onMonthCalendarPageSelected(DateTime dateTime) {
        if (STATE == MONTH) {
            requestLayout();
            weekCalendar.setDateTime(dateTime);
        }
    }
}
