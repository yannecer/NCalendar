package com.necer.calendar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;

import android.util.AttributeSet;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.R;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.enumeration.MultipleCountModel;
import com.necer.enumeration.CheckModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnCalendarScrollingListener;
import com.necer.listener.OnCalendarStateChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.listener.OnEndAnimatorListener;
import com.necer.listener.OnMWDateChangeListener;
import com.necer.painter.CalendarAdapter;
import com.necer.painter.CalendarBackground;
import com.necer.painter.CalendarPainter;
import com.necer.painter.InnerPainter;
import com.necer.painter.NumBackground;
import com.necer.painter.WhiteBackground;
import com.necer.utils.Attrs;
import com.necer.utils.AttrsUtil;
import com.necer.utils.ViewUtil;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * Created by necer on 2018/11/12.
 */
public abstract class NCalendar extends FrameLayout implements IICalendar, NestedScrollingParent, ValueAnimator.AnimatorUpdateListener {

    protected WeekCalendar weekCalendar;
    protected MonthCalendar monthCalendar;

    //周日历的高度
    protected int weekHeight;
    //月日历的高度,是日历整个的高
    protected int monthHeight;
    //月日历拉伸的高度
    protected int stretchMonthHeight;
    //日历状态，默认月
    protected CalendarState calendarState;
    private OnCalendarStateChangedListener onCalendarStateChangedListener;
    private OnCalendarScrollingListener onCalendarScrollingListener;

    protected View childView;//NCalendar内部包含的直接子view，直接子view并不一定是NestScrillChild
    private View targetView;//实际滑动的view 当targetView==null时，子view没有NestScrillChild或者NestScrillChild不可见，此时滑动交给onTochEvent处理

    protected RectF monthRect;//月日历大小的矩形
    protected RectF weekRect;//周日历大小的矩形 ，用于判断点击事件是否在日历的范围内
    protected RectF stretchMonthRect;

    private boolean isWeekHoldEnable;//是否需要周状态定
    private boolean isStretchCalendarEnable;//月日历是否可拉伸

    private boolean isInflateFinish;//是否加载完成，

    protected ValueAnimator monthValueAnimator;//月日历动画
    protected ValueAnimator monthStretchValueAnimator;//月日历动画
    protected ValueAnimator childViewValueAnimator;

    private final Attrs attrs;

    public NCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NCalendar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setMotionEventSplittingEnabled(false);
        this.attrs = AttrsUtil.getAttrs(context, attrs);

        int duration = this.attrs.animationDuration;
        monthHeight = this.attrs.calendarHeight;
        isStretchCalendarEnable = this.attrs.stretchCalendarEnable;
        stretchMonthHeight = this.attrs.stretchCalendarHeight;

        if (monthHeight >= stretchMonthHeight) {
            throw new RuntimeException(getContext().getString(R.string.N_stretch_month_height));
        }

        calendarState = CalendarState.valueOf(this.attrs.defaultCalendar);
        weekHeight = monthHeight / 5;

        monthCalendar = new MonthCalendar(context, attrs);
        weekCalendar = new WeekCalendar(context, attrs);

        monthCalendar.setId(R.id.N_monthCalendar);
        weekCalendar.setId(R.id.N_weekCalendar);

        setCalendarPainter(new InnerPainter(getContext(), this));

