package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.necer.adapter.BaseCalendarAdapter;
import com.necer.entity.NDate;
import com.necer.listener.OnDateChangedListener;
import com.necer.listener.OnYearMonthChangedListener;
import com.necer.utils.Attrs;
import com.necer.utils.AttrsUtil;
import com.necer.utils.Util;
import com.necer.view.BaseCalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class BaseCalendar extends ViewPager {

    protected int mCalendarSize;
    private BaseCalendarAdapter calendarAdapter;
    private Attrs attrs;
    protected BaseCalendarView mCurrView;//当前显示的页面
    protected BaseCalendarView mLastView;//当前显示的页面的上一个页面
    protected BaseCalendarView mNextView;//当前显示的页面的下一个页面

    protected LocalDate mSelectDate;//日历上面点击选中的日期,包含点击选中和翻页选中
    protected LocalDate mOnClickDate;//专值点击选中的日期

    private List<LocalDate> mPointList;

    protected OnYearMonthChangedListener onYearMonthChangedListener;
    //上次回调的年，月
    protected int mLaseYear;
    protected int mLastMonth;


    protected OnDateChangedListener onDateChangedListener;

    public BaseCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        Attrs attrs = AttrsUtil.getAttrs(context, attributeSet);
        init(context, attrs);
    }


    public BaseCalendar(Context context,Attrs attrs) {
        super(context);
        init(context,attrs);
    }


    private void init(Context context, Attrs attrs) {
        this.attrs = attrs;
        mPointList = new ArrayList<>();
        LocalDate startDate = new LocalDate("1901-01-01");
        LocalDate endDate = new LocalDate("2099-12-31");

        mCalendarSize = getCalendarSize(startDate, endDate, attrs.firstDayOfWeek);
        int currNum = getTwoDateNum(startDate, new LocalDate(), attrs.firstDayOfWeek);

        calendarAdapter = getCalendarAdapter(context, attrs, mCalendarSize, currNum);
        setAdapter(calendarAdapter);

        setBackgroundColor(attrs.bgCalendarColor);

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageSelected(final int position) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        reDraw(position);
                    }
                });
            }
        });

        setCurrentItem(currNum);

    }

    private void reDraw(int position) {

        this.mCurrView = calendarAdapter.getBaseCalendarView(position);
        this.mLastView = calendarAdapter.getBaseCalendarView(position - 1);
        this.mNextView = calendarAdapter.getBaseCalendarView(position + 1);

        LocalDate initialDate = mCurrView.getInitialDate();
        LocalDate localDate;
        //当前页面的初始值和上个页面选中的日期，相差几月或几周，再又上个页面选中的日期得出当前页面选中的日期
        if (mSelectDate != null) {
            int currNum = getTwoDateNum(mSelectDate, initialDate, attrs.firstDayOfWeek);//得出两个页面相差几个
            localDate = getDate(mSelectDate, currNum);
        } else {
            localDate = initialDate;
        }

        //绘制的规则：1、默认选中，每个页面都会有选中。1、默认不选中，但是点击了当前页面的某个日期
        boolean isDraw = attrs.isDefaultSelect || (mSelectDate.equals(mOnClickDate));
        notifyView(localDate, isDraw);



        //选中回调 ,绘制了才会回到
        if (isDraw) {
            onSelcetDate(Util.getNDate(mSelectDate));
        }
        //年月回调
        onYearMonthChanged(mSelectDate.getYear(), mSelectDate.getMonthOfYear());
        //日期回调
        onDateChanged(Util.getNDate(mSelectDate), isDraw);


    }

    public void setPointList(List<String> list) {
        mPointList.clear();
        for (int i = 0; i < list.size(); i++) {
            mPointList.add(new LocalDate(list.get(i)));
        }
        if (mCurrView != null) {
            mCurrView.invalidate();
        }
        if (mLastView != null) {
            mLastView.invalidate();
        }
        if (mNextView != null) {
            mNextView.invalidate();
        }
    }

    //刷新页面
    protected void notifyView(LocalDate currectSelectDate, boolean isDraw) {
        this.mSelectDate = currectSelectDate;
        if (mCurrView != null) {
            mCurrView.setSelectDate(currectSelectDate, mPointList, isDraw);
        }

        if (mLastView != null) {
            mLastView.setSelectDate(getLastSelectDate(currectSelectDate), mPointList, isDraw);
        }
        if (mNextView != null) {
            mNextView.setSelectDate(getNextSelectDate(currectSelectDate), mPointList, isDraw);
        }
    }


    protected abstract BaseCalendarAdapter getCalendarAdapter(Context context, Attrs attrs, int calendarSize, int currNum);

    /**
     * 日历的页数
     *
     * @return
     */
    protected abstract int getCalendarSize(LocalDate startDate, LocalDate endDate, int type);

    /**
     * 日历开始日期和结束日期的相差数量
     *
     * @return
     */
    protected abstract int getTwoDateNum(LocalDate startDate, LocalDate endDate, int type);

    /**
     * 相差count之后的的日期
     *
     * @param localDate
     * @param count
     * @return
     */
    protected abstract LocalDate getDate(LocalDate localDate, int count);

    /**
     * 重绘当前页面时，获取上个月选中的日期
     *
     * @return
     */
    protected abstract LocalDate getLastSelectDate(LocalDate currectSelectDate);


    /**
     * 重绘当前页面时，获取下个月选中的日期
     *
     * @return
     */
    protected abstract LocalDate getNextSelectDate(LocalDate currectSelectDate);


    /**
     * 日历上面选中的日期，有选中圈的才会回调
     *
     * @param nDate
     */
    protected abstract void onSelcetDate(NDate nDate);

    /**
     * 年份和月份变化回调,点击和翻页都会回调，不管有没有日期选中
     *
     * @param year
     * @param month
     */
    public void onYearMonthChanged(int year, int month) {
        if (onYearMonthChangedListener != null && (year != mLaseYear || month != mLastMonth)) {
            mLaseYear = year;
            mLastMonth = month;
            onYearMonthChangedListener.onYearMonthChanged(this, year, month);
        }
    }

    /**
     * 任何操作都会回调
     *
     * @param date
     * @param isDraw 页面是否选中
     */
    public void onDateChanged(NDate date, boolean isDraw) {
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(this, date, isDraw);
        }
    }

    public void setOnYearMonthChangeListener(OnYearMonthChangedListener onYearMonthChangedListener) {
        this.onYearMonthChangedListener = onYearMonthChangedListener;
    }

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.onDateChangedListener = onDateChangedListener;
    }



    /**
     * 下一页，月日历即是下一月，周日历即是下一周
     */
    public void toNextPager() {
        setCurrentItem(getCurrentItem() + 1, true);
    }

    /**
     * 上一页
     */
    public void toLastPager() {
        setCurrentItem(getCurrentItem() - 1, true);
    }


    /**
     * 跳转日期
     *
     * @param formatDate
     */
    public void jumpDate(String formatDate) {

        LocalDate jumpDate = null;
        try {
            jumpDate = new LocalDate(formatDate);
        } catch (Exception e) {
            throw new RuntimeException("jumpDate的参数需要 yyyy-MM-dd 格式的日期");
        }

        jumpDate(jumpDate, true);
    }

    //回到今天
    public void toToday() {
        jumpDate(new LocalDate(), true);
    }


    //
    protected void jumpDate(LocalDate localDate, boolean isDraw) {
        if (mSelectDate != null) {
            mOnClickDate = localDate;

            int num = getTwoDateNum(mSelectDate, localDate, attrs.firstDayOfWeek);
            setCurrentItem(getCurrentItem() + num, Math.abs(num) == 1);

            //同一月份的跳转回调
            if (mCurrView.isEqualsMonthOrWeek(localDate,mSelectDate)) {
                onDateChanged(Util.getNDate(localDate), isDraw);
            }

            notifyView(localDate, isDraw);
        }
    }
}
