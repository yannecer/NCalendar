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

import com.necer.MyLog;
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

        setCalenadrState(STATE);

        post(new Runnable() {
            @Override
            public void run() {
                monthRect = new Rect(0, 0, monthCalendar.getWidth(), monthCalendar.getHeight());
                weekRect = new Rect(0, 0, weekCalendar.getWidth(), weekCalendar.getHeight());
            }
        });

    }

    //根据child的autoScroll结束的状态来设置月周日历的状态
    @Override
    public void onCalendarStateChanged(boolean isMonthState) {
        if (isMonthState) {
            setCalenadrState(Attrs.MONTH);
        } else {
            setCalenadrState(Attrs.WEEK);
        }
    }

    //xml文件加载结束，添加月，周日历和child到NCalendar中
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
        //super.onLayout(changed, l, t, r, b); 调用父类的该方法会造成 快速滑动月日历同时快速上滑recyclerview造成月日历的残影
        int monthCalendarTop;
        int childLayoutTop;
        if (STATE == Attrs.MONTH) {
            monthCalendarTop = monthCalendar.getTop();
            childLayoutTop = childLayout.getTop() == 0 ? monthHeigh : childLayout.getTop();
        } else {
            monthCalendarTop = -monthCalendar.getMonthCalendarOffset();
            childLayoutTop = childLayout.getTop() == 0 ? weekHeigh : childLayout.getTop();
        }

        monthCalendar.layout(l, monthCalendarTop, r, monthHeigh + monthCalendarTop);
        childLayout.layout(l, childLayoutTop, r, childLayout.getMeasuredHeight() + childLayoutTop);
        weekCalendar.layout(l, 0, r, weekHeigh);


    }


    //根据条件设置日历的月周状态
    private void setCalenadrState(int state) {
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


    //自动滑动到适当的位置
    private void autoScroll() {
        //停止滑动的时候，距顶部的距离
        int childLayoutTop = childLayout.getTop();

        if (STATE == Attrs.MONTH && monthHeigh - childLayoutTop < weekHeigh) {
            monthCalendar.autoToMonth();
            childLayout.autoToMonth();
        } else if (STATE == Attrs.MONTH && monthHeigh - childLayoutTop >= weekHeigh) {
            monthCalendar.autoToWeek();
            childLayout.autoToWeek();
        } else if (STATE == Attrs.WEEK && childLayoutTop < weekHeigh * 2) {
            monthCalendar.autoToWeek();
            childLayout.autoToWeek();
        } else if (STATE == Attrs.WEEK && childLayoutTop >= weekHeigh * 2) {
            monthCalendar.autoToMonth();
            childLayout.autoToMonth();
        }
    }


    //月日历和周日历的日期变化回调，每次变化都会回调
    @Override
    public void onDateChanged(BaseCalendar baseCalendar, LocalDate localDate, boolean isDraw) {

        if (baseCalendar instanceof MonthCalendar) {
            //月日历 变化
            if (STATE == Attrs.MONTH) {
                weekCalendar.jumpDate(localDate, isDraw);
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
    }

    @Override
    public void onStopNestedScroll(View target) {
        //嵌套滑动结束，根据条件自动滑动
        if (monthCalendar.isMonthState() && childLayout.isMonthState() && STATE == Attrs.WEEK) {
            setCalenadrState(Attrs.MONTH);
        } else if (monthCalendar.isWeekState() && childLayout.isWeekState() && STATE == Attrs.MONTH) {
            setCalenadrState(Attrs.WEEK);
        } else {
            autoScroll();
        }
    }

    public void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener) {
        this.onCalendarChangedListener = onCalendarChangedListener;
    }


    //手势滑动的逻辑，做了简单处理，2种状态，1、向上滑动未到周状态 2、向下滑动未到月状态
    protected void gestureMove(int dy, boolean isNest, int[] consumed) {
        if (dy > 0 && (!monthCalendar.isWeekState() || !childLayout.isWeekState())) {
            //月日历和childView同时上滑
            monthCalendar.offsetTopAndBottom(-getGestureMonthUpOffset(dy));
            childLayout.offsetTopAndBottom(-getGestureChildUpOffset(dy));
            if (isNest) consumed[1] = dy;
        } else if (dy < 0 && (!monthCalendar.isMonthState() || !childLayout.isMonthState()) && !childLayout.canScrollVertically(-1)) {
            monthCalendar.offsetTopAndBottom(getGestureMonthDownOffset(dy));
            childLayout.offsetTopAndBottom(getChildLayoutDownOffset(dy));
            if (isNest) consumed[1] = dy;
        }


        //手势控制周日历的显示
        if (childLayout.getTop() == weekHeigh && weekCalendar.getVisibility() == INVISIBLE) {
            weekCalendar.setVisibility(VISIBLE);
        } else if (weekCalendar.getVisibility() == VISIBLE) {
            weekCalendar.setVisibility(INVISIBLE);
        }
    }

    //月日历根据手势向上移动的距离
    protected abstract int getGestureMonthUpOffset(int dy);
    //Child根据手势向上移动的距离
    protected abstract int getGestureChildUpOffset(int dy);
    //月日历根据手势向下移动的距离
    protected abstract int getGestureMonthDownOffset(int dy);
    //Child根据手势向下移动的距离
    protected abstract int getChildLayoutDownOffset(int dy);

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
                autoScroll();
                break;
        }
        return true;
    }


    /**
     * 点击事件是否在日历的范围内
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


    //过界处理
    protected int getOffset(int offset, int maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        }
        return offset;
    }

}
