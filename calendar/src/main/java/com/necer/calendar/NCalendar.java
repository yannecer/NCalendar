package com.necer.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarStateChangedListener;
import com.necer.listener.OnDateChangedListener;
import com.necer.utils.Attrs;
import com.necer.view.ChildLayout;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/11/12.
 */
public abstract class NCalendar extends FrameLayout implements NestedScrollingParent, OnCalendarStateChangedListener, OnDateChangedListener {


    protected WeekCalendar weekCalendar;
    protected MonthCalendar monthCalendar;

    protected int weekHeigh;//周日历的高度
    protected int monthHeigh;//月日历的高度,是日历整个的高

    protected int STATE;//默认月
    private int lastSate;//防止状态监听重复回调
    private OnCalendarChangedListener onCalendarChangedListener;

    protected ChildLayout childLayout;//NCalendar内部包含的直接子view，直接子view并不一定是NestScrillChild


    protected Rect monthRect;//月日历大小的矩形
    protected Rect weekRect;//周日历大小的矩形 ，用于判断点击事件是否在日历的范围内


    public NCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setMotionEventSplittingEnabled(false);

        weekCalendar = new WeekCalendar(context, attrs);
        Attrs attrss = weekCalendar.getAttrs();

        int duration = attrss.duration;
        monthHeigh = attrss.monthCalendarHeight;
        STATE = attrss.defaultCalendar;
        weekHeigh = monthHeigh / 5;

        monthCalendar = new MonthCalendar(context, attrs, duration);
        childLayout = new ChildLayout(getContext(), monthHeigh, duration, this);

        monthCalendar.setOnDateChangedListener(this);
        weekCalendar.setOnDateChangedListener(this);

        setState(STATE);

        post(new Runnable() {
            @Override
            public void run() {
                monthRect = new Rect(0, 0, monthCalendar.getWidth(), monthCalendar.getHeight());
                weekRect = new Rect(0, 0, weekCalendar.getWidth(), weekCalendar.getHeight());
            }
        });

    }

    @Override
    public void onCalendarStateChanged(boolean isMonthState) {
        if (isMonthState) {
            setState(Attrs.MONTH);
        } else {
            setState(Attrs.WEEK);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new RuntimeException("NCalendar中的只能有一个直接子view");
        }
        childLayout.addView(getChildAt(0));

        addView(monthCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, monthHeigh));
        addView(weekCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, weekHeigh));
        addView(childLayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams childLayoutLayoutParams = childLayout.getLayoutParams();
        childLayoutLayoutParams.height = getMeasuredHeight() - weekHeigh;


    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int monthCalendarTop;
        int childLayoutTop;
        if (STATE == Attrs.MONTH) {
            monthCalendarTop = monthCalendar.getTop();
            childLayoutTop = childLayout.getTop() == 0 ? monthHeigh : childLayout.getTop();
        } else {
            monthCalendarTop = -monthCalendar.getMonthCalendarOffset();
            childLayoutTop = childLayout.getTop() == 0 ? weekHeigh : childLayout.getTop();
        }

        monthCalendar.layout(0, monthCalendarTop, r, monthHeigh + monthCalendarTop);
        ViewGroup.LayoutParams layoutParams = childLayout.getLayoutParams();
        childLayout.layout(0, childLayoutTop, r, layoutParams.height + childLayoutTop);

        weekCalendar.layout(0, 0, r, weekHeigh);

    }


    private void setState(int state) {
        if (state == Attrs.WEEK) {
            STATE = Attrs.WEEK;
            weekCalendar.setVisibility(VISIBLE);
        } else {
            STATE = Attrs.MONTH;
            weekCalendar.setVisibility(INVISIBLE);
        }

        if (lastSate != state && onCalendarChangedListener != null) {
            onCalendarChangedListener.onCalendarStateChanged(STATE == Attrs.MONTH);
        }

        lastSate = state;
    }

    private void scroll() {
        //停止滑动的时候，距顶部的距离
        int childLayoutTop = childLayout.getTop();

        if (STATE == Attrs.MONTH && monthHeigh - childLayoutTop < weekHeigh) {
            monthCalendar.toMonth();
            childLayout.toMonth();
        } else if (STATE == Attrs.MONTH && monthHeigh - childLayoutTop >= weekHeigh) {
            monthCalendar.toWeek();
            childLayout.toWeek();
        } else if (STATE == Attrs.WEEK && childLayoutTop < weekHeigh * 2) {
            monthCalendar.toWeek();
            childLayout.toWeek();
        } else if (STATE == Attrs.WEEK && childLayoutTop >= weekHeigh * 2) {
            monthCalendar.toMonth();
            childLayout.toMonth();
        }
    }

    @Override
    public void onDateChanged(BaseCalendar baseCalendar, LocalDate localDate, boolean isDraw) {

        if (baseCalendar instanceof MonthCalendar) {
            //月日历 变化
            if (STATE == Attrs.MONTH) {
                weekCalendar.jumpDate(localDate, isDraw);
                requestLayout();
                if (onCalendarChangedListener != null && isDraw) {
                    onCalendarChangedListener.onCalendarDateChanged(localDate);
                }
            }

        } else {
            if (STATE == Attrs.WEEK) {
                monthCalendar.jumpDate(localDate, isDraw);
                requestLayout();
                if (onCalendarChangedListener != null && isDraw) {
                    onCalendarChangedListener.onCalendarDateChanged(localDate);
                }
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //跟随手势滑动
        gestureMove(dy, true, consumed);


        if (childLayout.getTop() == weekHeigh) {
            weekCalendar.setVisibility(VISIBLE);
        } else {
            weekCalendar.setVisibility(INVISIBLE);
        }

    }

    @Override
    public void onStopNestedScroll(View target) {
        //嵌套滑动结束，自动滑动

        if (monthCalendar.isMonthState() && childLayout.isMonthState() && STATE == Attrs.WEEK) {
            setState(Attrs.MONTH);
        } else if (monthCalendar.isWeekState() && childLayout.isWeekState() && STATE == Attrs.MONTH) {
            setState(Attrs.WEEK);
        } else {
            scroll();
        }
    }

    public void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener) {
        this.onCalendarChangedListener = onCalendarChangedListener;
    }


    protected abstract void gestureMove(int dy, boolean isNest, int[] consumed);


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
                gestureMove(dy, false, null);
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
        if (STATE == Attrs.MONTH) {
            return monthRect.contains(x, y);
        } else {
            return weekRect.contains(x, y);
        }
    }

}
