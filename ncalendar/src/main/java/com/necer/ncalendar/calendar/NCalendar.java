package com.necer.ncalendar.calendar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.ncalendar.listener.OnCalendarChangeListener;
import com.necer.ncalendar.listener.OnClickMonthCalendarListener;
import com.necer.ncalendar.listener.OnClickWeekCalendarListener;
import com.necer.ncalendar.listener.OnMonthCalendarPageChangeListener;
import com.necer.ncalendar.listener.OnWeekCalendarPageChangeListener;
import com.necer.ncalendar.utils.Attrs;
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

    private View targetView;//嵌套滑动的目标vier，RecyclerView等

    public static final int MONTH = 100;
    public static final int WEEK = 200;
    private static int STATE = 100;//默认月
    private int weekHeigh;//周日历的高度
    private int monthHeigh;//月日历的高度,是日历整个的高度，并非是月日历绘制区域的高度
    private int duration;//动画时间

    private int monthCalendarTop;//月日历到顶部的距离，0到负monthHeigh的值
    private int childViewTop;//nestedScrollingChild到顶部的距离 ，这两个变量用于在动画结束后，重新确定月日历和childview的位置

    private int monthCalendarOffset;//月日历需要滑动的距离

    private ValueAnimator monthValueAnimator;//月日历动画
    private ValueAnimator nestedScrollingChildValueAnimator;//nestedScrollingChild动画

    private boolean isNestChildScrolling;


    private Rect monthRect;
    private Rect weekRect;

    public NCalendar(Context context) {
        this(context, null);
    }

    public NCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        monthCalendar = new NMonthCalendar(context, attrs);
        weekCalendar = new NWeekCalendar(context, attrs);

        duration = Attrs.duration;
        monthHeigh = Attrs.monthCalendarHeight;
        STATE = Attrs.defaultCalendar;

        weekHeigh = monthHeigh / 5;
        monthCalendar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, monthHeigh));
        weekCalendar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, weekHeigh));

        addView(monthCalendar);
        addView(weekCalendar);

        monthCalendar.setOnClickMonthCalendarListener(this);
        monthCalendar.setOnMonthCalendarPageChangeListener(this);
        weekCalendar.setOnClickWeekCalendarListener(this);
        weekCalendar.setOnWeekCalendarPageChangeListener(this);


        post(new Runnable() {
            @Override
            public void run() {
                weekCalendar.setVisibility(STATE == MONTH ? INVISIBLE : VISIBLE);

                monthRect = new Rect(0, monthCalendar.getTop(), monthCalendar.getWidth(), monthCalendar.getHeight());
                weekRect = new Rect(0, weekCalendar.getTop(), weekCalendar.getWidth(), weekCalendar.getHeight());
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
    public void onStopNestedScroll(View target) {

        //停止滑动，恢复日历的滑动
        weekCalendar.setScrollEnable(true);
        monthCalendar.setScrollEnable(true);

        isNestChildScrolling = false;
        scroll();

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        //滑动时，禁止日历的滑动
        weekCalendar.setScrollEnable(false);
        monthCalendar.setScrollEnable(false);

        isNestChildScrolling = true;
        move(dy, true, consumed);
    }


    /**
     * 手势滑动的主要逻辑
     *
     * @param dy
     * @param isNest
     * @param consumed
     */
    private void move(int dy, boolean isNest, int[] consumed) {

        int monthTop = monthCalendar.getTop();
        int nestedScrollingChildTop = nestedScrollingChild.getTop();

        monthCalendarOffset = getMonthCalendarOffset();

        //4种情况
        if (dy > 0 && Math.abs(monthTop) < monthCalendarOffset) {
            //月日历和nestedScrollingChild同时上滑
            int offset = getOffset(dy, monthCalendarOffset - Math.abs(monthTop));
            monthCalendar.offsetTopAndBottom(-offset);
            nestedScrollingChild.offsetTopAndBottom(-offset);
            if (isNest) consumed[1] = dy;
        } else if (dy > 0 && nestedScrollingChildTop > weekHeigh) {
            //月日历滑动到位置后，nestedScrollingChild继续上滑，覆盖一部分月日历
            int offset = getOffset(dy, nestedScrollingChildTop - weekHeigh);
            nestedScrollingChild.offsetTopAndBottom(-offset);
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && monthTop != 0 && !ViewCompat.canScrollVertically(targetView, -1)) {
            //月日历和nestedScrollingChild下滑
            int offset = getOffset(Math.abs(dy), Math.abs(monthTop));
            monthCalendar.offsetTopAndBottom(offset);
            nestedScrollingChild.offsetTopAndBottom(offset);
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && monthTop == 0 && nestedScrollingChildTop != monthHeigh && !ViewCompat.canScrollVertically(targetView, -1)) {
            //月日历滑动到位置后，nestedScrollingChild继续下滑
            int offset = getOffset(Math.abs(dy), monthHeigh - nestedScrollingChildTop);
            nestedScrollingChild.offsetTopAndBottom(offset);
            if (isNest) consumed[1] = dy;
        }

        //nestedScrollingChild滑动到周位置后，标记状态，同时周日显示
        if (nestedScrollingChildTop == weekHeigh) {
            STATE = WEEK;
            weekCalendar.setVisibility(VISIBLE);
        }

        //周状态，下滑显示月日历，把周日历隐掉
        if (STATE == WEEK && dy < 0 && !ViewCompat.canScrollVertically(targetView, -1)) {
            weekCalendar.setVisibility(INVISIBLE);
        }

        //彻底滑到月日历，标记状态
        if (nestedScrollingChildTop == monthHeigh) {
            STATE = MONTH;
        }
    }

    /**
     * 自动滑动的主要逻辑
     */
    private void scroll() {
        //停止滑动的时候，距顶部的距离
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
                autoScroll(monthCalendarTop, 0, nestedScrollingChildTop, monthHeigh);
            } else {
                autoScroll(monthCalendarTop, -monthCalendarOffset, nestedScrollingChildTop, weekHeigh);
            }

        } else {
            if (nestedScrollingChildTop < weekHeigh * 2) {
                autoScroll(monthCalendarTop, -monthCalendarOffset, nestedScrollingChildTop, weekHeigh);
            } else {
                autoScroll(monthCalendarTop, 0, nestedScrollingChildTop, monthHeigh);
            }
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
        //总共有三个view，0,1,2
        nestedScrollingChild = getChildAt(2);
        if (nestedScrollingChild instanceof NestedScrollingChild) {
            targetView = nestedScrollingChild;
        } else {
            targetView = getNestedScrollingChild(nestedScrollingChild);
        }
        if (targetView == null) {
            throw new RuntimeException("NCalendar中的子类中必须要有NestedScrollingChild的实现类！");
        }

    }


    /**
     * 得到NestedScrollingChild的实现类
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
            childViewTop = nestedScrollingChild.getTop() == 0 ? monthHeigh : nestedScrollingChild.getTop();
        } else {
            monthCalendarTop = -getMonthCalendarOffset();
            childViewTop = nestedScrollingChild.getTop() == 0 ? weekHeigh : nestedScrollingChild.getTop();

        }

        monthCalendar.layout(0, monthCalendarTop, r, monthHeigh + monthCalendarTop);
        ViewGroup.LayoutParams layoutParams = nestedScrollingChild.getLayoutParams();
        nestedScrollingChild.layout(0, childViewTop, r, layoutParams.height + childViewTop);

        weekCalendar.layout(0, 0, r, weekHeigh);

    }


    //月日历需要滑动的距离，
    private int getMonthCalendarOffset() {
        NMonthView currectMonthView = monthCalendar.getCurrectMonthView();
        //该月有几行
        int rowNum = currectMonthView.getRowNum();
        //现在选中的是第几行
        int selectRowIndex = currectMonthView.getSelectRowIndex();

        //month需要移动selectRowIndex*h/rowNum ,计算时依每个行高的中点计算
        int monthCalendarOffset = selectRowIndex * currectMonthView.getDrawHeight() / rowNum;

        return monthCalendarOffset;
    }

    //自动滑动
    private void autoScroll(int startMonth, int endMonth, int startChild, int endChild) {
        monthValueAnimator.setIntValues(startMonth, endMonth);
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.start();

        nestedScrollingChildValueAnimator.setIntValues(startChild, endChild);
        nestedScrollingChildValueAnimator.setDuration(duration);
        nestedScrollingChildValueAnimator.start();
    }


    private OnCalendarChangeListener onClickCalendarListener;

    public void setOnCalendarChangeListener(OnCalendarChangeListener onClickCalendarListener) {
        this.onClickCalendarListener = onClickCalendarListener;
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
            weekCalendar.setDateTime(dateTime);
            requestLayout();
        }

        if (onClickCalendarListener != null) {
            onClickCalendarListener.onClickCalendar(dateTime);
        }

    }

    @Override
    public void onClickWeekCalendar(DateTime dateTime) {
        if (STATE == WEEK) {
            monthCalendar.setDateTime(dateTime);
            requestLayout();
        }

        if (onClickCalendarListener != null) {
            onClickCalendarListener.onClickCalendar(dateTime);
        }
    }

    @Override
    public void onWeekCalendarPageSelected(DateTime dateTime) {
        if (STATE == WEEK) {
            monthCalendar.setDateTime(dateTime);
            requestLayout();
        }

        if (onClickCalendarListener != null) {
            onClickCalendarListener.onCalendarPageChanged(dateTime);
        }

    }

    @Override
    public void onMonthCalendarPageSelected(DateTime dateTime) {
        if (STATE == MONTH) {
            weekCalendar.setDateTime(dateTime);
            requestLayout();
        }
        if (onClickCalendarListener != null) {
            onClickCalendarListener.onCalendarPageChanged(dateTime);
        }
    }



    private int dowmY;
    private int downX;
    private int lastY;
    private int verticalY = 50;//竖直方向上滑动的临界值，大于这个值认为是竖直滑动


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isNestChildScrolling) {
            return super.onInterceptTouchEvent(ev);
        }

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
                //防止出现过大的移动
                if (dy > verticalY) {
                    dy = dy - verticalY;
                }
                if (dy < -verticalY) {
                    dy = dy + verticalY;
                }
                move(dy, false, null);

                lastY = y;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                scroll();
                break;
        }
        return true;
    }


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
     * @param year
     * @param month
     * @param day
     */
    public void setDate(int year, int month, int day) {
        DateTime dateTime = new DateTime(year, month, day, 0, 0, 0);

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
            int monthCalendarTop = monthCalendar.getTop();
            int childTop = nestedScrollingChild.getTop();
            weekCalendar.setVisibility(INVISIBLE);
            autoScroll(monthCalendarTop, 0, childTop, monthHeigh);
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
            monthCalendar.setDateTime(new DateTime());
        } else {
            weekCalendar.setDateTime(new DateTime());
        }
    }
}
