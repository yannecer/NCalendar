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

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public class NCalendar extends FrameLayout implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener, OnWeekCalendarChangedListener, OnMonthCalendarChangedListener {

    private WeekCalendar weekCalendar;
    private MonthCalendar monthCalendar;
    private View childView;//NCalendar内部包含的直接子view，直接子view并不一定是NestScrillChild
    private View targetView;//嵌套滑动的目标view，即RecyclerView等
    public static final int MONTH = 100;
    public static final int WEEK = 200;
    private static int STATE = 100;//默认月
    private int weekHeigh;//周日历的高度
    private int monthHeigh;//月日历的高度,是日历整个的高度，并非是月日历绘制区域的高度

    private int monthCalendarTop; //月日历的getTop
    private int childViewTop; // childView的getTop

    private int duration;//动画时间
    private int monthCalendarOffset;//月日历需要滑动的距离
    private ValueAnimator monthValueAnimator;//月日历动画
    private ValueAnimator childViewValueAnimator;//childView动画

    private Rect monthRect;//月日历大小的矩形
    private Rect weekRect;//周日历大小的矩形 ，用于判断点击事件是否在日历的范围内

    private OnCalendarChangedListener onCalendarChangedListener;


    public NCalendar(Context context) {
        this(context, null);
    }

    public NCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //禁止多点触摸
        setMotionEventSplittingEnabled(false);

        monthCalendar = new MonthCalendar(context, attrs);
        weekCalendar = new WeekCalendar(context, attrs);

        duration = Attrs.duration;
        monthHeigh = Attrs.monthCalendarHeight;
        STATE = Attrs.defaultCalendar;

        weekHeigh = monthHeigh / 5;
        monthCalendar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, monthHeigh));
        weekCalendar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, weekHeigh));

        addView(monthCalendar);
        addView(weekCalendar);

        monthCalendar.setOnMonthCalendarChangedListener(this);
        weekCalendar.setOnWeekCalendarChangedListener(this);

        post(new Runnable() {
            @Override
            public void run() {
                weekCalendar.setVisibility(STATE == MONTH ? INVISIBLE : VISIBLE);

                monthRect = new Rect(0, monthCalendar.getTop(), monthCalendar.getWidth(), monthCalendar.getHeight());
                weekRect = new Rect(0, weekCalendar.getTop(), weekCalendar.getWidth(), weekCalendar.getHeight());


            }
        });

        monthValueAnimator = new ValueAnimator();
        childViewValueAnimator = new ValueAnimator();

        monthValueAnimator.addUpdateListener(this);
        childViewValueAnimator.addUpdateListener(this);
        childViewValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int top = childView.getTop();
                if (top == monthHeigh) {
                    STATE = MONTH;
                    weekCalendar.setVisibility(INVISIBLE);
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
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //跟随手势滑动
        move(dy, true, consumed);
    }

    @Override
    public void onStopNestedScroll(View target) {
        //嵌套滑动结束，自动滑动
        scroll();
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

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //防止快速滑动
        childViewTop = childView.getTop();
        if (childViewTop > weekHeigh) {
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


    /**
     * 得到NestedScrollingChild的实现类
     *
     * @param view
     * @return
     */
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

        if (STATE == MONTH) {
            monthCalendarTop = monthCalendar.getTop();
            childViewTop = childView.getTop() == 0 ? monthHeigh : childView.getTop();
        } else {
            monthCalendarTop = -getMonthCalendarOffset();
            childViewTop = childView.getTop() == 0 ? weekHeigh : childView.getTop();
        }

        monthCalendar.layout(0, monthCalendarTop, r, monthHeigh + monthCalendarTop);
        ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
        childView.layout(0, childViewTop, r, layoutParams.height + childViewTop);

        weekCalendar.layout(0, 0, r, weekHeigh);

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

    //自动滑动
    private void autoScroll(int startMonth, int endMonth, int startChild, int endChild) {
        monthValueAnimator.setIntValues(startMonth, endMonth);
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.start();

        childViewValueAnimator.setIntValues(startChild, endChild);
        childViewValueAnimator.setDuration(duration);
        childViewValueAnimator.start();
    }

    public void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener) {
        this.onCalendarChangedListener = onCalendarChangedListener;
    }


    /**
     * 防止滑动过快越界
     *
     * @param offset
     * @param maxOffset
     * @return
     */
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
        } else {
            int animatedValue = (int) animation.getAnimatedValue();
            int top = childView.getTop();
            int i = animatedValue - top;
            childView.offsetTopAndBottom(i);
        }
    }

    @Override
    public void onWeekCalendarChanged(DateTime dateTime) {
        if (STATE == WEEK) {
            monthCalendar.setDateTime(dateTime);
            requestLayout();
            if (onCalendarChangedListener != null) {
                onCalendarChangedListener.onCalendarChanged(dateTime);
            }
        }
    }

    @Override
    public void onMonthCalendarChanged(DateTime dateTime) {
        //monthCalendarOffset在这里赋值，月日历改变的时候
        monthCalendarOffset = getMonthCalendarOffset();

        if (STATE == MONTH) {
            weekCalendar.setDateTime(dateTime);
            if (onCalendarChangedListener != null) {
                onCalendarChangedListener.onCalendarChanged(dateTime);
            }
        }
    }


    private int dowmY;
    private int downX;
    private int lastY;//上次的y
    private int verticalY = 50;//竖直方向上滑动的临界值，大于这个值认为是竖直滑动
    private boolean isFirstScroll = true; //第一次手势滑动，因为第一次滑动的偏移量大于verticalY，会出现猛的一划，这里只对第一次滑动做处理

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dowmY = (int) ev.getY();
                downX = (int) ev.getX();
                lastY = dowmY;
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                int absY = Math.abs(dowmY - y);
                boolean inCalendar = isInCalendar(downX, dowmY);
                if (absY > verticalY && inCalendar) {
                    //onInterceptTouchEvent返回true，触摸事件交给当前的onTouchEvent处理
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                int y = (int) event.getY();
                int dy = lastY - y;

                if (isFirstScroll) {
                    // 防止第一次的偏移量过大
                    if (dy > verticalY) {
                        dy = dy - verticalY;
                    } else if (dy < -verticalY) {
                        dy = dy + verticalY;
                    }
                    isFirstScroll = false;
                }

                // 跟随手势滑动
                move(dy, false, null);

                lastY = y;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isFirstScroll = true;
                scroll();
                break;
        }
        return true;
    }


    /**
     * 点击事件是否在日历的范围内
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isInCalendar(int x, int y) {
        if (STATE == MONTH) {
            return monthRect.contains(x, y);
        } else {
            return weekRect.contains(x, y);
        }
    }


    /**
     * 跳转制定日期
     *
     * @param formatDate yyyy-MM-dd
     */
    public void setDate(String formatDate) {
        DateTime dateTime = new DateTime(formatDate);
        if (STATE == MONTH) {
            monthCalendar.setDateTime(dateTime);
        } else {
            weekCalendar.setDateTime(dateTime);
        }
    }

    /**
     * 由周日历转换到月日历
     */
    public void toMonth() {
        if (STATE == WEEK) {
            monthCalendarTop = monthCalendar.getTop();
            childViewTop = childView.getTop();
            weekCalendar.setVisibility(INVISIBLE);
            autoScroll(monthCalendarTop, 0, childViewTop, monthHeigh);
        }
    }

    /**
     * 由月日历到周日历
     */
    public void toWeek() {
        if (STATE == MONTH) {
            int monthCalendarOffset = getMonthCalendarOffset();
            autoScroll(0, -monthCalendarOffset, monthHeigh, weekHeigh);
        }
    }

    /**
     * 回到今天
     */
    public void toToday() {
        if (STATE == MONTH) {
            monthCalendar.toToday();
        } else {
            weekCalendar.toToday();
        }
    }

    /**
     * 设置指示圆点
     *
     * @param pointList
     */
    public void setPoint(List<String> pointList) {
        monthCalendar.setPointList(pointList);
        weekCalendar.setPointList(pointList);
    }

    public void toNextPager() {
        if (STATE == MONTH) {
            monthCalendar.toNextPager();
        } else {
            weekCalendar.toNextPager();
        }
    }

    public void toLastPager() {
        if (STATE == MONTH) {
            monthCalendar.toLastPager();
        } else {
            weekCalendar.toLastPager();
        }
    }

    /**
     * 设置日期区间
     * @param startString
     * @param endString
     */
    public void setDateInterval(String startString,String endString) {
        monthCalendar.setDateInterval(startString,endString);
        weekCalendar.setDateInterval(startString,endString);
    }


    /**
     * 返回100是月，返回200是周
     * @return
     */
    public int getState() {
        return STATE;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try{
            super.onRestoreInstanceState(state);
        } catch (Exception e){
            state = null;
        }
    }
}
