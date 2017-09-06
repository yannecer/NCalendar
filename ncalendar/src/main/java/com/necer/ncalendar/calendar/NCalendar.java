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
import android.widget.LinearLayout;
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

public class NCalendar extends LinearLayout implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener, OnClickMonthCalendarListener, OnClickWeekCalendarListener, OnWeekCalendarPageChangeListener, OnMonthCalendarPageChangeListener {


    private NWeekCalendar weekCalendar;
    private NMonthCalendar monthCalendar;
    private View nestedScrollingChild;

    public static final int MONTH = 100;
    public static final int WEEK = 200;
    private static int STATE = 100;//默认开
    private int rowHeigh;
    private int heigh;//总的高度
    private int rowNum;//行数
    private int selectRowIndex;//被选中的行

    private int duration = 240;


    int monthCalendarTop;//0到负height的值
    int childViewTop;


    private int monthCalendarOffset;//月日历需要滑动的距离

    private ValueAnimator monthValueAnimator;
    private ValueAnimator nestedScrollingChildValueAnimator;

    public NCalendar(Context context) {
        this(context, null);
    }

    public NCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);

        monthCalendar = new NMonthCalendar(context, attrs);
        addView(monthCalendar);

        weekCalendar = new NWeekCalendar(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        heigh = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, Utils.dp2px(context, 300));
        duration = ta.getInt(R.styleable.NCalendar_duration, 240);
        ta.recycle();

        rowHeigh = heigh / 5;
        monthCalendar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heigh));
        weekCalendar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeigh));


        monthCalendarTop = 0;
        childViewTop = heigh;

        monthCalendar.setOnClickMonthCalendarListener(this);
        monthCalendar.setOnMonthCalendarPageChangeListener(this);
        weekCalendar.setOnClickWeekCalendarListener(this);
        weekCalendar.setOnWeekCalendarPageChangeListener(this);

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

                if (top == heigh) {
                    STATE = MONTH;
                    requestLayout();
                } else {
                    STATE = WEEK;
                    weekCalendar.setVisibility(VISIBLE);
                    requestLayout();
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

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

        if (monthCalendarTop == 0 && nestedScrollingChildTop == heigh) {
            return;
        }

        if (monthCalendarTop == -monthCalendarOffset && nestedScrollingChildTop == rowHeigh) {
            return;
        }

        if (STATE == MONTH) {
            if (heigh - nestedScrollingChildTop < rowHeigh) {
                autoOpen(monthCalendarTop, 0, nestedScrollingChildTop, heigh);
            } else {
                autoClose(monthCalendarTop, -monthCalendarOffset, nestedScrollingChildTop, rowHeigh);
            }

        } else {
            if (nestedScrollingChildTop < rowHeigh * 2) {
                autoClose(monthCalendarTop, -monthCalendarOffset, nestedScrollingChildTop, rowHeigh);
            } else {
                autoOpen(monthCalendarTop, 0, nestedScrollingChildTop, heigh);
            }
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        int monthTop = monthCalendar.getTop();
        int nestedScrollingChildTop = nestedScrollingChild.getTop();


        NMonthView currectMonthView = monthCalendar.getCurrectMonthView();

        rowNum = currectMonthView.getRowNum();
        selectRowIndex = currectMonthView.getSelectRowIndex();

        //month需要移动selectRowIndex*h/rowNum ,计算时依每个行高的中点计算
        monthCalendarOffset = selectRowIndex * heigh / rowNum;

        if (dy > 0 && Math.abs(monthTop) < monthCalendarOffset) {
            int offset = getOffset(dy, monthCalendarOffset - Math.abs(monthTop));
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
        } else if (dy < 0 && monthTop == 0 && nestedScrollingChildTop != heigh && !ViewCompat.canScrollVertically(target, -1)) {
            int offset = getOffset(Math.abs(dy), heigh - nestedScrollingChildTop);
            nestedScrollingChild.offsetTopAndBottom(offset);
            consumed[1] = dy;
        }

        if (nestedScrollingChildTop == rowHeigh) {
            STATE = WEEK;
            weekCalendar.setVisibility(VISIBLE);
        }

        if (STATE == WEEK && dy < 0 && !ViewCompat.canScrollVertically(target, -1)) {
            weekCalendar.setVisibility(INVISIBLE);
        }

        if (nestedScrollingChildTop == heigh) {
            STATE = MONTH;
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //  super.onLayout(changed, l, t, r, b);


        if (STATE == MONTH) {
            monthCalendarTop = 0;
            childViewTop = heigh;

        } else {
            NMonthView currectMonthView = monthCalendar.getCurrectMonthView();
            int rowNum = currectMonthView.getRowNum();
            int selectRowIndex = currectMonthView.getSelectRowIndex();
            //month需要移动selectRowIndex*h/rowNum ,计算时依每个行高的中点计算
            int monthCalendarOffset = selectRowIndex * heigh / rowNum;

            monthCalendarTop = -monthCalendarOffset;
            childViewTop = nestedScrollingChild.getTop();
        }

        monthCalendar.layout(0, monthCalendarTop, r, heigh + monthCalendarTop);
        ViewGroup.LayoutParams layoutParams = nestedScrollingChild.getLayoutParams();
        nestedScrollingChild.layout(0, childViewTop, r, layoutParams.height + childViewTop);
    }

    //自动开
    private void autoOpen(int startMonth, int endMonth, int startChild, int endChild) {
        // STATE = MONTH;
        monthValueAnimator.setIntValues(startMonth, endMonth);
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.start();

        nestedScrollingChildValueAnimator.setIntValues(startChild, endChild);
        nestedScrollingChildValueAnimator.setDuration(duration);
        nestedScrollingChildValueAnimator.start();
    }

    //自动闭
    private void autoClose(int startMonth, int endMonth, int startChild, int endChild) {
        //  STATE = WEEK;
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
