package com.necer.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
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

        attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, context.getResources().getColor(R.color.solarTextColor));
        attrs.selectSolarTextColorColor = ta.getColor(R.styleable.NCalendar_selectSolarTextColorColor, context.getResources().getColor(R.color.solarTextColor));
        attrs.todaySolarTextColor = ta.getColor(R.styleable.NCalendar_todaySolarTextColor, context.getResources().getColor(R.color.todaySolarTextColor));
        attrs.todaySolarSelectTextColor = ta.getColor(R.styleable.NCalendar_todaySolarSelectTextColor, context.getResources().getColor(R.color.white));
        attrs.lunarTextColor = ta.getColor(R.styleable.NCalendar_lunarTextColor, context.getResources().getColor(R.color.lunarTextColor));
        attrs.selectLunarTextColor = ta.getColor(R.styleable.NCalendar_selectLunarTextColor, context.getResources().getColor(R.color.lunarTextColor));
        attrs.solarHolidayTextColor = ta.getColor(R.styleable.NCalendar_solarHolidayTextColor, context.getResources().getColor(R.color.solarHolidayTextColor));
        attrs.lunarHolidayTextColor = ta.getColor(R.styleable.NCalendar_lunarHolidayTextColor, context.getResources().getColor(R.color.lunarHolidayTextColor));
        attrs.solarTermTextColor = ta.getColor(R.styleable.NCalendar_solarTermTextColor, context.getResources().getColor(R.color.solarTermTextColor));

        attrs.selectCircleColor = ta.getColor(R.styleable.NCalendar_selectCircleColor, context.getResources().getColor(R.color.selectCircleColor));
        attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, CalendarUtil.sp2px(context, 18));
        attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, CalendarUtil.sp2px(context, 10));
        attrs.lunarDistance = ta.getDimension(R.styleable.NCalendar_lunarDistance, CalendarUtil.dp2px(context, 15));
        attrs.holidayDistance = ta.getDimension(R.styleable.NCalendar_holidayDistance, CalendarUtil.dp2px(context, 15));
        attrs.holidayTextSize = ta.getDimension(R.styleable.NCalendar_holidayTextSize, CalendarUtil.sp2px(context, 10));
        attrs.selectCircleRadius = ta.getDimension(R.styleable.NCalendar_selectCircleRadius, CalendarUtil.dp2px(context, 22));
        attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, true);
        attrs.pointSize = ta.getDimension(R.styleable.NCalendar_pointSize, CalendarUtil.dp2px(context, 2));
        attrs.pointDistance = ta.getDimension(R.styleable.NCalendar_pointDistance, CalendarUtil.dp2px(context, 18));
        attrs.pointColor = ta.getColor(R.styleable.NCalendar_pointColor, context.getResources().getColor(R.color.pointColor));
        attrs.hollowCircleColor = ta.getColor(R.styleable.NCalendar_hollowCircleColor, context.getResources().getColor(R.color.hollowCircleColor));
        attrs.hollowCircleStroke = ta.getDimension(R.styleable.NCalendar_hollowCircleStroke, CalendarUtil.dp2px(context, 1));
        attrs.calendarHeight = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, CalendarUtil.dp2px(context, 300));
        attrs.stretchCalendarHeight = (int) ta.getDimension(R.styleable.NCalendar_stretchCalendarHeight, CalendarUtil.dp2px(context, 450));
        attrs.duration = ta.getInt(R.styleable.NCalendar_duration, 240);
        attrs.isShowHoliday = ta.getBoolean(R.styleable.NCalendar_isShowHoliday, true);
        attrs.holidayColor = ta.getColor(R.styleable.NCalendar_holidayColor, context.getResources().getColor(R.color.holidayColor));
        attrs.workdayColor = ta.getColor(R.styleable.NCalendar_workdayColor, context.getResources().getColor(R.color.workdayColor));
        attrs.todaySelectContrastColor = ta.getColor(R.styleable.NCalendar_todaySelectContrastColor, context.getResources().getColor(R.color.white));
        attrs.bgCalendarColor = ta.getColor(R.styleable.NCalendar_bgCalendarColor, context.getResources().getColor(R.color.white));
        attrs.firstDayOfWeek = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, Attrs.SUNDAY);
        attrs.pointLocation = ta.getInt(R.styleable.NCalendar_pointLocation, Attrs.UP);
        attrs.defaultCalendar = ta.getInt(R.styleable.NCalendar_defaultCalendar, CalendarState.MONTH.getValue());
        attrs.holidayLocation = ta.getInt(R.styleable.NCalendar_holidayLocation, Attrs.TOP_RIGHT);

        attrs.alphaColor = ta.getInt(R.styleable.NCalendar_alphaColor, 90);
        attrs.disabledAlphaColor = ta.getInt(R.styleable.NCalendar_disabledAlphaColor, 50);
        attrs.disabledString = ta.getString(R.styleable.NCalendar_disabledString);

        attrs.stretchTextSize = ta.getDimension(R.styleable.NCalendar_stretchTextSize, CalendarUtil.sp2px(context, 10));
        attrs.stretchTextDistance = ta.getDimension(R.styleable.NCalendar_stretchTextDistance, CalendarUtil.dp2px(context, 32));
        attrs.stretchTextColor = ta.getColor(R.styleable.NCalendar_stretchTextColor, context.getResources().getColor(R.color.stretchTextColor));

        attrs.isAllMonthSixLine = ta.getBoolean(R.styleable.NCalendar_isAllMonthSixLine, false);
        attrs.isShowNumberBackground = ta.getBoolean(R.styleable.NCalendar_isShowNumberBackground, false);
        attrs.numberBackgroundTextSize = ta.getDimension(R.styleable.NCalendar_numberBackgroundTextSize, CalendarUtil.sp2px(context, 260));
        attrs.numberBackgroundTextColor = ta.getColor(R.styleable.NCalendar_numberBackgroundTextColor, context.getResources().getColor(R.color.todaySolarTextColor));
        attrs.numberBackgroundAlphaColor = ta.getInt(R.styleable.NCalendar_numberBackgroundAlphaColor, 50);

        ta.recycle();

        return attrs;

    }

}
