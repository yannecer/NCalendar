package com.necer.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.necer.MyLog;
import com.necer.R;
import com.necer.adapter.BaseCalendarAdapter;
import com.necer.utils.Attrs;
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
    protected int mCurrNum;
    private BaseCalendarAdapter calendarAdapter;
    private Attrs attrs;
    private BaseCalendarView mCurrView;//当前显示的页面
    private BaseCalendarView mLastView;//当前显示的页面的上一个页面
    private BaseCalendarView mNextView;//当前显示的页面的下一个页面

    protected LocalDate mSelectDate;//日历上面点击选中的日期

    private List<LocalDate> mPointList;

    public BaseCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.NCalendar);

        attrs = new Attrs();
        attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, getResources().getColor(R.color.solarTextColor));
        attrs.lunarTextColor = ta.getColor(R.styleable.NCalendar_lunarTextColor, getResources().getColor(R.color.lunarTextColor));
        attrs.selectCircleColor = ta.getColor(R.styleable.NCalendar_selectCircleColor, getResources().getColor(R.color.selectCircleColor));
        attrs.hintColor = ta.getColor(R.styleable.NCalendar_hintColor, getResources().getColor(R.color.hintColor));
        attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, Util.sp2px(context, 18));
        attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, Util.sp2px(context, 10));
        attrs.selectCircleRadius = ta.getDimension(R.styleable.NCalendar_selectCircleRadius, Util.dp2px(context, 20));

        attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, true);
        attrs.isDefaultSelect = ta.getBoolean(R.styleable.NCalendar_isDefaultSelect, true);

        attrs.pointSize = ta.getDimension(R.styleable.NCalendar_pointSize, (int) Util.dp2px(context, 2));
        attrs.pointColor = ta.getColor(R.styleable.NCalendar_pointColor, getResources().getColor(R.color.pointColor));
        attrs.hollowCircleColor = ta.getColor(R.styleable.NCalendar_hollowCircleColor, Color.WHITE);
        attrs.hollowCircleStroke = ta.getDimension(R.styleable.NCalendar_hollowCircleStroke, Util.dp2px(context, 1));

        attrs.monthCalendarHeight = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, Util.dp2px(context, 300));
        attrs.duration = ta.getInt(R.styleable.NCalendar_duration, 240);

        attrs.isShowHoliday = ta.getBoolean(R.styleable.NCalendar_isShowHoliday, true);
        attrs.holidayColor = ta.getColor(R.styleable.NCalendar_holidayColor, getResources().getColor(R.color.holidayColor));
        attrs.workdayColor = ta.getColor(R.styleable.NCalendar_workdayColor, getResources().getColor(R.color.workdayColor));

        attrs.backgroundColor = ta.getColor(R.styleable.NCalendar_backgroundColor, getResources().getColor(R.color.white));

        String firstDayOfWeek = ta.getString(R.styleable.NCalendar_firstDayOfWeek);
        String defaultCalendar = ta.getString(R.styleable.NCalendar_defaultCalendar);

        String startString = ta.getString(R.styleable.NCalendar_startDate);
        String endString = ta.getString(R.styleable.NCalendar_endDate);

        attrs.firstDayOfWeek = "Monday".equals(firstDayOfWeek) ? 1 : 0;
        //  attr.defaultCalendar = "Week".equals(defaultCalendar) ? NCalendar.WEEK : NCalendar.MONTH;

        ta.recycle();


        mPointList = new ArrayList<>();

        LocalDate startDate = new LocalDate(startString == null ? "1901-01-01" : startString);
        LocalDate endDate = new LocalDate(endString == null ? "2099-12-31" : endString);

        mCalendarSize = getCalendarSize(startDate, endDate, attrs.firstDayOfWeek);
        mCurrNum = getCurrNum(startDate, new LocalDate(), attrs.firstDayOfWeek);

        calendarAdapter = getCalendarAdapter(context, attrs, mCalendarSize, mCurrNum);
        setAdapter(calendarAdapter);

        setBackgroundColor(attrs.backgroundColor);

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(final int position) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        BaseCalendarView currectView = calendarAdapter.getBaseCalendarView(position);
                        BaseCalendarView lastView = calendarAdapter.getBaseCalendarView(position - 1);
                        BaseCalendarView nextView = calendarAdapter.getBaseCalendarView(position + 1);
                        reDraw(lastView, currectView, nextView);
                    }
                });
            }
        });

        setCurrentItem(mCurrNum);

    }

    private void reDraw(BaseCalendarView lastView, BaseCalendarView currectView, BaseCalendarView nextView) {
        this.mCurrView = currectView;
        this.mLastView = lastView;
        this.mNextView = nextView;

        LocalDate initialDate = currectView.getInitialDate();
        //当前页面的初始值和上个页面选中的日期，相差几月或几周，再又上个页面选中的日期得出当前页面选中的日期
        if (mSelectDate != null) {
            int currNum = getCurrNum(mSelectDate, initialDate, attrs.firstDayOfWeek);//得出两个页面相差几个
            mSelectDate = getDate(mSelectDate, currNum);
        } else {
            mSelectDate = initialDate;
        }
        notifyView(mSelectDate);

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
    protected void notifyView(LocalDate currectSelectDate) {
        this.mSelectDate = currectSelectDate;
        mCurrView.setSelectDate(currectSelectDate, mPointList);

        if (mLastView != null) {
            mLastView.setSelectDate(getLastSelectDate(currectSelectDate), mPointList);
        }
        if (mNextView != null) {
            mNextView.setSelectDate(getNextSelectDate(currectSelectDate), mPointList);
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
     * 日历当前的页码 开始时间和当天比较
     *
     * @return
     */
    protected abstract int getCurrNum(LocalDate startDate, LocalDate endDate, int type);

    /**
     * 相差count的日期
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

}
