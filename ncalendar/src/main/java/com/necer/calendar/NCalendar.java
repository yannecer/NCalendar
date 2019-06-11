package com.necer.calendar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Parcelable;
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
import com.necer.listener.OnClickDisableDateListener;
import com.necer.listener.OnEndAnimatorListener;
import com.necer.listener.OnMWDateChangeListener;
import com.necer.painter.CalendarPainter;
import com.necer.painter.InnerPainter;
import com.necer.utils.Attrs;
import com.necer.utils.AttrsUtil;
import com.necer.view.ChildLayout;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * Created by necer on 2018/11/12.
 */
public abstract class NCalendar extends FrameLayout implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener {


    protected WeekCalendar weekCalendar;
    protected MonthCalendar monthCalendar;

    protected int weekHeight;//周日历的高度
    protected int monthHeight;//月日历的高度,是日历整个的高

    protected int childLayoutLayoutTop;//onLayout中，定位的高度，

    protected int STATE;//默认月
    private int lastSate;//防止状态监听重复回调
    private OnCalendarChangedListener onCalendarChangedListener;

    protected ChildLayout childLayout;//NCalendar内部包含的直接子view，直接子view并不一定是NestScrillChild

    protected Rect monthRect;//月日历大小的矩形
    protected Rect weekRect;//周日历大小的矩形 ，用于判断点击事件是否在日历的范围内

    private boolean isWeekHold;//是否需要周状态定住

    private CalendarPainter calendarPainter;
    private boolean isInflateFinish;//是否加载完成，


    protected ValueAnimator monthValueAnimator;//月日历动画
    protected ValueAnimator childLayoutValueAnimator;

    public NCalendar(@NonNull Context context) {
        this(context, null);
    }

    public NCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NCalendar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setMotionEventSplittingEnabled(false);
        Attrs attrss = AttrsUtil.getAttrs(context, attrs);

        int duration = attrss.duration;
        monthHeight = attrss.monthCalendarHeight;
        STATE = attrss.defaultCalendar;
        weekHeight = monthHeight / 5;
        isWeekHold = attrss.isWeekHold;

        calendarPainter = new InnerPainter(attrss);
        weekCalendar = new WeekCalendar(context, attrss, calendarPainter);
        monthCalendar = new MonthCalendar(context, attrss, calendarPainter);
        childLayout = new ChildLayout(getContext(), attrs, monthHeight);

        monthCalendar.setOnDateChangeListener(onMWDateChangeListener);
        weekCalendar.setOnDateChangeListener(onMWDateChangeListener);
        childLayout.setBackgroundColor(attrss.bgChildColor);

        childLayoutLayoutTop = STATE == Attrs.WEEK ? weekHeight : monthHeight;

        post(new Runnable() {
            @Override
            public void run() {

                monthRect = new Rect(0, 0, monthCalendar.getWidth(), monthCalendar.getHeight());
                weekRect = new Rect(0, 0, weekCalendar.getWidth(), weekCalendar.getHeight());

                monthCalendar.setY(STATE == Attrs.MONTH ? 0 : getMonthYOnWeekState(new LocalDate()));
                childLayout.setY(STATE == Attrs.MONTH ? monthHeight : weekHeight);

                setCalenadrState();

                isInflateFinish = true;

            }
        });


        monthValueAnimator = new ValueAnimator();
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.addUpdateListener(this);
        monthValueAnimator.addListener(onEndAnimatorListener);

