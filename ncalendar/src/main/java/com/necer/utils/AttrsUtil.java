package com.necer.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.necer.R;
import com.necer.enumeration.CalendarState;

/**
 * Created by necer on 2018/11/28.
 */
public class AttrsUtil {


    public static Attrs getAttrs(Context context, AttributeSet attributeSet) {
        Attrs attrs = new Attrs();

        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.NCalendar);

        attrs.todayCheckedSolarTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedSolarTextColor, context.getResources().getColor(R.color.N_white));
        attrs.todayUnCheckedSolarTextColor = ta.getColor(R.styleable.NCalendar_todayUnCheckedSolarTextColor, context.getResources().getColor(R.color.N_todaySolarUnCheckedTextColor));
        attrs.defaultCheckedSolarTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedSolarTextColor, context.getResources().getColor(R.color.N_defaultSolarTextColor));
        attrs.defaultUnCheckedSolarTextColor = ta.getColor(R.styleable.NCalendar_defaultUnCheckedSolarTextColor, context.getResources().getColor(R.color.N_defaultSolarTextColor));
        attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, context.getResources().getDimension(R.dimen.N_solarTextSize));

        attrs.pointLocation = ta.getInt(R.styleable.NCalendar_pointLocation, Attrs.UP);
        attrs.pointDistance = ta.getDimension(R.styleable.NCalendar_pointDistance, context.getResources().getDimension(R.dimen.N_pointDistance));
        attrs.todayCheckedPoint = ta.getResourceId(R.styleable.NCalendar_todayCheckedPoint, R.drawable.n_point_checked_today);
        attrs.todayUnCheckedPoint = ta.getResourceId(R.styleable.NCalendar_todayUnCheckedPoint, R.drawable.n_point_unchecked_today);
        attrs.defaultCheckedPoint = ta.getResourceId(R.styleable.NCalendar_defaultCheckedPoint, R.drawable.n_point_checked_default);
        attrs.defaultUnCheckedPoint = ta.getResourceId(R.styleable.NCalendar_defaultUnCheckedPoint, R.drawable.n_point_unchecked_default);

        attrs.todayCheckedHoliday = ta.getDrawable(R.styleable.NCalendar_todayCheckedHoliday);
        attrs.todayUnCheckedHoliday = ta.getDrawable(R.styleable.NCalendar_todayUnCheckedHoliday);
        attrs.defaultCheckedHoliday = ta.getDrawable(R.styleable.NCalendar_defaultCheckedHoliday);
        attrs.defaultUnCheckedHoliday = ta.getDrawable(R.styleable.NCalendar_defaultUnCheckedHoliday);
        attrs.todayCheckedWorkday = ta.getDrawable(R.styleable.NCalendar_todayCheckedWorkday);
        attrs.todayUnCheckedWorkday = ta.getDrawable(R.styleable.NCalendar_todayUnCheckedWorkday);
        attrs.defaultCheckedWorkday = ta.getDrawable(R.styleable.NCalendar_defaultCheckedWorkday);
        attrs.defaultUnCheckedWorkday = ta.getDrawable(R.styleable.NCalendar_defaultUnCheckedWorkday);

        attrs.isShowHoliday = ta.getBoolean(R.styleable.NCalendar_isShowHoliday, context.getResources().getBoolean(R.bool.N_isShowHoliday));
        attrs.holidayWorkdayTextSize = ta.getDimension(R.styleable.NCalendar_holidayWorkdayTextSize, context.getResources().getDimension(R.dimen.N_holidayWorkdayTextSize));
        attrs.holidayWorkdayDistance = ta.getDimension(R.styleable.NCalendar_holidayWorkdayDistance, context.getResources().getDimension(R.dimen.N_holidayWorkdayDistance));
        attrs.holidayWorkdayLocation = ta.getInt(R.styleable.NCalendar_holidayWorkdayLocation, Attrs.TOP_RIGHT);
        attrs.holidayText = ta.getString(R.styleable.NCalendar_holidayText);
        attrs.workdayText = ta.getString(R.styleable.NCalendar_workdayText);
        attrs.todayCheckedHolidayTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedHolidayTextColor, context.getResources().getColor(R.color.N_white));
        attrs.todayUnCheckedHolidayTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedHolidayTextColor, context.getResources().getColor(R.color.N_holidayTextColor));
        attrs.defaultCheckedHolidayTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedHolidayTextColor, context.getResources().getColor(R.color.N_holidayTextColor));
        attrs.defaultUnCheckedHolidayTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedHolidayTextColor, context.getResources().getColor(R.color.N_holidayTextColor));
        attrs.todayCheckedWorkdayTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedWorkdayTextColor, context.getResources().getColor(R.color.N_white));
        attrs.todayUnCheckedWorkdayTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedWorkdayTextColor, context.getResources().getColor(R.color.N_workdayTextColor));
        attrs.defaultCheckedWorkdayTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedWorkdayTextColor, context.getResources().getColor(R.color.N_workdayTextColor));
        attrs.defaultUnCheckedWorkdayTextColor = ta.getColor(R.styleable.NCalendar_defaultUnCheckedWorkdayTextColor, context.getResources().getColor(R.color.N_workdayTextColor));


        attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, context.getResources().getBoolean(R.bool.N_isShowLunar));
        attrs.todayCheckedLunarTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedLunarTextColor, context.getResources().getColor(R.color.N_white));
        attrs.todayUnCheckedLunarTextColor = ta.getColor(R.styleable.NCalendar_todayUnCheckedLunarTextColor, context.getResources().getColor(R.color.N_todayCheckedColor));
        attrs.defaultCheckedLunarTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedLunarTextColor, context.getResources().getColor(R.color.N_defaultLunarTextColor));
        attrs.defaultUnCheckedLunarTextColor = ta.getColor(R.styleable.NCalendar_defaultUnCheckedLunarTextColor, context.getResources().getColor(R.color.N_defaultLunarTextColor));
        attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, context.getResources().getDimension(R.dimen.N_lunarTextSize));
        attrs.lunarDistance = ta.getDimension(R.styleable.NCalendar_lunarDistance, context.getResources().getDimension(R.dimen.N_lunarDistance));


        attrs.calendarHeight = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, context.getResources().getDimension(R.dimen.N_calendarHeight));
        attrs.stretchCalendarHeight = (int) ta.getDimension(R.styleable.NCalendar_stretchCalendarHeight, context.getResources().getDimension(R.dimen.N_stretchCalendarHeight));
        attrs.animationDuration = ta.getInt(R.styleable.NCalendar_animationDuration, context.getResources().getInteger(R.integer.N_animationDuration));
        attrs.bgCalendarColor = ta.getColor(R.styleable.NCalendar_bgCalendarColor, context.getResources().getColor(R.color.N_white));
        attrs.firstDayOfWeek = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, Attrs.SUNDAY);
        attrs.defaultCalendar = ta.getInt(R.styleable.NCalendar_defaultCalendar, CalendarState.MONTH.getValue());

        attrs.lastNextMothAlphaColor = ta.getInt(R.styleable.NCalendar_lastNextMothAlphaColor, context.getResources().getInteger(R.integer.N_lastNextMothAlphaColor));
        attrs.disabledAlphaColor = ta.getInt(R.styleable.NCalendar_disabledAlphaColor, context.getResources().getInteger(R.integer.N_disabledAlphaColor));
        attrs.disabledString = ta.getString(R.styleable.NCalendar_disabledString);

        attrs.stretchTextSize = ta.getDimension(R.styleable.NCalendar_stretchTextSize, context.getResources().getDimension(R.dimen.N_stretchTextSize));
        attrs.stretchTextDistance = ta.getDimension(R.styleable.NCalendar_stretchTextDistance, context.getResources().getDimension(R.dimen.N_stretchTextDistance));
        attrs.stretchTextColor = ta.getColor(R.styleable.NCalendar_stretchTextColor, context.getResources().getColor(R.color.N_stretchTextColor));

        attrs.isAllMonthSixLine = ta.getBoolean(R.styleable.NCalendar_isAllMonthSixLine, context.getResources().getBoolean(R.bool.N_isAllMonthSixLine));
        attrs.isShowNumberBackground = ta.getBoolean(R.styleable.NCalendar_isShowNumberBackground, context.getResources().getBoolean(R.bool.N_isShowNumberBackground));
        attrs.numberBackgroundTextSize = ta.getDimension(R.styleable.NCalendar_numberBackgroundTextSize, context.getResources().getDimension(R.dimen.N_numberBackgroundTextSize));
        attrs.numberBackgroundTextColor = ta.getColor(R.styleable.NCalendar_numberBackgroundTextColor, context.getResources().getColor(R.color.N_todaySolarUnCheckedTextColor));
        attrs.numberBackgroundAlphaColor = ta.getInt(R.styleable.NCalendar_numberBackgroundAlphaColor, context.getResources().getInteger(R.integer.N_numberBackgroundAlphaColor));

        attrs.isLastNextMonthClickEnable = ta.getBoolean(R.styleable.NCalendar_isLastNextMonthClickEnable, context.getResources().getBoolean(R.bool.N_isLastNextMonthClickEnable));
        attrs.todayCheckedBackground = ta.getResourceId(R.styleable.NCalendar_todayCheckedBackground, R.drawable.n_bg_checked_today);
        attrs.defaultCheckedBackground = ta.getResourceId(R.styleable.NCalendar_defaultCheckedBackground, R.drawable.n_bg_checked_default);


        ta.recycle();

        return attrs;

    }

}
