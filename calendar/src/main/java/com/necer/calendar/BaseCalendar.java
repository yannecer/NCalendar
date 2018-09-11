package com.necer.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import com.necer.R;
import com.necer.adapter.BaseCalendarAdapter;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class BaseCalendar extends ViewPager{

    protected int mCalendarSize;
    protected int mCurrNum;

    public BaseCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.NCalendar);

        Attrs attrs = new Attrs();
        attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, getResources().getColor(R.color.solarTextColor));
        attrs.lunarTextColor = ta.getColor(R.styleable.NCalendar_lunarTextColor, getResources().getColor(R.color.lunarTextColor));
        attrs.selectCircleColor = ta.getColor(R.styleable.NCalendar_selectCircleColor, getResources().getColor(R.color.selectCircleColor));
        attrs.hintColor = ta.getColor(R.styleable.NCalendar_hintColor, getResources().getColor(R.color.hintColor));
        attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, Util.sp2px(context, 18));
        attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, Util.sp2px(context, 10));
        attrs.selectCircleRadius = ta.getDimension(R.styleable.NCalendar_selectCircleRadius, Util.dp2px(context, 20));

        attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, true);

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



        LocalDate startDate = new LocalDate(startString == null ? "1901-01-01" : startString);
        LocalDate endDate = new LocalDate(endString == null ? "2099-12-31" : endString);

        mCalendarSize = getCalendarSize(startDate,endDate,attrs.firstDayOfWeek);
        mCurrNum = getCurrNum(startDate,attrs.firstDayOfWeek);

        setAdapter(getCalendarAdapter(context,attrs,mCalendarSize,mCurrNum));

        setCurrentItem(mCurrNum);



    }


    protected abstract BaseCalendarAdapter getCalendarAdapter(Context context,Attrs attrs,int calendarSize,int currNum);

    /**
     * 日历的页数
     * @return
     */
    protected abstract int getCalendarSize(LocalDate startDate,LocalDate endDate,int type);

    /**
     * 日历当前的页码 开始时间和当天比较
     * @return
     */
    protected abstract int getCurrNum(LocalDate startDate,int type);
}