        OnMWDateChangeListener onMWDateChangeListener = (baseCalendar, localDate, totalCheckedDateList) -> {
            int childViewY = (int) childView.getY();
            if (baseCalendar == monthCalendar && (childViewY == monthHeight || childViewY == stretchMonthHeight)) {
                //交换数据，保证月周选中数据同步
                weekCalendar.exchangeCheckedDateList(totalCheckedDateList);
                //月日历变化,改变周的选中
                weekCalendar.jump(localDate, getCheckModel() == CheckModel.SINGLE_DEFAULT_CHECKED, DateChangeBehavior.API);

            } else if (baseCalendar == weekCalendar && childViewY == weekHeight) {
                //交换数据，保证月周选中数据同步
                monthCalendar.exchangeCheckedDateList(totalCheckedDateList);
                //周日历变化，改变月的选中
                monthCalendar.jump(localDate, getCheckModel() == CheckModel.SINGLE_DEFAULT_CHECKED, DateChangeBehavior.API);
                monthCalendar.post(() -> {
                    //此时需要根据月日历的选中日期调整值
                    // 跳转翻页需要时间，需要等待完成之后再调整，如果不这样直接获取位置信息，会出现老的数据，不能获取正确的数据
                    monthCalendar.setY(getMonthYOnWeekState(localDate));
                });
            }
        };
        monthCalendar.setOnMWDateChangeListener(onMWDateChangeListener);
        weekCalendar.setOnMWDateChangeListener(onMWDateChangeListener);

        //背景颜色
        CalendarBackground monthBackground;
        if (this.attrs.showNumberBackground) {
            monthBackground = new NumBackground(this.attrs.numberBackgroundTextSize, this.attrs.numberBackgroundTextColor, this.attrs.numberBackgroundAlphaColor);
        } else if (this.attrs.calendarBackground != null) {
            monthBackground = (localDate, currentDistance, totalDistance) -> this.attrs.calendarBackground;
        } else {
            monthBackground = new WhiteBackground();
        }

        setMonthCalendarBackground(monthBackground);
        setWeekCalendarBackground(new WhiteBackground());


