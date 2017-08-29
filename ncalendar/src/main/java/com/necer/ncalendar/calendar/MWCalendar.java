package com.necer.ncalendar.calendar;

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
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.necer.ncalendar.R;
import com.necer.ncalendar.listener.OnCalendarChangeListener;
import com.necer.ncalendar.listener.OnClickMonthCalendarListener;
import com.necer.ncalendar.listener.OnClickWeekCalendarListener;
import com.necer.ncalendar.listener.OnMonthCalendarPageChangeListener;
import com.necer.ncalendar.listener.OnWeekCalendarPageChangeListener;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.MonthView;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by necer on 2017/6/14.
 */

public class MWCalendar extends LinearLayout implements NestedScrollingParent, OnMonthCalendarPageChangeListener, OnClickMonthCalendarListener, OnClickWeekCalendarListener, OnWeekCalendarPageChangeListener {



    private WeekCalendar weekCalendar;
    private MonthCalendar monthCalendar;
    private View nestedScrollingChild;
    private OverScroller mScroller;

    public static final int OPEN = 100;
    public static final int CLOSE = 200;
    private static int STATE = 100;//默认开
    private int rowHeigh;
    private int duration;

    public MWCalendar(Context context) {
        this(context, null);
    }

    public MWCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MWCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
        monthCalendar = new MonthCalendar(context, attrs);
        addView(monthCalendar);

