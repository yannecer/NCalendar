package com.necer.calendar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.R;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.MultipleNumModel;
import com.necer.enumeration.SelectedModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnCalendarStateChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.listener.OnEndAnimatorListener;
import com.necer.listener.OnMWDateChangeListener;
import com.necer.painter.CalendarPainter;
import com.necer.painter.InnerPainter;
import com.necer.utils.Attrs;
import com.necer.utils.AttrsUtil;
import com.necer.utils.ViewException;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * Created by necer on 2018/11/12.
 */
public abstract class NCalendar extends FrameLayout implements IICalendar, NestedScrollingParent, ValueAnimator.AnimatorUpdateListener {

    protected WeekCalendar weekCalendar;
    protected MonthCalendar monthCalendar;

    protected int weekHeight;//周日历的高度
    protected int monthHeight;//月日历的高度,是日历整个的高

    protected CalendarState calendarState;//默认月
    private OnCalendarStateChangedListener onCalendarStateChangedListener;

    protected View childView;//NCalendar内部包含的直接子view，直接子view并不一定是NestScrillChild
    private View targetView;//实际滑动的view

    protected Rect monthRect;//月日历大小的矩形
    protected Rect weekRect;//周日历大小的矩形 ，用于判断点击事件是否在日历的范围内

    private boolean isWeekHold;//是否需要周状态定

    private CalendarPainter calendarPainter;
    private boolean isInflateFinish;//是否加载完成，

    protected ValueAnimator monthValueAnimator;//月日历动画
    protected ValueAnimator childViewValueAnimator;

    private Attrs attrs;

    public NCalendar(@NonNull Context context) {
        this(context, null);
    }

    public NCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NCalendar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setMotionEventSplittingEnabled(false);
        this.attrs = AttrsUtil.getAttrs(context, attrs);

        int duration = this.attrs.duration;
        monthHeight = this.attrs.monthCalendarHeight;
        calendarState = CalendarState.valueOf(this.attrs.defaultCalendar);
        weekHeight = monthHeight / 5;
        isWeekHold = this.attrs.isWeekHold;

        calendarPainter = new InnerPainter(this);
        weekCalendar = new WeekCalendar(context, this.attrs, calendarPainter);
        monthCalendar = new MonthCalendar(context, this.attrs, calendarPainter);

        monthCalendar.setOnMWDateChangeListener(onMWDateChangeListener);
        weekCalendar.setOnMWDateChangeListener(onMWDateChangeListener);