        childLayoutValueAnimator = new ValueAnimator();
        childLayoutValueAnimator.setDuration(duration);
        childLayoutValueAnimator.addUpdateListener(this);
        childLayoutValueAnimator.addListener(onEndAnimatorListener);

    }


    private OnMWDateChangeListener onMWDateChangeListener = new OnMWDateChangeListener() {
        @Override
        public void onMwDateChange(BaseCalendar baseCalendar, final LocalDate localDate, List<LocalDate> dateList) {
            if (baseCalendar == monthCalendar && STATE == Attrs.MONTH) {
                //月日历变化,改变周的选中
                weekCalendar.jump(localDate, false);
                weekCalendar.setSelectDateList(dateList);

                MyLog.d("月状态：：：：" + localDate);

            } else if (baseCalendar == weekCalendar && STATE == Attrs.WEEK) {
                //周日历变化，改变月的选中
                monthCalendar.jump(localDate, false);

                MyLog.d("周状态：：：：" + localDate);
                monthCalendar.setSelectDateList(dateList);

                monthCalendar.post(new Runnable() {
                    @Override
                    public void run() {
                        //此时需要根据月日历的选中日期调整值
                        // post是因为在前面得到当前view是再post中完成，如果不这样直接获取位置信息，会出现老的数据，不能获取正确的数据
                        monthCalendar.setY(getMonthYOnWeekState(localDate));

                    }
                });
            }
        }
    };


    /**
     * xml文件加载结束，添加月，周日历和child到NCalendar中
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 1) {
            throw new RuntimeException("NCalendar中的只能有一个直接子view");
        }
        childLayout.addView(getChildAt(0), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        addView(monthCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, monthHeight));
        addView(weekCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, weekHeight));
        addView(childLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams childLayoutLayoutParams = childLayout.getLayoutParams();
        childLayoutLayoutParams.height = getMeasuredHeight() - weekHeight;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b); //调用父类的该方法会造成 快速滑动月日历同时快速上滑recyclerview造成月日历的残影

        int measuredWidth = getMeasuredWidth();
        weekCalendar.layout(0, 0, measuredWidth, weekHeight);
        monthCalendar.layout(0, 0, measuredWidth, monthHeight);
        childLayout.layout(0, childLayoutLayoutTop, measuredWidth, childLayout.getMeasuredHeight() + childLayoutLayoutTop);
    }


    /**
     * 自动滑动到适当的位置
     */
    private void autoScroll() {

        float childLayoutY = childLayout.getY();

        if (STATE == Attrs.MONTH && monthHeight - childLayoutY < weekHeight) {
            onAutoToMonthState2();
        } else if (STATE == Attrs.MONTH && monthHeight - childLayoutY >= weekHeight) {
            onAutoToWeekState2();
        } else if (STATE == Attrs.WEEK && childLayoutY < weekHeight * 2) {
            onAutoToWeekState2();
        } else if (STATE == Attrs.WEEK && childLayoutY >= weekHeight * 2) {
            onAutoToMonthState2();
        }
    }


    private void onAutoToWeekState2() {

        float top = monthCalendar.getY();//起始位置
        int end;
        if (STATE == Attrs.MONTH) {
            //月  月日历有选中则选中为 中心点，如果没有选中则第一行
            end = -monthCalendar.getMonthCalendarOffset(); //结束位置
        } else {
            //周的情况，按照周的第一个数据为中心点
            end = -monthCalendar.getMonthCalendarOffset(weekCalendar.getFirstDate());
        }
        monthValueAnimator.setFloatValues(top, end);
        monthValueAnimator.start();

        float start = childLayout.getY();
        int end1 = weekHeight;
        childLayoutValueAnimator.setFloatValues(start, end1);
        childLayoutValueAnimator.start();

    }

    private void onAutoToMonthState2() {
        float top = monthCalendar.getY();//起始位置
        int end = 0;
        monthValueAnimator.setFloatValues(top, end);
        monthValueAnimator.start();


        float start = childLayout.getY();
        int end1 = monthHeight;
        childLayoutValueAnimator.setFloatValues(start, end1);
        childLayoutValueAnimator.start();
    }


