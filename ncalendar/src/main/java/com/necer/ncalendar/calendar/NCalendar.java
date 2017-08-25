package com.necer.ncalendar.calendar;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.necer.ncalendar.R;
import com.necer.ncalendar.listener.OnCalendarChangeListener;
import com.necer.ncalendar.listener.OnClickMonthCalendarListener;
import com.necer.ncalendar.listener.OnClickWeekCalendarListener;
import com.necer.ncalendar.listener.OnMonthCalendarPageChangeListener;
import com.necer.ncalendar.listener.OnWeekCalendarPageChangeListener;
import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NCalendar extends  LinearLayout implements NestedScrollingParent, OnMonthCalendarPageChangeListener, OnClickMonthCalendarListener, OnClickWeekCalendarListener, OnWeekCalendarPageChangeListener, ValueAnimator.AnimatorUpdateListener  {


    private WeekCalendar weekCalendar;
    private MonthCalendar monthCalendar;
    private View nestedScrollingChild;

    public static final int OPEN = 100;
    public static final int CLOSE = 200;
    private static int STATE = 100;//默认开
    private int rowHeigh;

    private int duration;

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

    }


}