        addView(monthCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, monthHeight));
        addView(weekCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, weekHeight));

        monthValueAnimator = new ValueAnimator();
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.addUpdateListener(this);
        monthValueAnimator.addListener(onEndAnimatorListener);

        childViewValueAnimator = new ValueAnimator();
        childViewValueAnimator.setDuration(duration);
        childViewValueAnimator.addUpdateListener(this);
        childViewValueAnimator.addListener(onEndAnimatorListener);
    }


    private OnMWDateChangeListener onMWDateChangeListener = new OnMWDateChangeListener() {
        @Override
        public void onMwDateChange(BaseCalendar baseCalendar, final LocalDate localDate, List<LocalDate> dateList) {
            if (baseCalendar == monthCalendar && calendarState == CalendarState.MONTH) {
                //交换数据，保证月周选中数据同步
                weekCalendar.exchangeSelectDateList(dateList);
                //月日历变化,改变周的选中
                weekCalendar.jump(localDate, false);

            } else if (baseCalendar == weekCalendar && calendarState == CalendarState.WEEK) {
                //交换数据，保证月周选中数据同步
                monthCalendar.exchangeSelectDateList(dateList);
                //周日历变化，改变月的选中
                monthCalendar.jump(localDate, false);

                monthCalendar.post(new Runnable() {
                    @Override
                    public void run() {
                        //此时需要根据月日历的选中日期调整值
                        // 跳转翻页需要时间，需要等待完成之后再调整，如果不这样直接获取位置信息，会出现老的数据，不能获取正确的数据
                        monthCalendar.setY(getMonthYOnWeekState(localDate));
                    }
                });
            }
        }
    };


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 3) {
            throw new RuntimeException("NCalendar中的有且只能有一个直接子view");
        }

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) != monthCalendar && getChildAt(i) != weekCalendar) {
                childView = getChildAt(i);
                Drawable background = childView.getBackground();
                if (background == null) {
                    childView.setBackgroundColor(Color.WHITE);
                }
            }
        }
        targetView = findViewWithTag(getResources().getString(R.string.factual_scroll_view));
        if (targetView == null) {
            try {
                ViewException.traverseView(childView);
            } catch (ViewException e) {
                e.printStackTrace();
                targetView = e.getExceptionView();
            }
        }
        if (targetView == null) {
            throw new RuntimeException("NCalendar中需要实现了NestedScrollingChild的子类");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams childLayoutLayoutParams = childView.getLayoutParams();
        childLayoutLayoutParams.height = getMeasuredHeight() - weekHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b); //调用父类的该方法会造成 快速滑动月日历同时快速上滑recyclerview造成月日历的残影

        int measuredWidth = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        weekCalendar.layout(0 + paddingLeft, 0, measuredWidth - paddingRight, weekHeight);
        monthCalendar.layout(0 + paddingLeft, 0, measuredWidth - paddingRight, monthHeight);
        childView.layout(0 + paddingLeft, monthHeight, measuredWidth - paddingRight, childView.getMeasuredHeight() + monthHeight);

    }


    //自动滑动到适当的位置
    private void autoScroll() {
        float childLayoutY = childView.getY();
        if (calendarState == CalendarState.MONTH && monthHeight - childLayoutY < weekHeight) {
            autoToMonthState();
        } else if (calendarState == CalendarState.MONTH && monthHeight - childLayoutY >= weekHeight) {
            autoToWeekState();
        } else if (calendarState == CalendarState.WEEK && childLayoutY < weekHeight * 2) {
            autoToWeekState();
        } else if (calendarState == CalendarState.WEEK && childLayoutY >= weekHeight * 2) {
            autoToMonthState();
        }
    }


    //自动滑动到周
    private void autoToWeekState() {
        float monthCalendarStart = monthCalendar.getY();//起始位置
        float monthCalendarEnd = getMonthCalendarAutoWeekEndY();

        monthValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);
        monthValueAnimator.start();

        float childViewStart = childView.getY();
        float childViewEnd = weekHeight;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();

    }

    //自动滑动到月
    private void autoToMonthState() {
        float monthCalendarStart = monthCalendar.getY();//起始位置
        float monthCalendarEnd = 0;
        monthValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);
        monthValueAnimator.start();

        float childViewStart = childView.getY();
        float childViewEnd = monthHeight;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        //super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //跟随手势滑动
        gestureMove(dy, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //只有都在都在周状态下，才允许子View Fling滑动
        return !(isChildWeekState() && isMonthCalendarWeekState());
    }

    @Override
    public void onStopNestedScroll(View target) {
        //该方法手指抬起的时候回调，此时根据此刻的位置，自动滑动到相应的状态，
        //如果已经在对应的位置上，则不执行动画，
        if (isMonthCalendarMonthState() && isChildMonthState() && calendarState == CalendarState.WEEK) {
            setCalenadrState();
        } else if (isMonthCalendarWeekState() && isChildWeekState() && calendarState == CalendarState.MONTH) {
            setCalenadrState();
        } else if (!isChildMonthState() && !isChildWeekState()) {
            //不是周状态也不是月状态时，自动滑动
            autoScroll();
        }
    }

    /**
     * 手势滑动的逻辑，做了简单处理，2种状态，都以ChildLayout滑动的状态判断
     * 1、向上滑动未到周状态
     * 2、向下滑动未到月状态
     */
    protected void gestureMove(int dy, int[] consumed) {

        float monthCalendarY = monthCalendar.getY();
        float childLayoutY = childView.getY();

        if (dy > 0 && !isChildWeekState()) {
            monthCalendar.setY(-getGestureMonthUpOffset(dy) + monthCalendarY);
            childView.setY(-getGestureChildUpOffset(dy) + childLayoutY);
            if (consumed != null) consumed[1] = dy;
        } else if (dy < 0 && isWeekHold && isChildWeekState()) {
            //不操作，

        } else if (dy < 0 && !isChildMonthState() && !targetView.canScrollVertically(-1)) {
            monthCalendar.setY(getGestureMonthDownOffset(dy) + monthCalendarY);
            childView.setY(getGestureChildDownOffset(dy) + childLayoutY);
            if (consumed != null) consumed[1] = dy;
        }

        setWeekVisible(dy > 0);
    }


    private int dowmY;
    private int downX;
    private int lastY;//上次的y
    private int verticalY = 50;//竖直方向上滑动的临界值，大于这个值认为是竖直滑动
    private boolean isFirstScroll = true; //第一次手势滑动，因为第一次滑动的偏移量大于verticalY，会出现猛的一划，这里只对第一次滑动做处理

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isInflateFinish) {
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
                gestureMove(dy, null);
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


    //点击事件是否在日历的范围内
    private boolean isInCalendar(int x, int y) {
        if (calendarState == CalendarState.MONTH) {
            return monthRect.contains(x, y);
        } else {
            return weekRect.contains(x, y);
        }
    }


    @Override
    public void jumpDate(String formatDate) {
        if (calendarState == CalendarState.MONTH) {
            monthCalendar.jumpDate(formatDate);
        } else {
            weekCalendar.jumpDate(formatDate);
        }
    }

    @Override
    public void setInitializeDate(String formatDate) {
        monthCalendar.setInitializeDate(formatDate);
        weekCalendar.setInitializeDate(formatDate);
    }


    @Override
    public void toToday() {
        if (calendarState == CalendarState.MONTH) {
            monthCalendar.toToday();
        } else {
            weekCalendar.toToday();
        }
    }

    @Override
    public void toWeek() {
        if (calendarState == CalendarState.MONTH) {
            autoToWeekState();
        }
    }

    @Override
    public void toMonth() {
        if (calendarState == CalendarState.WEEK) {
            autoToMonthState();
        }
    }

    @Override
    public CalendarPainter getCalendarPainter() {
        return calendarPainter;
    }

    @Override
    public CalendarState getCalendarState() {
        return calendarState;
    }

    @Override
    public void setCalendarState(CalendarState calendarState) {
        this.calendarState = calendarState;
    }

    @Override
    public void toNextPager() {
        if (calendarState == CalendarState.MONTH) {
            monthCalendar.toNextPager();
        } else if (calendarState == CalendarState.WEEK) {
            weekCalendar.toNextPager();
        }
    }

    @Override
    public void toLastPager() {
        if (calendarState == CalendarState.MONTH) {
            monthCalendar.toLastPager();
        } else if (calendarState == CalendarState.WEEK) {
            weekCalendar.toLastPager();
        }
    }

    @Override
    public void setSelectedMode(SelectedModel selectedMode) {
        monthCalendar.setSelectedMode(selectedMode);
        weekCalendar.setSelectedMode(selectedMode);
    }

    @Override
    public void setDefaultSelectFitst(boolean isDefaultSelectFitst) {
        monthCalendar.setDefaultSelectFitst(isDefaultSelectFitst);
        weekCalendar.setDefaultSelectFitst(isDefaultSelectFitst);
    }

    @Override
    public void setDateInterval(String startFormatDate, String endFormatDate) {
        monthCalendar.setDateInterval(startFormatDate, endFormatDate);
        weekCalendar.setDateInterval(startFormatDate, endFormatDate);
    }

    @Override
    public void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener) {
        monthCalendar.setOnCalendarChangedListener(onCalendarChangedListener);
        weekCalendar.setOnCalendarChangedListener(onCalendarChangedListener);
    }


    @Override
    public void setOnCalendarMultipleChangedListener(OnCalendarMultipleChangedListener onCalendarMultipleChangedListener) {
        monthCalendar.setOnCalendarMultipleChangedListener(onCalendarMultipleChangedListener);
        weekCalendar.setOnCalendarMultipleChangedListener(onCalendarMultipleChangedListener);
    }

    @Override
    public void setOnCalendarStateChangedListener(OnCalendarStateChangedListener onCalendarStateChangedListener) {
        this.onCalendarStateChangedListener = onCalendarStateChangedListener;
    }

    @Override
    public void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener) {
        monthCalendar.setOnClickDisableDateListener(onClickDisableDateListener);
        weekCalendar.setOnClickDisableDateListener(onClickDisableDateListener);
    }

    @Override
    public void setCalendarPainter(CalendarPainter calendarPainter) {
        this.calendarPainter = calendarPainter;
        monthCalendar.setCalendarPainter(calendarPainter);
        weekCalendar.setCalendarPainter(calendarPainter);
    }

    @Override
    public void notifyCalendar() {
        monthCalendar.notifyCalendar();
        weekCalendar.notifyCalendar();
    }

    @Override
    public void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate) {
        monthCalendar.setDateInterval(startFormatDate, endFormatDate, formatInitializeDate);
        weekCalendar.setDateInterval(startFormatDate, endFormatDate, formatInitializeDate);
    }

    @Override
    public void setMultipleNum(int multipleNum, MultipleNumModel multipleNumModel) {
        monthCalendar.setMultipleNum(multipleNum, multipleNumModel);
        weekCalendar.setMultipleNum(multipleNum, multipleNumModel);
    }

    @Override
    public Attrs getAttrs() {
        return attrs;
    }

    @Override
    public List<LocalDate> getAllSelectDateList() {
        if (calendarState == CalendarState.WEEK) {
            return weekCalendar.getAllSelectDateList();
        } else if (calendarState == CalendarState.MONTH) {
            return monthCalendar.getAllSelectDateList();
        }
        return null;
    }

    @Override
    public List<LocalDate> getCurrectSelectDateList() {
        if (calendarState == CalendarState.WEEK) {
            return weekCalendar.getCurrectSelectDateList();
        } else if (calendarState == CalendarState.MONTH) {
            return monthCalendar.getCurrectSelectDateList();
        }
        return null;
    }

    @Override
    public List<LocalDate> getCurrectDateList() {
        if (calendarState == CalendarState.WEEK) {
            return weekCalendar.getCurrectDateList();
        } else if (calendarState == CalendarState.MONTH) {
            return monthCalendar.getCurrectDateList();
        }
        return null;
    }

    //修复id重复
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(null);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        if (animation == monthValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            float top = monthCalendar.getY();
            float i = animatedValue - top;
            float y = monthCalendar.getY();
            monthCalendar.setY(i + y);

        } else if (animation == childViewValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            float top = childView.getY();
            float i = animatedValue - top;
            float y = childView.getY();
            childView.setY(i + y);

            setWeekVisible(-i > 0);

        }
    }

    private OnEndAnimatorListener onEndAnimatorListener = new OnEndAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (animation == childViewValueAnimator) {
                setCalenadrState();
            }
        }
    };

    //设置日历的状态、月周的显示及状态回调
    private void setCalenadrState() {
        if (isMonthCalendarWeekState() && isChildWeekState() && calendarState == CalendarState.MONTH) {
            calendarState = CalendarState.WEEK;
            weekCalendar.setVisibility(VISIBLE);
            monthCalendar.setVisibility(INVISIBLE);

            if (onCalendarStateChangedListener != null) {
                onCalendarStateChangedListener.onCalendarStateChange(calendarState);
            }
        } else if (isMonthCalendarMonthState() && isChildMonthState() && calendarState == CalendarState.WEEK) {
            calendarState = CalendarState.MONTH;
            weekCalendar.setVisibility(INVISIBLE);
            monthCalendar.setVisibility(VISIBLE);

            //重新设置周的显示 展开月日历上面可能有选中的日期，需要下次折叠时周日历的显示要正确
            LocalDate pivot = monthCalendar.getPivotDate();
            weekCalendar.jump(pivot, false);

            if (onCalendarStateChangedListener != null) {
                onCalendarStateChangedListener.onCalendarStateChange(calendarState);
            }
        }
    }

    //childView周状态的条件
    protected boolean isChildWeekState() {
        return childView.getY() <= weekHeight;
    }

    //childView月状态的条件
    protected boolean isChildMonthState() {
        return childView.getY() >= monthHeight;
    }

    //月日历周状态
    protected boolean isMonthCalendarWeekState() {
        return monthCalendar.getY() <= -monthCalendar.getPivotDistanceFromTop();
    }

    //月日历月状态
    protected boolean isMonthCalendarMonthState() {
        return monthCalendar.getY() >= 0;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!isInflateFinish) {
            monthCalendar.setVisibility(calendarState == CalendarState.MONTH ? VISIBLE : INVISIBLE);
            weekCalendar.setVisibility(calendarState == CalendarState.MONTH ? INVISIBLE : VISIBLE);
            monthRect = new Rect(0, 0, monthCalendar.getMeasuredWidth(), monthCalendar.getMeasuredHeight());
            weekRect = new Rect(0, 0, weekCalendar.getMeasuredWidth(), weekCalendar.getMeasuredHeight());
            monthCalendar.setY(calendarState == CalendarState.MONTH ? 0 : getMonthYOnWeekState(weekCalendar.getFirstDate()));
            childView.setY(calendarState == CalendarState.MONTH ? monthHeight : weekHeight);
            isInflateFinish = true;
        }
    }

    //获取月日历自动到周状态的y值
    protected abstract float getMonthCalendarAutoWeekEndY();

    //周状态下 月日历的getY 是个负值,用于在 周状态下日期改变设置正确的y值
    protected abstract float getMonthYOnWeekState(LocalDate localDate);

    //滑动过界处理 ，如果大于最大距离就返回最大距离
    protected float getOffset(float offset, float maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        }
        return offset;
    }

    /**
     * 设置weekCalendar的显示隐藏，该方法会在手势滑动和自动滑动的的时候一直回调
     * isUp 是否是向上滑动
     */
    protected abstract void setWeekVisible(boolean isUp);

    /**
     * 月日历根据手势向上移动的距离
     *
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return 根据不同日历的交互，计算不同的滑动值
     */
    protected abstract float getGestureMonthUpOffset(int dy);

    /**
     * Child根据手势向上移动的距离
     *
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return 根据不同日历的交互，计算不同的滑动值
     */
    protected abstract float getGestureChildUpOffset(int dy);

    /**
     * 月日历根据手势向下移动的距离
     *
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return 根据不同日历的交互，计算不同的滑动值
     */
    protected abstract float getGestureMonthDownOffset(int dy);

    /**
     * Child根据手势向下移动的距离
     *
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return 根据不同日历的交互，计算不同的滑动值
     */
    protected abstract float getGestureChildDownOffset(int dy);


}
