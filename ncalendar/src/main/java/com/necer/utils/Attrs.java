package com.necer.utils;

/**
 * Created by necer on 2017/6/13.
 */

public class Attrs {


    //指示圆点的位置
    public static final int UP = 200;//在公历日期上面
    public static final int DOWN = 201;//在公历日期下面

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
    public int todaySolarSelectTextColor;
    public int lunarTextColor;
    public int solarHolidayTextColor;
    public int lunarHolidayTextColor;
    public int solarTermTextColor;
    public int selectCircleColor;
    public int selectSolarTextColorColor;//选中公历颜色
    public int selectLunarTextColor;//选中农历颜色
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

    public int calendarHeight;//正常日历的高度
    public int stretchCalendarHeight;//拉伸后日历的高度
    public int duration;

    public boolean isShowHoliday;
    public int holidayColor;
    public float holidayTextSize;
    public float holidayDistance;
    public int holidayLocation;
    public int workdayColor;

    public int alphaColor;//不在同一月的颜色透明度
    public int disabledAlphaColor;//不可用的日期颜色透明度
    public String disabledString;//点击不可用的日期提示语


    public int todaySelectContrastColor;//当天被选中的对比色，选中当前的农历，原定等颜色
    public int bgCalendarColor;//日历背景

    public float stretchTextSize; //拉伸显示的字体大小
    public int stretchTextColor; //拉伸显示的字体颜色
    public float stretchTextDistance; //拉伸显示的字体距离矩形中心的距离

    public boolean isAllMonthSixLine;//月是否都6行


    public boolean isShowNumberBackground; //是否显示数字背景
    public float numberBackgroundTextSize; //数字背景字体大小
    public int numberBackgroundTextColor; //数字背景字体颜色
    public int numberBackgroundAlphaColor;//数字背景字体透明度


}
