package com.necer.calendar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.necer.adapter.BaseCalendarAdapter;
import com.necer.adapter.MonthCalendarAdapter;
import com.necer.entity.NDate;
import com.necer.listener.OnClickMonthViewListener;
import com.necer.listener.OnMonthAnimatorListener;
import com.necer.listener.OnMonthSelectListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendar extends BaseCalendar implements OnClickMonthViewListener, ValueAnimator.AnimatorUpdateListener {


    protected ValueAnimator monthValueAnimator;//月日历动画
    private OnMonthSelectListener onMonthSelectListener;
    private OnMonthAnimatorListener onMonthAnimatorListener;


    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }
    public MonthCalendar(Context context,Attrs attrs, int duration, OnMonthAnimatorListener onMonthAnimatorListener) {
        super(context,attrs);
        this.onMonthAnimatorListener = onMonthAnimatorListener;
        monthValueAnimator = new ValueAnimator();
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.addUpdateListener(this);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, Attrs attrs, int calendarSize, int currNum) {
        return new MonthCalendarAdapter(context, attrs, calendarSize, currNum, this);
    }

    @Override
    protected int getCalendarSize(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate) + 1;
    }

    @Override
    protected int getTwoDateNum(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate);
    }

    @Override
    protected LocalDate getDate(LocalDate localDate, int count) {
        LocalDate date = localDate.plusMonths(count);
        return date;
    }

    @Override
    protected LocalDate getLastSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusMonths(-1);
    }

    @Override
    protected LocalDate getNextSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusMonths(1);
    }

    @Override
    protected void onSelcetDate(NDate nDate) {
        //选中，即理解为点击，因为点击了肯定会选中
        mOnClickDate = nDate.localDate;
        if (onMonthSelectListener != null) {
            onMonthSelectListener.onMonthSelect(nDate);
        }
    }


    @Override
    public void onClickCurrentMonth(NDate nDate) {
        onSelcetDate(nDate);
        onDateChanged(nDate, true);
        onYearMonthChanged(nDate.localDate.getYear(), nDate.localDate.getMonthOfYear());
        //  Toast.makeText(getContext(), date.toString(), Toast.LENGTH_SHORT).show();
        notifyView(nDate.localDate, true);
    }

    @Override
    public void onClickLastMonth(NDate nDate) {
        onSelcetDate(nDate);
        onDateChanged(nDate, true);
        onYearMonthChanged(nDate.localDate.getYear(), nDate.localDate.getMonthOfYear());
        setCurrentItem(getCurrentItem() - 1, true);
        notifyView(nDate.localDate, true);
    }

    @Override
    public void onClickNextMonth(NDate nDate) {
        onSelcetDate(nDate);
        onDateChanged(nDate, true);
        onYearMonthChanged(nDate.localDate.getYear(), nDate.localDate.getMonthOfYear());
        setCurrentItem(getCurrentItem() + 1, true);
        notifyView(nDate.localDate, true);
    }

    public void setOnMonthSelectListener(OnMonthSelectListener onMonthSelectListener) {
        this.onMonthSelectListener = onMonthSelectListener;
    }


    public int getMonthCalendarOffset() {
        if (mCurrView != null) {
            return mCurrView.getMonthCalendarOffset();
        }
        return 0;

    }

    public void autoToMonth() {
        int top = getTop();//起始位置
        int end = 0;
        monthValueAnimator.setIntValues(top, end);
        monthValueAnimator.start();
    }


    public void autoToMIUIWeek() {
        int top = getTop();//起始位置
        int end = -getMonthCalendarOffset(); //结束位置
        monthValueAnimator.setIntValues(top, end);
        monthValueAnimator.start();
    }

    public void autoToEMUIWeek() {
        int top = getTop();//起始位置
        int end = -getHeight() * 4 / 5; //结束位置
        monthValueAnimator.setIntValues(top, end);
        monthValueAnimator.start();
    }


    public boolean isMonthState() {
        return getTop() >= 0;
    }

    public boolean isWeekState() {
        return getTop() <= -getMonthCalendarOffset();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int animatedValue = (int) animation.getAnimatedValue();
        int top = getTop();
        int i = animatedValue - top;
        offsetTopAndBottom(i);

        if (onMonthAnimatorListener != null) {
            //回调遵循>0向上，<0向下
            onMonthAnimatorListener.onMonthAnimatorChanged(-i);
        }
    }

}
