package com.necer.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.necer.R;
import com.necer.enumeration.CalendarState;

/**
 * @author necer
 * @date 2018/11/28
 */
public class AttrsUtil {


    public static Attrs getAttrs(Context context, AttributeSet attributeSet) {
        Attrs attrs = new Attrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.NCalendar);

        attrs.todayCheckedBackground = ta.getResourceId(R.styleable.NCalendar_todayCheckedBackground, R.drawable.n_bg_checked_today);
        attrs.defaultCheckedBackground = ta.getResourceId(R.styleable.NCalendar_defaultCheckedBackground, R.drawable.n_bg_checked_default);

        attrs.todayCheckedSolarTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedSolarTextColor, ContextCompat.getColor(context, R.color.N_white));
        attrs.todayUnCheckedSolarTextColor = ta.getColor(R.styleable.NCalendar_todayUnCheckedSolarTextColor, ContextCompat.getColor(context, R.color.N_todaySolarUnCheckedTextColor));
        attrs.defaultCheckedSolarTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedSolarTextColor, ContextCompat.getColor(context, R.color.N_defaultSolarTextColor));
        attrs.defaultUnCheckedSolarTextColor = ta.getColor(R.styleable.NCalendar_defaultUnCheckedSolarTextColor, ContextCompat.getColor(context, R.color.N_defaultSolarTextColor));
        attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, context.getResources().getDimension(R.dimen.N_solarTextSize));
        attrs.solarTextBold = ta.getBoolean(R.styleable.NCalendar_solarTextBold, context.getResources().getBoolean(R.bool.N_textBold));

        attrs.showLunar = ta.getBoolean(R.styleable.NCalendar_showLunar, context.getResources().getBoolean(R.bool.N_showLunar));
        attrs.todayCheckedLunarTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedLunarTextColor, ContextCompat.getColor(context, R.color.N_white));
        attrs.todayUnCheckedLunarTextColor = ta.getColor(R.styleable.NCalendar_todayUnCheckedLunarTextColor, ContextCompat.getColor(context, R.color.N_todayCheckedColor));
        attrs.defaultCheckedLunarTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedLunarTextColor, ContextCompat.getColor(context, R.color.N_defaultLunarTextColor));
        attrs.defaultUnCheckedLunarTextColor = ta.getColor(R.styleable.NCalendar_defaultUnCheckedLunarTextColor, ContextCompat.getColor(context, R.color.N_defaultLunarTextColor));
        attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, context.getResources().getDimension(R.dimen.N_lunarTextSize));
        attrs.lunarTextBold = ta.getBoolean(R.styleable.NCalendar_lunarTextBold, context.getResources().getBoolean(R.bool.N_textBold));
        attrs.lunarDistance = ta.getDimension(R.styleable.NCalendar_lunarDistance, context.getResources().getDimension(R.dimen.N_lunarDistance));

        attrs.pointLocation = ta.getInt(R.styleable.NCalendar_pointLocation, Attrs.UP);
        attrs.pointDistance = ta.getDimension(R.styleable.NCalendar_pointDistance, context.getResources().getDimension(R.dimen.N_pointDistance));
        attrs.todayCheckedPoint = ta.getResourceId(R.styleable.NCalendar_todayCheckedPoint, R.drawable.n_point_checked_today);
        attrs.todayUnCheckedPoint = ta.getResourceId(R.styleable.NCalendar_todayUnCheckedPoint, R.drawable.n_point_unchecked_today);
        attrs.defaultCheckedPoint = ta.getResourceId(R.styleable.NCalendar_defaultCheckedPoint, R.drawable.n_point_checked_default);
        attrs.defaultUnCheckedPoint = ta.getResourceId(R.styleable.NCalendar_defaultUnCheckedPoint, R.drawable.n_point_unchecked_default);

        attrs.showHolidayWorkday = ta.getBoolean(R.styleable.NCalendar_showHoliday, context.getResources().getBoolean(R.bool.N_showHolidayWorkday));

        attrs.todayCheckedHoliday = ta.getDrawable(R.styleable.NCalendar_todayCheckedHoliday);
        attrs.todayUnCheckedHoliday = ta.getDrawable(R.styleable.NCalendar_todayUnCheckedHoliday);
        attrs.defaultCheckedHoliday = ta.getDrawable(R.styleable.NCalendar_defaultCheckedHoliday);
        attrs.defaultUnCheckedHoliday = ta.getDrawable(R.styleable.NCalendar_defaultUnCheckedHoliday);
        attrs.todayCheckedWorkday = ta.getDrawable(R.styleable.NCalendar_todayCheckedWorkday);
        attrs.todayUnCheckedWorkday = ta.getDrawable(R.styleable.NCalendar_todayUnCheckedWorkday);
        attrs.defaultCheckedWorkday = ta.getDrawable(R.styleable.NCalendar_defaultCheckedWorkday);
        attrs.defaultUnCheckedWorkday = ta.getDrawable(R.styleable.NCalendar_defaultUnCheckedWorkday);

        attrs.holidayWorkdayTextSize = ta.getDimension(R.styleable.NCalendar_holidayWorkdayTextSize, context.getResources().getDimension(R.dimen.N_holidayWorkdayTextSize));
        attrs.holidayWorkdayTextBold = ta.getBoolean(R.styleable.NCalendar_holidayWorkdayTextBold, context.getResources().getBoolean(R.bool.N_textBold));
        attrs.holidayWorkdayDistance = ta.getDimension(R.styleable.NCalendar_holidayWorkdayDistance, context.getResources().getDimension(R.dimen.N_holidayWorkdayDistance));
        attrs.holidayWorkdayLocation = ta.getInt(R.styleable.NCalendar_holidayWorkdayLocation, Attrs.TOP_RIGHT);
        attrs.holidayText = ta.getString(R.styleable.NCalendar_holidayText);
        attrs.workdayText = ta.getString(R.styleable.NCalendar_workdayText);
        attrs.todayCheckedHolidayTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedHolidayTextColor, ContextCompat.getColor(context, R.color.N_white));
        attrs.todayUnCheckedHolidayTextColor = ta.getColor(R.styleable.NCalendar_todayUnCheckedHolidayTextColor, ContextCompat.getColor(context, R.color.N_holidayTextColor));
        attrs.defaultCheckedHolidayTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedHolidayTextColor, ContextCompat.getColor(context, R.color.N_holidayTextColor));
        attrs.defaultUnCheckedHolidayTextColor = ta.getColor(R.styleable.NCalendar_defaultUnCheckedHolidayTextColor, ContextCompat.getColor(context, R.color.N_holidayTextColor));
        attrs.todayCheckedWorkdayTextColor = ta.getColor(R.styleable.NCalendar_todayCheckedWorkdayTextColor, ContextCompat.getColor(context, R.color.N_white));
        attrs.todayUnCheckedWorkdayTextColor = ta.getColor(R.styleable.NCalendar_todayUnCheckedWorkdayTextColor, ContextCompat.getColor(context, R.color.N_workdayTextColor));
        attrs.defaultCheckedWorkdayTextColor = ta.getColor(R.styleable.NCalendar_defaultCheckedWorkdayTextColor, ContextCompat.getColor(context, R.color.N_workdayTextColor));
        attrs.defaultUnCheckedWorkdayTextColor = ta.getColor(R.styleable.NCalendar_defaultUnCheckedWorkdayTextColor, ContextCompat.getColor(context, R.color.N_workdayTextColor));

        attrs.showNumberBackground = ta.getBoolean(R.styleable.NCalendar_showNumberBackground, context.getResources().getBoolean(R.bool.N_showNumberBackground));
        attrs.numberBackgroundTextSize = ta.getDimension(R.styleable.NCalendar_numberBackgroundTextSize, context.getResources().getDimension(R.dimen.N_numberBackgroundTextSize));
        attrs.numberBackgroundTextColor = ta.getColor(R.styleable.NCalendar_numberBackgroundTextColor, ContextCompat.getColor(context, R.color.N_todaySolarUnCheckedTextColor));
        attrs.numberBackgroundAlphaColor = ta.getInt(R.styleable.NCalendar_numberBackgroundAlphaColor, context.getResources().getInteger(R.integer.N_numberBackgroundAlphaColor));

        attrs.firstDayOfWeek = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, Attrs.SUNDAY);
        attrs.allMonthSixLine = ta.getBoolean(R.styleable.NCalendar_allMonthSixLine, context.getResources().getBoolean(R.bool.N_allMonthSixLine));
        attrs.lastNextMonthClickEnable = ta.getBoolean(R.styleable.NCalendar_lastNextMonthClickEnable, context.getResources().getBoolean(R.bool.N_lastNextMonthClickEnable));
        attrs.calendarBackground = ta.getDrawable(R.styleable.NCalendar_calendarBackground);
        attrs.lastNextMothAlphaColor = ta.getInt(R.styleable.NCalendar_lastNextMothAlphaColor, context.getResources().getInteger(R.integer.N_lastNextMothAlphaColor));
        attrs.disabledAlphaColor = ta.getInt(R.styleable.NCalendar_disabledAlphaColor, context.getResources().getInteger(R.integer.N_disabledAlphaColor));
        attrs.disabledString = ta.getString(R.styleable.NCalendar_disabledString);

        attrs.defaultCalendar = ta.getInt(R.styleable.NCalendar_defaultCalendar, CalendarState.MONTH.getValue());
        attrs.calendarHeight = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, context.getResources().getDimension(R.dimen.N_calendarHeight));
        attrs.animationDuration = ta.getInt(R.styleable.NCalendar_animationDuration, context.getResources().getInteger(R.integer.N_animationDuration));

        attrs.stretchCalendarEnable =ta.getBoolean(R.styleable.NCalendar_stretchCalendarEnable, context.getResources().getBoolean(R.bool.N_stretchCalendarEnable));
        attrs.stretchCalendarHeight = (int) ta.getDimension(R.styleable.NCalendar_stretchCalendarHeight, context.getResources().getDimension(R.dimen.N_stretchCalendarHeight));
        attrs.stretchTextSize = ta.getDimension(R.styleable.NCalendar_stretchTextSize, context.getResources().getDimension(R.dimen.N_stretchTextSize));
        attrs.stretchTextBold = ta.getBoolean(R.styleable.NCalendar_stretchTextBold, context.getResources().getBoolean(R.bool.N_textBold));
        attrs.stretchTextColor = ta.getColor(R.styleable.NCalendar_stretchTextColor, ContextCompat.getColor(context, R.color.N_stretchTextColor));
        attrs.stretchTextDistance = ta.getDimension(R.styleable.NCalendar_stretchTextDistance, context.getResources().getDimension(R.dimen.N_stretchTextDistance));

        ta.recycle();
        return attrs;

    }

}