//
//    @Override
//    public void onMonthSelect(NDate date) {
//        if (STATE == Attrs.MONTH) {
//            //月日历变化,改变周的选中
//            weekCalendar.jumpDate(date.localDate, true);
//            if (onCalendarChangedListener != null) {
//                onCalendarChangedListener.onCalendarDateChanged(date);
//            }
//        }
//    }
//
//    @Override
//    public void onWeekSelect(NDate date) {
//        if (STATE == Attrs.WEEK) {
//            //周日历变化，改变月的选中
//            monthCalendar.jumpDate(date.localDate, true);
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    //此时需要根据月日历的选中日期调整Y值
//                    // post是因为在前面得到当前view是再post中完成，如果不这样直接获取位置信息，会出现老的数据，不能获取正确的数据
//                    monthCalendar.setY(getMonthYOnWeekState());
//                }
//            });
//            if (onCalendarChangedListener != null) {
//                onCalendarChangedListener.onCalendarDateChanged(date);
//            }
//        }
//    }


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
        return !(childLayout.isWeekState() && monthCalendar.isWeekState());
    }

    @Override
    public void onStopNestedScroll(View target) {
        //该方法手指抬起的时候回调，此时根据此刻的位置，自动滑动到相应的状态，
        //如果已经在对应的位置上，则不执行动画，
        if (monthCalendar.isMonthState() && childLayout.isMonthState() && STATE == Attrs.WEEK) {
            setCalenadrState();
        } else if (monthCalendar.isWeekState() && childLayout.isWeekState() && STATE == Attrs.MONTH) {
            setCalenadrState();
        } else if (!childLayout.isMonthState() && !childLayout.isWeekState()) {
            //不是周状态也不是月状态时，自动滑动
            autoScroll();
        }
    }

    /**
     * 手势滑动的逻辑，做了简单处理，2种状态，都以ChildLayout滑动的状态判断
     * 1、向上滑动未到周状态
     * 2、向下滑动未到月状态
     *
     * @param dy
     * @param consumed
     */
    protected void gestureMove(int dy, int[] consumed) {

        float monthCalendarY = monthCalendar.getY();
        float childLayoutY = childLayout.getY();

        if (dy > 0 && !childLayout.isWeekState()) {
            monthCalendar.setY(-getGestureMonthUpOffset(dy) + monthCalendarY);
            childLayout.setY(-getGestureChildUpOffset(dy) + childLayoutY);
            if (consumed != null) consumed[1] = dy;
        } else if (dy < 0 && isWeekHold && childLayout.isWeekState()) {
            //不操作，

        } else if (dy < 0 && !childLayout.isMonthState() && !childLayout.canScrollVertically(-1)) {
            monthCalendar.setY(getGestureMonthDownOffset(dy) + monthCalendarY);
            childLayout.setY(getGestureChildDownOffset(dy) + childLayoutY);
            if (consumed != null) consumed[1] = dy;
        }

        onSetWeekVisible(dy);
    }


    private int dowmY;
    private int downX;
    private int lastY;//上次的y
    private int verticalY = 50;//竖直方向上滑动的临界值，大于这个值认为是竖直滑动
    private boolean isFirstScroll = true; //第一次手势滑动，因为第一次滑动的偏移量大于verticalY，会出现猛的一划，这里只对第一次滑动做处理

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isInflateFinish) {
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


    /**
     * 滑动过界处理 ，如果大于最大距离就返回最大距离
     *
     * @param offset    当前滑动的距离
     * @param maxOffset 当前滑动的最大距离
     * @return
     */
    protected float getOffset(float offset, float maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        }
        return offset;
    }

    /**
     * 自动回到月的状态 包括月日历和chilayout
     */
    protected abstract void onAutoToMonthState();

    /**
     * 自动回到周的状态 包括月日历和chilayout
     */
    protected abstract void onAutoToWeekState();

    /**
     * 设置weekCalendar的显示隐藏，该方法会在手势滑动和自动滑动的的时候一直回调
     */
    protected abstract void onSetWeekVisible(int dy);

    /**
     * 周状态下 月日历的getY 是个负值
     * 用于在 周状态下日期改变设置正确的y值
     *
     * @return
     */
    protected abstract float getMonthYOnWeekState(LocalDate localDate);


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


    /**
     * 跳转日期
     *
     * @param formatDate
     */
    public void jumpDate(String formatDate) {
        if (STATE == Attrs.MONTH) {
            monthCalendar.jumpDate(formatDate);
        } else {
            weekCalendar.jumpDate(formatDate);
        }
    }

    /**
     * 日历初始化的日期
     *
     * @param formatDate
     */
    public void setInitializeDate(String formatDate) {
        monthCalendar.setInitializeDate(formatDate);
        weekCalendar.setInitializeDate(formatDate);
    }


    /**
     * 回到今天
     */
    public void toToday() {
        if (STATE == Attrs.MONTH) {
            monthCalendar.toToday();
        } else {
            weekCalendar.toToday();
        }
    }

    /**
     * 自动滑动到周视图
     */
    public void toWeek() {
        if (STATE == Attrs.MONTH) {
            onAutoToWeekState();
        }
    }

    /**
     * 自动滑动到月视图
     */
    public void toMonth() {
        if (STATE == Attrs.WEEK) {
            onAutoToMonthState2();
        }
    }

    /**
     * 获取绘制CalendarPainter 强转，设置其他属性
     *
     * @param
     */
    public CalendarPainter getCalendarPainter() {
        return calendarPainter;
    }

    /**
     * 获取当前日历的状态
     * Attrs.MONTH==月视图    Attrs.WEEK==周视图
     *
     * @return
     */
    public int getState() {
        return STATE;
    }


    /**
     * 下一页
     */
    public void toNextPager() {
        if (STATE == Attrs.MONTH) {
            monthCalendar.toNextPager();
        } else {
            weekCalendar.toNextPager();
        }
    }

    /**
     * 上一页
     */
    public void toLastPager() {
        if (STATE == Attrs.MONTH) {
            monthCalendar.toLastPager();
        } else {
            weekCalendar.toLastPager();
        }
    }

    /**
     * 设置日期区间
     *
     * @param startFormatDate
     * @param endFormatDate
     */
    public void setDateInterval(String startFormatDate, String endFormatDate) {
        monthCalendar.setDateInterval(startFormatDate, endFormatDate);
        weekCalendar.setDateInterval(startFormatDate, endFormatDate);
    }

    /**
     * 日期、状态回调
     *
     * @param onCalendarChangedListener
     */
    public void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener) {
        this.onCalendarChangedListener = onCalendarChangedListener;
    }

    /**
     * 点击不可用的日期回调
     *
     * @param onClickDisableDateListener
     */
    public void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener) {
        monthCalendar.setOnClickDisableDateListener(onClickDisableDateListener);
        weekCalendar.setOnClickDisableDateListener(onClickDisableDateListener);
    }

    //设置绘制类
    public void setCalendarPainter(CalendarPainter calendarPainter) {
        this.calendarPainter = calendarPainter;
        monthCalendar.setCalendarPainter(calendarPainter);
        weekCalendar.setCalendarPainter(calendarPainter);
    }

    //刷新页面
    public void notifyAllView() {
        monthCalendar.notifyAllView();
        weekCalendar.notifyAllView();
    }


    //id重复
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(null);
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        onSetWeekVisible(0);

        if (animation == monthValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            float top = monthCalendar.getY();
            float i = animatedValue - top;
            float y = monthCalendar.getY();

            monthCalendar.setY(i + y);
        } else if (animation == childLayoutValueAnimator) {

            float animatedValue = (float) animation.getAnimatedValue();
            float top = childLayout.getY();
            float i = animatedValue - top;
            float y = childLayout.getY();
            childLayout.setY(i + y);

        }


    }


    private OnEndAnimatorListener onEndAnimatorListener = new OnEndAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (animation == monthValueAnimator) {

            } else if (animation == childLayoutValueAnimator) {
                setCalenadrState();
            }
        }
    };

    private void setCalenadrState() {
        if (monthCalendar.isWeekState() && childLayout.isWeekState()) {
            STATE = Attrs.WEEK;
            weekCalendar.setVisibility(VISIBLE);
            monthCalendar.setVisibility(INVISIBLE);
        } else if (monthCalendar.isMonthState() && childLayout.isMonthState()) {
            STATE = Attrs.MONTH;
            weekCalendar.setVisibility(INVISIBLE);
            monthCalendar.setVisibility(VISIBLE);

            //重新设置周的显示
            LocalDate pivot = monthCalendar.getPivotDate();
            weekCalendar.jump(pivot, false);

        }
    }

}
