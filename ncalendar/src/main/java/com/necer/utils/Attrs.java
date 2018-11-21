package com.necer.utils;

/**
 * Created by necer on 2017/6/13.
 */

public class Attrs {

    //日历默认视图
    public static final int WEEK = 100;//周视图
    public static final int MONTH = 101;//月视图

    //指示圆点的位置
    public static final int UP = 200;//再公历日期上面
    public static final int DOWN = 201;//再公历日期下面

    //周的第一天
    public static final int SUNDAY = 300;//周的第一天 周日
    public static final int MONDAY = 301;//周的第一天 周一


    //节假日的位置
    public static final int TOP_RIGHT = 400; //右上方
    public static final int TOP_LEFT = 401; //左上方
    public static final int BOTTOM_RIGHT = 402;//右下方
    public static final int BOTTOM_LEFT = 403;//左下方



    public int solarTextColor;
    public int todaySolarTextColor;
    public int lunarTextColor;
    public int solarHolidayTextColor;
    public int lunarHolidayTextColor;
    public int solarTermTextColor;
    public int hintColor;
    public int selectCircleColor;
    public float solarTextSize;
    public float lunarTextSize;
    public float lunarDistance;//农历到文字中心的距离
    public float selectCircleRadius;
    public boolean isShowLunar;


    public float pointSize;
    public float pointDistance;//圆点到文字中心的距离
    public int pointColor;
    public int pointLocation; //0 在上面 1在下面
    public int hollowCircleColor;
    public float hollowCircleStroke;

    public int firstDayOfWeek;
    public int defaultCalendar;

    public int monthCalendarHeight;
    public int duration;

    public boolean isWeekHold;
    public boolean isShowHoliday;
    public int holidayColor;
    public float holidayTextSize;
    public float holidayDistance;
    public int holidayLocation;
    public int workdayColor;
    public int bgCalendarColor;//日历的背景
    public int bgChildColor;//子view的背景
    public boolean isDefaultSelect;//是否默认选中


}
