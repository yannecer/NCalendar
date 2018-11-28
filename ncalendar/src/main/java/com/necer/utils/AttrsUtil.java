package com.necer.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.necer.R;

/**
 * Created by necer on 2018/11/28.
 */
public class AttrsUtil {


    public static Attrs getAttrs(Context context,AttributeSet attributeSet) {
        Attrs attrs = new Attrs();

        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.NCalendar);

        attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, context.getResources().getColor(R.color.solarTextColor));
        attrs.todaySolarTextColor = ta.getColor(R.styleable.NCalendar_todaySolarTextColor, context.getResources().getColor(R.color.todaySolarTextColor));
        attrs.lunarTextColor = ta.getColor(R.styleable.NCalendar_lunarTextColor, context.getResources().getColor(R.color.lunarTextColor));
        attrs.solarHolidayTextColor = ta.getColor(R.styleable.NCalendar_solarHolidayTextColor, context.getResources().getColor(R.color.solarHolidayTextColor));
        attrs.lunarHolidayTextColor = ta.getColor(R.styleable.NCalendar_lunarHolidayTextColor, context.getResources().getColor(R.color.lunarHolidayTextColor));
        attrs.solarTermTextColor = ta.getColor(R.styleable.NCalendar_solarTermTextColor, context.getResources().getColor(R.color.solarTermTextColor));

        attrs.selectCircleColor = ta.getColor(R.styleable.NCalendar_selectCircleColor, context.getResources().getColor(R.color.selectCircleColor));
        attrs.hintColor = ta.getColor(R.styleable.NCalendar_hintColor, context.getResources().getColor(R.color.hintColor));
        attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, Util.sp2px(context, 18));
        attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, Util.sp2px(context, 10));
        attrs.lunarDistance = ta.getDimension(R.styleable.NCalendar_lunarDistance, Util.sp2px(context, 15));
        attrs.holidayDistance = ta.getDimension(R.styleable.NCalendar_holidayDistance, Util.sp2px(context, 15));
        attrs.holidayTextSize = ta.getDimension(R.styleable.NCalendar_holidayTextSize, Util.sp2px(context, 10));
        attrs.selectCircleRadius = ta.getDimension(R.styleable.NCalendar_selectCircleRadius, Util.dp2px(context, 22));
        attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, true);
        attrs.isDefaultSelect = ta.getBoolean(R.styleable.NCalendar_isDefaultSelect, true);
        attrs.pointSize = ta.getDimension(R.styleable.NCalendar_pointSize, Util.dp2px(context, 2));
        attrs.pointDistance = ta.getDimension(R.styleable.NCalendar_pointDistance, Util.dp2px(context, 12));
        attrs.pointColor = ta.getColor(R.styleable.NCalendar_pointColor, context.getResources().getColor(R.color.pointColor));
        attrs.hollowCircleColor = ta.getColor(R.styleable.NCalendar_hollowCircleColor, context.getResources().getColor(R.color.hollowCircleColor));
        attrs.hollowCircleStroke = ta.getDimension(R.styleable.NCalendar_hollowCircleStroke, Util.dp2px(context, 1));
        attrs.monthCalendarHeight = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, Util.dp2px(context, 300));
        attrs.duration = ta.getInt(R.styleable.NCalendar_duration, 240);
        attrs.isShowHoliday = ta.getBoolean(R.styleable.NCalendar_isShowHoliday, true);
        attrs.isWeekHold = ta.getBoolean(R.styleable.NCalendar_isWeekHold, false);
        attrs.holidayColor = ta.getColor(R.styleable.NCalendar_holidayColor, context.getResources().getColor(R.color.holidayColor));
        attrs.workdayColor = ta.getColor(R.styleable.NCalendar_workdayColor, context.getResources().getColor(R.color.workdayColor));
        attrs.bgCalendarColor = ta.getColor(R.styleable.NCalendar_bgCalendarColor, context.getResources().getColor(R.color.white));
        attrs.bgChildColor = ta.getColor(R.styleable.NCalendar_bgChildColor, context.getResources().getColor(R.color.white));
        attrs.firstDayOfWeek = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, Attrs.SUNDAY);
        attrs.pointLocation = ta.getInt(R.styleable.NCalendar_pointLocation, Attrs.UP);
        attrs.defaultCalendar = ta.getInt(R.styleable.NCalendar_defaultCalendar, Attrs.MONTH);
        attrs.holidayLocation = ta.getInt(R.styleable.NCalendar_holidayLocation, Attrs.TOP_RIGHT);
/*

        String startString = ta.getString(R.styleable.NCalendar_startDate);
        String endString = ta.getString(R.styleable.NCalendar_endDate);
*/


        ta.recycle();

        return attrs;

    }

}
