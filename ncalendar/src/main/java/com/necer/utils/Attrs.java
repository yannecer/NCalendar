package com.necer.utils;

import android.graphics.drawable.Drawable;

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


    //公立日期属性
    public int todayCheckedSolarTextColor;//当天选中的公历字体颜色
    public int todayUnCheckedSolarTextColor;//当天不选中的公历字体颜色
    public int defaultCheckedSolarTextColor; //默认选中的公历字体颜色
    public int defaultUnCheckedSolarTextColor;//默认不选中公历字体颜色
    public float solarTextSize; //公历字体大小


    //标记日期属性
    public int todayCheckedPoint;//当天选中标记
    public int todayUnCheckedPoint;//当天不选中标记
    public int defaultCheckedPoint;//默认选中标记
    public int defaultUnCheckedPoint;//默认不选中标记
    public int pointLocation; //0 在上面 1在下面
    public float pointDistance;//圆点到文字中心的距离


    //节假日日期属性  drawable
    public Drawable todayCheckedHoliday; //当天选中节假日
    public Drawable todayUnCheckedHoliday;//当天不选中节假日
    public Drawable defaultCheckedHoliday;//默认选中节假日
    public Drawable defaultUnCheckedHoliday;//默认不选中节假日
    public Drawable todayCheckedWorkday; //当天选中工作日
    public Drawable todayUnCheckedWorkday;//当天不选中工作日
    public Drawable defaultCheckedWorkday;//默认选中工作日
    public Drawable defaultUnCheckedWorkday;//默认不选中工作日

    //节假日日期属性  text
    public boolean isShowHoliday; //是否显示节假日和工作日标记
    public String holidayText;//节假日文字
    public String workdayText;//工作日文字
    public float holidayWorkdayTextSize; //字体大小
    public float holidayWorkdayDistance;//文字距离中心距离
    public int holidayWorkdayLocation; //文字的位置
    public int todayCheckedHolidayTextColor;//当天选中的节假日字体颜色
    public int todayUnCheckedHolidayTextColor;//当天选中的节假日字体颜色
    public int defaultCheckedHolidayTextColor;//默认选中的节假日字体颜色
    public int defaultUnCheckedHolidayTextColor;//默认选中的节假日字体颜色
    public int todayCheckedWorkdayTextColor;//当天选中的工作日字体颜色
    public int todayUnCheckedWorkdayTextColor;//当天选中的工作日颜色
    public int defaultCheckedWorkdayTextColor;//默认选中的工作日字体颜色
    public int defaultUnCheckedWorkdayTextColor;//默认选中的工作日字体颜色

    //农历属性
    public boolean isShowLunar;//是否显示农历
    public int todayCheckedLunarTextColor;//当天选中农历颜色
    public int todayUnCheckedLunarTextColor;//当天不选中农历颜色
    public int defaultCheckedLunarTextColor;//默认选中农历颜色
    public int defaultUnCheckedLunarTextColor;//默认不选中农历颜色
    public float lunarTextSize;
    public float lunarDistance;//农历到文字中心的距离


    //上下月
    public int lastNextMothAlphaColor;//上下月月的颜色透明度


    public int firstDayOfWeek;//日历一周开始是周日或周一

    //折叠日历相关
    public int defaultCalendar; //折叠日历的默认展示日历
    public int calendarHeight;//折叠日历的高度
    public int stretchCalendarHeight;//拉伸后日历的高度
    public int animationDuration; //折叠日历动画时间



    public int disabledAlphaColor;//不可用的日期颜色透明度
    public String disabledString;//点击不可用的日期提示语


    public int bgCalendarColor;//日历背景

    public float stretchTextSize; //拉伸显示的字体大小
    public int stretchTextColor; //拉伸显示的字体颜色
    public float stretchTextDistance; //拉伸显示的字体距离矩形中心的距离

    public boolean isAllMonthSixLine;//月是否都6行


    public boolean isShowNumberBackground; //是否显示数字背景
    public float numberBackgroundTextSize; //数字背景字体大小
    public int numberBackgroundTextColor; //数字背景字体颜色
    public int numberBackgroundAlphaColor;//数字背景字体透明度

    public boolean isLastNextMonthClickEnable;//月日历上下月是否可点击

    public int todayCheckedBackground;//选中当天的checkedBackground
    public int defaultCheckedBackground;//选中其他日期的checkedBackground


}