        addView(monthCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, monthHeight));
        addView(weekCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, weekHeight));

        monthValueAnimator = getValueAnimator(duration);
        monthStretchValueAnimator = getValueAnimator(duration);

        childViewValueAnimator = getValueAnimator(duration);
        OnEndAnimatorListener onEndAnimatorListener = new OnEndAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                callBackCalendarState();
            }
        };
        childViewValueAnimator.addListener(onEndAnimatorListener);

        post(new Runnable() {
            @Override
            public void run() {
                monthCalendar.setVisibility(calendarState == CalendarState.MONTH ? VISIBLE : INVISIBLE);
                weekCalendar.setVisibility(calendarState == CalendarState.WEEK ? VISIBLE : INVISIBLE);
                monthRect = new RectF(0, 0, monthCalendar.getMeasuredWidth(), monthCalendar.getMeasuredHeight());
                weekRect = new RectF(0, 0, weekCalendar.getMeasuredWidth(), weekCalendar.getMeasuredHeight());
                stretchMonthRect = new RectF(0, 0, monthCalendar.getMeasuredWidth(), stretchMonthHeight);
                monthCalendar.setY(calendarState == CalendarState.MONTH ? 0 : getMonthYOnWeekState(weekCalendar.getFirstDate()));
                childView.setY(calendarState == CalendarState.MONTH ? monthHeight : weekHeight);
                isInflateFinish = true;
            }
        });
    }

    private ValueAnimator getValueAnimator(int duration) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(this);
        return valueAnimator;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 3) {
            throw new RuntimeException(getContext().getString(R.string.N_NCalendar_child_num));
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

        weekCalendar.layout(paddingLeft, 0, measuredWidth - paddingRight, weekHeight);

        if (childView.getY() >= monthHeight && isStretchCalendarEnable) {
            monthCalendar.layout(paddingLeft, 0, measuredWidth - paddingRight, stretchMonthHeight);
        } else {
            monthCalendar.layout(paddingLeft, 0, measuredWidth - paddingRight, monthHeight);
        }
        childView.layout(paddingLeft, monthHeight, measuredWidth - paddingRight, childView.getMeasuredHeight() + monthHeight);


    }


    //自动滑动到适当的位置
    private void autoScroll() {
        int childLayoutY = (int) childView.getY();

        if ((calendarState == CalendarState.MONTH || calendarState == CalendarState.MONTH_STRETCH) && childLayoutY <= monthHeight && childLayoutY >= monthHeight * 4 / 5) {
            autoToMonthBySetY();
        } else if ((calendarState == CalendarState.MONTH || calendarState == CalendarState.MONTH_STRETCH) && childLayoutY <= monthHeight * 4 / 5) {
            autoToWeekBySetY();
        } else if ((calendarState == CalendarState.WEEK || calendarState == CalendarState.MONTH_STRETCH) && childLayoutY < weekHeight * 2) {
            autoToWeekBySetY();
        } else if ((calendarState == CalendarState.WEEK || calendarState == CalendarState.MONTH_STRETCH) && childLayoutY >= weekHeight * 2 && childLayoutY <= monthHeight) {
            autoToMonthBySetY();
        } else if (childLayoutY < monthHeight + (stretchMonthHeight - monthHeight) / 2 && childLayoutY >= monthHeight) {
            autoToMonthFromStretch();
        } else if (childLayoutY >= monthHeight + (stretchMonthHeight - monthHeight) / 2) {
            autoToStretchFromMonth();
        }
    }

    //自动从拉伸到月状态 通过改变高度
    private void autoToMonthFromStretch() {
        float monthCalendarStart = monthCalendar.getLayoutParams().height;
        float monthCalendarEnd = monthHeight;
        monthStretchValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);
        monthStretchValueAnimator.start();

        float childViewStart = childView.getY();
        float childViewEnd = monthHeight;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();
    }

    //自动从月到拉伸状态  通过改变高度
    private void autoToStretchFromMonth() {
        float monthCalendarStart = monthCalendar.getLayoutParams().height;
        float monthCalendarEnd = stretchMonthHeight;
        monthStretchValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);
        monthStretchValueAnimator.start();

        float childViewStart = childView.getY();
        float childViewEnd = stretchMonthHeight;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();

    }


    //自动滑动到周 通过setY 月->周
    private void autoToWeekBySetY() {
        float monthCalendarStart = monthCalendar.getY();//起始位置
        float monthCalendarEnd = getMonthCalendarAutoWeekEndY();

        monthValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);
        monthValueAnimator.start();

        float childViewStart = childView.getY();
        float childViewEnd = weekHeight;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();

    }

    //自动滑动到月 通过setY 周->月
    private void autoToMonthBySetY() {
        float monthCalendarStart = monthCalendar.getY();
        float monthCalendarEnd = 0;
        monthValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);
        monthValueAnimator.start();

        float childViewStart = childView.getY();
        float childViewEnd = monthHeight;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        //super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        //跟随手势滑动
        gestureMove(dy, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return childView.getY() != weekHeight;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {

        //该方法 在 View 类中11758行 actionMasked == MotionEvent.ACTION_DOWN down的时候会执行
        //此时 childViewY 必为 3个标志位之一，判断不为这三个数值就自动滑动

        int childViewY = (int) childView.getY();

        if (childViewY != monthHeight && childViewY != weekHeight && childViewY != stretchMonthHeight) {
            autoScroll();
        } else {
            callBackCalendarState();
        }
    }

    /**
     * 手势滑动的逻辑，
     * 1、起始位置 根据滑动的方向判断是月周切换还是月、拉伸切换
     * 2、起始条件触发后，根据childView的位置执行不同的逻辑 setY 和 拉伸高度
     */
    protected void gestureMove(float dy, int[] consumed) {
        float monthCalendarY = monthCalendar.getY();
        float childViewY = childView.getY();

        ViewGroup.LayoutParams layoutParams = monthCalendar.getLayoutParams();
        int realMonthHeight = layoutParams.height;

        if (dy > 0 && childViewY == monthHeight && monthCalendarY == 0) {
            //setY  起始位置 月->周的过程
            if (isStretchCalendarEnable && realMonthHeight != monthHeight) {
                layoutParams.height = monthHeight;
                monthCalendar.setLayoutParams(layoutParams);
            }

            monthCalendar.setY(-getGestureMonthUpOffset(dy) + monthCalendarY);
            childView.setY(-getGestureChildUpOffset(dy) + childViewY);

            if (consumed != null) {
                consumed[1] = (int) dy;
            }
            scrolling(dy);

        } else if (dy < 0 && childViewY == monthHeight && monthCalendarY == 0 && isStretchCalendarEnable) {
            //拉伸  起始位置 月->拉伸的过程
            float monthOffset = getOffset(-dy, stretchMonthHeight - realMonthHeight);
            layoutParams.height = (int) (layoutParams.height + monthOffset);
            monthCalendar.setLayoutParams(layoutParams);

            float childOffset = getOffset(-dy, stretchMonthHeight - childViewY);
            childView.setY(childViewY + childOffset);

            if (consumed != null) {
                consumed[1] = (int) dy;
            }
            scrolling(dy);

        } else if (dy > 0 && childViewY <= monthHeight && childViewY != weekHeight) {
            //setY  持续过程  月->周的过程
            if (isStretchCalendarEnable && realMonthHeight != monthHeight) {
                //由于滑动过程中可能存在从拉伸状态直接滑到周的状态，引起monthCalendar的高度还没有到monthHeight就向上滑动，所以在这里判断一下，高度正常之后才上滑
                layoutParams.height = monthHeight;
                monthCalendar.setLayoutParams(layoutParams);
            }

            monthCalendar.setY(-getGestureMonthUpOffset(dy) + monthCalendarY);
            childView.setY(-getGestureChildUpOffset(dy) + childViewY);

            if (consumed != null) {
                consumed[1] = (int) dy;
            }
            scrolling(dy);

        } else if (dy < 0 && childViewY <= monthHeight && childViewY >= weekHeight && (!(isWeekHoldEnable && calendarState == CalendarState.WEEK) || consumed == null) && (targetView == null || !targetView.canScrollVertically(-1))) {
            //setY  持续过程  周->月的过程
            if (isStretchCalendarEnable && realMonthHeight != monthHeight) {
                layoutParams.height = monthHeight;
                monthCalendar.setLayoutParams(layoutParams);
            }

            monthCalendar.setY(getGestureMonthDownOffset(dy) + monthCalendarY);
            childView.setY(getGestureChildDownOffset(dy) + childViewY);

            if (consumed != null) {
                consumed[1] = (int) dy;
            }
            scrolling(dy);

        } else if (dy < 0 && childViewY >= monthHeight && childViewY <= stretchMonthHeight && monthCalendarY == 0 && isStretchCalendarEnable) {
            //拉伸  持续过程  月->拉伸的过程
            float monthOffset = getOffset(-dy, stretchMonthHeight - realMonthHeight);
            layoutParams.height = (int) (layoutParams.height + monthOffset);
            monthCalendar.setLayoutParams(layoutParams);

            float childOffset = getOffset(-dy, stretchMonthHeight - childViewY);
            childView.setY(childViewY + childOffset);

            if (consumed != null) {
                consumed[1] = (int) dy;
            }
            scrolling(dy);

        } else if (dy > 0 && childViewY >= monthHeight && childViewY <= stretchMonthHeight && monthCalendarY == 0 && isStretchCalendarEnable) {
            //拉伸  持续过程 拉伸->月的过程
            float monthOffset = getOffset(-dy, stretchMonthHeight - realMonthHeight);
            layoutParams.height = (int) (layoutParams.height + monthOffset);
            monthCalendar.setLayoutParams(layoutParams);

            float childOffset = getOffset(-dy, stretchMonthHeight - childViewY);
            childView.setY(childViewY + childOffset);

            if (consumed != null) {
                consumed[1] = (int) dy;
            }
            scrolling(dy);
        }

    }


    private float downY;
    private float downX;
    //上次的y
    private float lastY;
    //竖直方向上滑动的临界值，大于这个值认为是竖直滑动
    private final float verticalY = 50.f;
    //第一次手势滑动，因为第一次滑动的偏移量大于verticalY，会出现猛的一划，这里只对第一次滑动做处理
    private boolean isFirstScroll = true;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isInflateFinish) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                downX = ev.getX();
                lastY = downY;
                targetView = ViewUtil.getTargetView(getContext(), childView);
                break;
            case MotionEvent.ACTION_MOVE:
                float y = ev.getY();
                float absY = Math.abs(downY - y);
                boolean inCalendar = isInCalendar(downX, downY);
                if ((absY > verticalY && inCalendar) || targetView == null && absY > verticalY) {
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
                float y = event.getY();
                float dy = lastY - y;
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
    private boolean isInCalendar(float x, float y) {
        if (calendarState == CalendarState.MONTH) {
            return monthRect.contains(x, y);
        } else if (calendarState == CalendarState.WEEK) {
            return weekRect.contains(x, y);
        } else if (calendarState == CalendarState.MONTH_STRETCH) {
            return stretchMonthRect.contains(x, y);
        }
        return false;
    }


    @Override
    public void jumpDate(String formatDate) {
        if (calendarState == CalendarState.WEEK) {
            weekCalendar.jumpDate(formatDate);
        } else {
            monthCalendar.jumpDate(formatDate);
        }
    }

    @Override
    public void jumpDate(int year, int month, int day) {
        if (calendarState == CalendarState.WEEK) {
            weekCalendar.jumpDate(year, month, day);
        } else {
            monthCalendar.jumpDate(year, month, day);
        }
    }

    @Override
    public void jumpMonth(int year, int month) {
        if (calendarState == CalendarState.WEEK) {
            weekCalendar.jumpMonth(year, month);
        } else {
            monthCalendar.jumpMonth(year, month);
        }
    }

    @Override
    public void setInitializeDate(String formatDate) {
        monthCalendar.setInitializeDate(formatDate);
        weekCalendar.setInitializeDate(formatDate);
    }

    @Override
    public void setCheckedDates(List<String> dateList) {
        if (calendarState == CalendarState.WEEK) {
            weekCalendar.setCheckedDates(dateList);
        } else {
            monthCalendar.setCheckedDates(dateList);
        }
    }

    @Override
    public void toToday() {
        if (calendarState == CalendarState.WEEK) {
            weekCalendar.toToday();
        } else {
            monthCalendar.toToday();
        }
    }

    @Override
    public void toWeek() {
        if (calendarState == CalendarState.MONTH) {
            autoToWeekBySetY();
        }
    }

    @Override
    public void toMonth() {
        if (calendarState == CalendarState.WEEK) {
            autoToMonthBySetY();
        } else if (calendarState == CalendarState.MONTH_STRETCH) {
            autoToMonthFromStretch();
        }
    }

    @Override
    public void toStretch() {
        if (calendarState == CalendarState.MONTH) {
            autoToStretchFromMonth();
        }
    }

    @Override
    public CalendarPainter getCalendarPainter() {
        return monthCalendar.getCalendarPainter();
    }

    @Override
    public CalendarAdapter getCalendarAdapter() {
        return monthCalendar.getCalendarAdapter();
    }

    @Override
    public CalendarState getCalendarState() {
        return calendarState;
    }

    @Override
    public void setCalendarState(CalendarState calendarState) {
        if (calendarState == CalendarState.MONTH_STRETCH) {
            throw new RuntimeException(getContext().getString(R.string.N_calendarState_illegal));
        }
        this.calendarState = calendarState;
    }

    @Override
    public void toNextPager() {
        if (calendarState == CalendarState.WEEK) {
            weekCalendar.toNextPager();
        } else {
            monthCalendar.toNextPager();
        }
    }

    @Override
    public void toLastPager() {
        if (calendarState == CalendarState.WEEK) {
            weekCalendar.toLastPager();
        } else {
            monthCalendar.toLastPager();
        }
    }

    @Override
    public void setCheckMode(CheckModel checkModel) {
        monthCalendar.setCheckMode(checkModel);
        weekCalendar.setCheckMode(checkModel);
    }

    @Override
    public CheckModel getCheckModel() {
        return monthCalendar.getCheckModel();
    }


    @Override
    public void setDefaultCheckedFirstDate(boolean isDefaultSelectFitst) {
        monthCalendar.setDefaultCheckedFirstDate(isDefaultSelectFitst);
        weekCalendar.setDefaultCheckedFirstDate(isDefaultSelectFitst);
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
    public void updateSlideDistance(int currentDistance) {
        monthCalendar.updateSlideDistance(currentDistance - weekHeight);
        weekCalendar.updateSlideDistance(currentDistance - weekHeight);
    }

    @Override
    public void setOnCalendarStateChangedListener(OnCalendarStateChangedListener onCalendarStateChangedListener) {
        this.onCalendarStateChangedListener = onCalendarStateChangedListener;
    }

    @Override
    public void setOnCalendarScrollingListener(OnCalendarScrollingListener onCalendarScrollingListener) {
        this.onCalendarScrollingListener = onCalendarScrollingListener;
    }

    @Override
    public void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener) {
        monthCalendar.setOnClickDisableDateListener(onClickDisableDateListener);
        weekCalendar.setOnClickDisableDateListener(onClickDisableDateListener);
    }

    @Override
    public void setCalendarPainter(CalendarPainter calendarPainter) {
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
    public void setMultipleCount(int multipleCount, MultipleCountModel multipleCountModel) {
        monthCalendar.setMultipleCount(multipleCount, multipleCountModel);
        weekCalendar.setMultipleCount(multipleCount, multipleCountModel);
    }

    @Override
    public void setLastNextMonthClickEnable(boolean enable) {
        monthCalendar.setLastNextMonthClickEnable(enable);
        weekCalendar.setLastNextMonthClickEnable(enable);
    }

    @Override
    public Attrs getAttrs() {
        return attrs;
    }


    @Override
    public void setCalendarAdapter(CalendarAdapter calendarAdapter) {
        monthCalendar.setCalendarAdapter(calendarAdapter);
        weekCalendar.setCalendarAdapter(calendarAdapter);
    }

    @Override
    public List<LocalDate> getTotalCheckedDateList() {
        if (calendarState == CalendarState.WEEK) {
            return weekCalendar.getTotalCheckedDateList();
        } else {
            return monthCalendar.getTotalCheckedDateList();
        }
    }

    @Override
    public List<LocalDate> getCurrPagerCheckDateList() {
        if (calendarState == CalendarState.WEEK) {
            return weekCalendar.getCurrPagerCheckDateList();
        } else {
            return monthCalendar.getCurrPagerCheckDateList();
        }
    }

    @Override
    public List<LocalDate> getCurrPagerDateList() {
        if (calendarState == CalendarState.WEEK) {
            return weekCalendar.getCurrPagerDateList();
        } else {
            return monthCalendar.getCurrPagerDateList();
        }
    }

    @Override
    public void setWeekHoldEnable(boolean isWeekHoldEnable) {
        this.isWeekHoldEnable = isWeekHoldEnable;
    }

    @Override
    public void setStretchCalendarEnable(boolean isMonthStretchEnable) {
        this.isStretchCalendarEnable = isMonthStretchEnable;
    }

    @Override
    public void setScrollEnable(boolean scrollEnable) {
        monthCalendar.setScrollEnable(scrollEnable);
        weekCalendar.setScrollEnable(scrollEnable);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        if (animation == monthValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            monthCalendar.setY(animatedValue);
        } else if (animation == monthStretchValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = monthCalendar.getLayoutParams();
            layoutParams.height = (int) animatedValue;
            monthCalendar.setLayoutParams(layoutParams);
        } else if (animation == childViewValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            float top = childView.getY();
            float i = animatedValue - top;
            childView.setY(animatedValue);

            scrolling((int) -i);

        }
    }

    //设置日历的状态、月周的显示及状态回调
    private void callBackCalendarState() {

        int childViewY = (int) childView.getY();

        if (childViewY == weekHeight && calendarState != CalendarState.WEEK) {
            calendarState = CalendarState.WEEK;
            weekCalendar.setVisibility(VISIBLE);
            monthCalendar.setVisibility(INVISIBLE);
            if (onCalendarStateChangedListener != null) {
                onCalendarStateChangedListener.onCalendarStateChange(calendarState);
            }

        } else if (childViewY == monthHeight && calendarState != CalendarState.MONTH) {
            calendarState = CalendarState.MONTH;
            weekCalendar.setVisibility(INVISIBLE);
            monthCalendar.setVisibility(VISIBLE);

            //重新设置周的显示 展开月日历上面可能有选中的日期，需要下次折叠时周日历的显示要正确
            LocalDate pivot = monthCalendar.getPivotDate();
            weekCalendar.jump(pivot, getCheckModel() == CheckModel.SINGLE_DEFAULT_CHECKED, DateChangeBehavior.API);

            if (onCalendarStateChangedListener != null) {
                onCalendarStateChangedListener.onCalendarStateChange(calendarState);
            }
        } else if (childViewY == stretchMonthHeight && calendarState != CalendarState.MONTH_STRETCH) {
            calendarState = CalendarState.MONTH_STRETCH;
            weekCalendar.setVisibility(INVISIBLE);
            monthCalendar.setVisibility(VISIBLE);

            //重新设置周的显示 展开月日历上面可能有选中的日期，需要下次折叠时周日历的显示要正确
            LocalDate pivot = monthCalendar.getPivotDate();
            weekCalendar.jump(pivot, getCheckModel() == CheckModel.SINGLE_DEFAULT_CHECKED, DateChangeBehavior.API);

            if (onCalendarStateChangedListener != null) {
                onCalendarStateChangedListener.onCalendarStateChange(calendarState);
            }
        }

    }

    //childView周状态的条件 容错状态
    protected boolean isChildWeekState() {
        return childView.getY() <= weekHeight;
    }

    //月日历周状态 容错状态
    protected boolean isMonthCalendarWeekState() {
        return monthCalendar.getY() <= -monthCalendar.getPivotDistanceFromTop();
    }

    //滑动中 包含跟随手势和自动滑动
    protected void scrolling(float dy) {
        setWeekVisible(dy > 0);
        updateSlideDistance((int) childView.getY());
        if (onCalendarScrollingListener != null) {
            onCalendarScrollingListener.onCalendarScrolling(dy);
        }
    }


    //获取月日历自动到周状态的y值
    protected abstract float getMonthCalendarAutoWeekEndY();

    //周状态下 月日历的getY 是个负值,用于在 周状态下日期改变设置正确的y值
    protected abstract float getMonthYOnWeekState(LocalDate localDate);

    //滑动过界处理 ，如果大于最大距离就返回最大距离
    protected float getOffset(float offset, float maxOffset) {
        return Math.min(offset, maxOffset);
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
    protected abstract float getGestureMonthUpOffset(float dy);

    /**
     * Child根据手势向上移动的距离
     *
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return 根据不同日历的交互，计算不同的滑动值
     */
    protected abstract float getGestureChildUpOffset(float dy);

    /**
     * 月日历根据手势向下移动的距离
     *
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return 根据不同日历的交互，计算不同的滑动值
     */
    protected abstract float getGestureMonthDownOffset(float dy);

    /**
     * Child根据手势向下移动的距离
     *
     * @param dy 当前滑动的距离 dy>0向上滑动，dy<0向下滑动
     * @return 根据不同日历的交互，计算不同的滑动值
     */
    protected abstract float getGestureChildDownOffset(float dy);


    @Override
    public CalendarBackground getCalendarBackground() throws IllegalAccessException {
        throw new IllegalAccessException(getContext().getString(R.string.N_NCalendar_calendar_background_illegal));
    }

    @Override
    public void setCalendarBackground(CalendarBackground calendarBackground) throws IllegalAccessException {
        throw new IllegalAccessException(getContext().getString(R.string.N_NCalendar_set_calendar_background_illegal));
    }

    @Override
    public void setMonthCalendarBackground(CalendarBackground calendarBackground) {
        monthCalendar.setCalendarBackground(calendarBackground);
    }

    @Override
    public void setWeekCalendarBackground(CalendarBackground calendarBackground) {
        weekCalendar.setCalendarBackground(calendarBackground);
    }
}