        weekCalendar = new WeekCalendar(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        float dimension = ta.getDimension(R.styleable.NCalendar_calendarHeight, Utils.dp2px(context, 240));
        duration = ta.getInt(R.styleable.NCalendar_duration, 500);
        ta.recycle();

        rowHeigh = (int) (dimension / 6);
        monthCalendar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeigh * 6));
        weekCalendar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, rowHeigh));

        monthCalendar.setOnMonthCalendarPageChangeListener(this);
        monthCalendar.setOnClickMonthCalendarListener(this);
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
            }
        });
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //  return super.onNestedFling(target, velocityX, velocityY, consumed);
        return false;
    }

    @Override
    public void onStopNestedScroll(View target) {
        //停止滑动，恢复日历的滑动
        weekCalendar.setScrollEnable(true);
        monthCalendar.setScrollEnable(true);

        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == rowHeigh * 5) {
            return;
        }

        if (STATE == OPEN) {
            if (scrollY > 100) {
                startScroll(scrollY, rowHeigh * 5 - scrollY, duration * (rowHeigh * 5 - scrollY) / (rowHeigh * 5));
            } else {
                startScroll(scrollY, -scrollY, duration * scrollY / (rowHeigh * 5));
            }
        }
        if (STATE == CLOSE) {
            if (scrollY < rowHeigh * 5 - 100) {
                startScroll(scrollY, -scrollY, duration * scrollY / (rowHeigh * 5));
            } else {
                startScroll(scrollY, rowHeigh * 5 - scrollY, duration * (rowHeigh * 5 - scrollY) / (rowHeigh * 5));
            }
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //滑动时，禁止日历的滑动
        weekCalendar.setScrollEnable(false);
        monthCalendar.setScrollEnable(false);

        boolean hiddenMonthCalendar = dy > 0 && getScrollY() < rowHeigh * 5;
        boolean showMonthCalendar = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenMonthCalendar || showMonthCalendar) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= rowHeigh * 5) return false;
        fling((int) velocityY);
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = nestedScrollingChild.getLayoutParams();
        layoutParams.height = getMeasuredHeight() - rowHeigh;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nestedScrollingChild = getChildAt(1);
        if (!(nestedScrollingChild instanceof NestedScrollingChild)) {
            throw new RuntimeException("子view必须实现NestedScrollingChild");
        }
    }

    private void startScroll(int startY, int dy, int duration) {
        mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }


    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, rowHeigh * 5);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > rowHeigh * 5) {
            y = rowHeigh * 5;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        int scrollY = getScrollY();
        if (scrollY == 0) {
            STATE = OPEN;
            weekCalendar.setVisibility(INVISIBLE);
        } else if (scrollY == 5 * rowHeigh) {
            STATE = CLOSE;
            weekCalendar.setVisibility(VISIBLE);
        } else {
            DateTime selectDateTime = weekCalendar.getSelectDateTime();
            DateTime initialDateTime = weekCalendar.getInitialDateTime();
            DateTime dateTime = selectDateTime == null ? initialDateTime : selectDateTime;

            MonthView currentCalendarView = (MonthView) monthCalendar.getCurrentCalendarView();
            int weekRow = currentCalendarView.getWeekRow(dateTime);
            weekCalendar.setVisibility(scrollY >= weekRow * rowHeigh ? VISIBLE : INVISIBLE);
        }

        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public void onMonthCalendarPageSelected(DateTime dateTime) {
        if (STATE == OPEN) {

            DateTime selectDateTime = monthCalendar.getSelectDateTime();
            if (selectDateTime == null) {
                weekCalendar.jumpDate(dateTime, true);
            } else {
                weekCalendar.setDate(selectDateTime.getYear(), selectDateTime.getMonthOfYear(), selectDateTime.getDayOfMonth());
            }

            if (onClickCalendarListener != null) {
                onClickCalendarListener.onCalendarPageChanged(dateTime);
            }
        }
    }

    @Override
    public void onClickMonthCalendar(DateTime dateTime) {
        weekCalendar.setDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        if (onClickCalendarListener != null) {
            onClickCalendarListener.onClickCalendar(dateTime);
        }
    }

    @Override
    public void onClickWeekCalendar(DateTime dateTime) {
        monthCalendar.setDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        if (onClickCalendarListener != null) {
            onClickCalendarListener.onClickCalendar(dateTime);
        }
    }


    @Override
    public void onWeekCalendarPageSelected(DateTime dateTime) {
        if (STATE == CLOSE) {
            DateTime selectDateTime = weekCalendar.getSelectDateTime();
            if (selectDateTime == null) {
                monthCalendar.jumpDate(dateTime, true);
            } else {
                monthCalendar.setDate(selectDateTime.getYear(), selectDateTime.getMonthOfYear(), selectDateTime.getDayOfMonth());
            }

            if (onClickCalendarListener != null) {
                onClickCalendarListener.onCalendarPageChanged(dateTime);
            }
        }
    }


    public void setDate(int year, int month, int day) {
        monthCalendar.setDate(year, month, day);
        weekCalendar.setDate(year, month, day);
    }

    public void setPointList(List<String> pointList) {
        monthCalendar.setPointList(pointList);
        weekCalendar.setPointList(pointList);
    }


    private OnCalendarChangeListener onClickCalendarListener;

    public void setOnClickCalendarListener(OnCalendarChangeListener onClickCalendarListener) {
        this.onClickCalendarListener = onClickCalendarListener;
    }

    public void open() {
        if (STATE == CLOSE) {
            startScroll(rowHeigh * 5, -rowHeigh * 5, duration);
        }
    }

    public void close() {
        if (STATE == OPEN) {
            startScroll(0, rowHeigh * 5, duration);
        }
    }






   /* private WeekCalendar weekCalendar;
    private MonthCalendar monthCalendar;
    private View nestedScrollingChild;

    public static final int OPEN = 100;
    public static final int CLOSE = 200;
    private static int STATE = 100;//默认开
    private int rowHeigh;

    private int duration;

    private ValueAnimator monthValueAnimator;
    private ValueAnimator nestedScrollingChildValueAnimator;

    public MWCalendar(Context context) {
        this(context, null);
    }

    public MWCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MWCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(LinearLayout.VERTICAL);
        monthCalendar = new MonthCalendar(context, attrs);
        addView(monthCalendar);

        weekCalendar = new WeekCalendar(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        float dimension = ta.getDimension(R.styleable.NCalendar_calendarHeight, Utils.dp2px(context, 240));
        duration = ta.getInt(R.styleable.NCalendar_duration, 500);
        ta.recycle();

        rowHeigh = (int) (dimension / 6);
        MyLog.d("rowHeigh::" + rowHeigh);
        monthCalendar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeigh * 6));
        weekCalendar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, rowHeigh));

        monthCalendar.setOnMonthCalendarPageChangeListener(this);
        monthCalendar.setOnClickMonthCalendarListener(this);
        weekCalendar.setOnClickWeekCalendarListener(this);
        weekCalendar.setOnWeekCalendarPageChangeListener(this);

        monthValueAnimator = new ValueAnimator();
        nestedScrollingChildValueAnimator = new ValueAnimator();

        monthValueAnimator.addUpdateListener(this);
        nestedScrollingChildValueAnimator.addUpdateListener(this);
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //  return super.onNestedFling(target, velocityX, velocityY, consumed);
        return false;
    }

    @Override
    public void onStopNestedScroll(View target) {

        int monthCalendarTop = monthCalendar.getTop();
        int nestedScrollingChildTop = nestedScrollingChild.getTop();

        if (monthCalendarTop == 0 && nestedScrollingChildTop == rowHeigh * 6) {
            return;
        }

        if (monthCalendarTop == -rowHeigh * 3 && nestedScrollingChildTop == rowHeigh) {
            return;
        }

        if (STATE == OPEN) {
            if (Math.abs(monthCalendarTop) < rowHeigh) {
                autoOpen(monthCalendarTop, 0, nestedScrollingChildTop, rowHeigh * 6);
            } else {
                autoClose(monthCalendarTop, -rowHeigh * 3, nestedScrollingChildTop, rowHeigh);
            }
        } else {
            if (nestedScrollingChildTop < rowHeigh * 2) {
                autoClose(monthCalendarTop, -rowHeigh * 3, nestedScrollingChildTop, rowHeigh);
            } else {
                autoOpen(monthCalendarTop, 0, nestedScrollingChildTop, rowHeigh * 6);
            }
        }
    }


    //自动开
    private void autoOpen(int startMonth, int endMonth, int startChild, int endChild) {
        STATE = OPEN;
        monthValueAnimator.setIntValues(startMonth, endMonth);
        monthValueAnimator.setDuration(240);
        monthValueAnimator.start();

        nestedScrollingChildValueAnimator.setIntValues(startChild, endChild);
        nestedScrollingChildValueAnimator.setDuration(240);
        nestedScrollingChildValueAnimator.start();
    }

    //自动闭
    private void autoClose(int startMonth, int endMonth, int startChild, int endChild) {
        STATE = CLOSE;
        monthValueAnimator.setIntValues(startMonth, endMonth);
        monthValueAnimator.setDuration(240);
        monthValueAnimator.start();

        nestedScrollingChildValueAnimator.setIntValues(startChild, endChild);
        nestedScrollingChildValueAnimator.setDuration(240);
        nestedScrollingChildValueAnimator.start();
        STATE = CLOSE;
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        int monthTop = monthCalendar.getTop();
        int nestedScrollingChildTop = nestedScrollingChild.getTop();

        if (dy > 0 && Math.abs(monthTop) < 3 * rowHeigh) {
            int offset = getOffset(dy, 3 * rowHeigh - Math.abs(monthTop));
            monthCalendar.offsetTopAndBottom(-offset);
            nestedScrollingChild.offsetTopAndBottom(-offset);
            consumed[1] = dy;
        } else if (dy > 0 && nestedScrollingChildTop > rowHeigh) {
            int offset = getOffset(dy, nestedScrollingChildTop - rowHeigh);
            nestedScrollingChild.offsetTopAndBottom(-offset);
            consumed[1] = dy;
        } else if (dy < 0 && monthTop != 0 && !ViewCompat.canScrollVertically(target, -1)) {
            int offset = getOffset(Math.abs(dy), Math.abs(monthTop));
            monthCalendar.offsetTopAndBottom(offset);
            nestedScrollingChild.offsetTopAndBottom(offset);
            consumed[1] = dy;
        } else if (dy < 0 && monthTop == 0 && nestedScrollingChildTop != rowHeigh * 6 && !ViewCompat.canScrollVertically(target, -1)) {
            int offset = getOffset(Math.abs(dy), rowHeigh * 6 - nestedScrollingChildTop);
            nestedScrollingChild.offsetTopAndBottom(offset);
            consumed[1] = dy;
        }

        if (monthTop == -3 * rowHeigh && nestedScrollingChildTop == rowHeigh) {
            STATE = CLOSE;
        }

        if (monthTop == 0 && nestedScrollingChildTop == rowHeigh * 6) {
            STATE = OPEN;
        }

    }


    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //防止快速滑动
        int nestedScrollingChildTop = nestedScrollingChild.getTop();
        if (nestedScrollingChildTop > rowHeigh) {
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
        layoutParams.height = getMeasuredHeight() - rowHeigh;
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
    public void onMonthCalendarPageSelected(DateTime dateTime) {
        if (STATE == OPEN) {
            DateTime selectDateTime = monthCalendar.getSelectDateTime();
            if (selectDateTime == null) {
                weekCalendar.jumpDate(dateTime, true);
            } else {
                weekCalendar.setDate(selectDateTime.getYear(), selectDateTime.getMonthOfYear(), selectDateTime.getDayOfMonth());
            }

            if (onClickCalendarListener != null) {
                onClickCalendarListener.onCalendarPageChanged(dateTime);
            }
        }
    }

    @Override
    public void onClickMonthCalendar(DateTime dateTime) {
        weekCalendar.setDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        if (onClickCalendarListener != null) {
            onClickCalendarListener.onClickCalendar(dateTime);
        }
    }

    @Override
    public void onClickWeekCalendar(DateTime dateTime) {
        monthCalendar.setDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        if (onClickCalendarListener != null) {
            onClickCalendarListener.onClickCalendar(dateTime);
        }
    }


    @Override
    public void onWeekCalendarPageSelected(DateTime dateTime) {
        if (STATE == CLOSE) {
            DateTime selectDateTime = weekCalendar.getSelectDateTime();
            if (selectDateTime == null) {
                monthCalendar.jumpDate(dateTime, true);
            } else {
                monthCalendar.setDate(selectDateTime.getYear(), selectDateTime.getMonthOfYear(), selectDateTime.getDayOfMonth());
            }

            if (onClickCalendarListener != null) {
                onClickCalendarListener.onCalendarPageChanged(dateTime);
            }
        }
    }


    private OnCalendarChangeListener onClickCalendarListener;

    public void setOnClickCalendarListener(OnCalendarChangeListener onClickCalendarListener) {
        this.onClickCalendarListener = onClickCalendarListener;
    }

    public void open() {
        //autoOpen();
    }

    public void close() {
        autoClose(0, -rowHeigh * 3, rowHeigh * 6, rowHeigh);
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

    }*/
}
