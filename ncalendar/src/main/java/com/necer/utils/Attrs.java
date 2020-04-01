package com.necer.utils;

import android.graphics.drawable.Drawable;

/**
 * @author necer
 * @date 2017/6/13
 */

public class Attrs {


    /**
     * 今天选中的背景 shape
     */
    public int todayCheckedBackground;
    /**
     * 其他日期选中的背景 shape
     */
    public int defaultCheckedBackground;


    /**
     * 指示圆点的位置
     * 在公历日期上面
     */
    public static final int UP = 200;
    /**
     * 指示圆点的位置
     * 在公历日期下面
     */
    public static final int DOWN = 201;

    /**
     * 周的第一天
     * 周日
     */
    public static final int SUNDAY = 300;
    /**
     * 周的第一天
     * 周一
     */
    public static final int MONDAY = 301;


    /**
     * 节假日的位置
     * 右上方
     */
    public static final int TOP_RIGHT = 400;
    /**
     * 节假日的位置
     * 左上方
     */
    public static final int TOP_LEFT = 401;
    /**
     * 节假日的位置
     * 右下方
     */
    public static final int BOTTOM_RIGHT = 402;
    /**
     * 节假日的位置
     * 左下方
     */
    public static final int BOTTOM_LEFT = 403;


    /**
     * 公历日期属性
     * 今天选中的公历字体颜色
     */
    public int todayCheckedSolarTextColor;
    /**
     * 公历日期属性
     * 今天不选中的公历字体颜色
     */
    public int todayUnCheckedSolarTextColor;
    /**
     * 公历日期属性
     * 默认选中的公历字体颜色
     */
    public int defaultCheckedSolarTextColor;
    /**
     * 公历日期属性
     * 默认不选中公历字体颜色
     */
    public int defaultUnCheckedSolarTextColor;
    /**
     * 公历日期属性
     * 公历字体大小
     */
    public float solarTextSize;

    /**
     * 公历日期属性
     * 公历字体是否加粗
     */
    public boolean solarTextBold;

    /**
     * 标记日期属性
     * 今天选中时标记
     */
    public int todayCheckedPoint;
    /**
     * 标记日期属性
     * 今天不选中时标记
     */
    public int todayUnCheckedPoint;
    /**
     * 标记日期属性
     * 默认选中时标记
     */
    public int defaultCheckedPoint;
    /**
     * 标记日期属性
     * 默认不选中时标记
     */
    public int defaultUnCheckedPoint;
    /**
     * 标记日期属性
     * 0 在上面 1在下面
     */
    public int pointLocation;
    /**
     * 标记日期属性
     * 标记点到文字中心的距离
     */
    public float pointDistance;


    /**
     * 节假日日期属性  drawable
     * 今天选中时节假日
     */
    public Drawable todayCheckedHoliday;
    /**
     * 节假日日期属性  drawable
     * 今天不选中时节假日
     */
    public Drawable todayUnCheckedHoliday;
    /**
     * 节假日日期属性  drawable
     * 默认选中时节假日
     */
    public Drawable defaultCheckedHoliday;
    /**
     * 节假日日期属性  drawable
     * 默认不选中时节假日
     */
    public Drawable defaultUnCheckedHoliday;
    /**
     * 节假日日期属性  drawable
     * 今天选中时工作日
     */
    public Drawable todayCheckedWorkday;
    /**
     * 节假日日期属性  drawable
     * 今天不选中工作日
     */
    public Drawable todayUnCheckedWorkday;
    /**
     * 节假日日期属性  drawable
     * 默认选中时工作日
     */
    public Drawable defaultCheckedWorkday;
    /**
     * 节假日日期属性  drawable
     * 默认不选中时工作日
     */
    public Drawable defaultUnCheckedWorkday;


    /**
     * 节假日日期属性  text
     * 是否显示节假日和工作日标记
     */
    public boolean showHolidayWorkday;
    /**
     * 节假日日期属性  text
     * 节假日文字
     */
    public String holidayText;
    /**
     * 节假日日期属性  text
     * 工作日文字
     */
    public String workdayText;
    /**
     * 节假日日期属性  text
     * 字体大小
     */
    public float holidayWorkdayTextSize;


    /**
     * 节假日日期属性  text
     * 是否加粗
     */
    public boolean holidayWorkdayTextBold;
    /**
     * 节假日日期属性  text
     * 文字距离中心距离
     */
    public float holidayWorkdayDistance;
    /**
     * 节假日日期属性  text
     * 文字的位置
     */
    public int holidayWorkdayLocation;
    /**
     * 节假日日期属性  text
     * 今天选中时节假日字体颜色
     */
    public int todayCheckedHolidayTextColor;
    /**
     * 节假日日期属性  text
     * 今天未选中时节假日字体颜色
     */
    public int todayUnCheckedHolidayTextColor;
    /**
     * 节假日日期属性  text
     * 默认选中时节假日字体颜色
     */
    public int defaultCheckedHolidayTextColor;
    /**
     * 节假日日期属性  text
     * 默认未选中时节假日字体颜色
     */
    public int defaultUnCheckedHolidayTextColor;
    /**
     * 节假日日期属性  text
     * 今天选中时工作日字体颜色
     */
    public int todayCheckedWorkdayTextColor;
    /**
     * 节假日日期属性  text
     * 今天选中时工作日颜色
     */
    public int todayUnCheckedWorkdayTextColor;
    /**
     * 节假日日期属性  text
     * 默认选中时工作日字体颜色
     */
    public int defaultCheckedWorkdayTextColor;
    /**
     * 节假日日期属性  text
     * 默认选中时工作日字体颜色
     */
    public int defaultUnCheckedWorkdayTextColor;


    /**
     * 农历属性
     * 是否显示农历
     */
    public boolean showLunar;
    /**
     * 农历属性
     * 今天选中时农历颜色
     */
    public int todayCheckedLunarTextColor;
    /**
     * 农历属性
     * 今天不选中时农历颜色
     */
    public int todayUnCheckedLunarTextColor;
    /**
     * 农历属性
     * 默认选中时农历颜色
     */
    public int defaultCheckedLunarTextColor;
    /**
     * 农历属性
     * 默认不选中时农历颜色
     */
    public int defaultUnCheckedLunarTextColor;
    /**
     * 农历属性
     * 农历字体大小
     */
    public float lunarTextSize;
    /**
     * 农历属性
     * 农历字体是否加粗
     */
    public boolean lunarTextBold;
    /**
     * 农历属性
     * 农历文字到文字中心的距离
     */
    public float lunarDistance;


    /**
     * 上下月月的颜色透明度
     */
    public int lastNextMothAlphaColor;


    /**
     * 日历一周开始是周日或周一
     */
    public int firstDayOfWeek;


    /**
     * 折叠日历属性
     * 折叠日历的默认展示日历
     */
    public int defaultCalendar;
    /**
     * 折叠日历属性
     * 折叠日历月日历的高度
     */
    public int calendarHeight;
    /**
     * 折叠日历属性
     * 是否可拉伸
     */
    public boolean stretchCalendarEnable;
    /**
     * 折叠日历属性
     * 拉伸后日历的高度
     */
    public int stretchCalendarHeight;
    /**
     * 折叠日历属性
     * 折叠日历动画时间
     */
    public int animationDuration;


    /**
     * 不可用的日期颜色透明度
     */
    public int disabledAlphaColor;
    /**
     * 点击不可用的日期提示语
     */
    public String disabledString;


    /**
     * 拉伸显示的字体大小
     */
    public float stretchTextSize;

    /**
     * 拉伸字体加粗
     */
    public boolean stretchTextBold;

    /**
     * 拉伸显示的字体颜色
     */
    public int stretchTextColor;
    /**
     * 拉伸显示的字体距离矩形中心的距离
     */
    public float stretchTextDistance;

    /**
     * 月日历是否全部6行
     */
    public boolean allMonthSixLine;


    /**
     * 是否显示数字背景
     */
    public boolean showNumberBackground;
    /**
     * 数字背景字体大小
     */
    public float numberBackgroundTextSize;
    /**
     * 数字背景字体颜色
     */
    public int numberBackgroundTextColor;
    /**
     * 数字背景字体透明度
     */
    public int numberBackgroundAlphaColor;

    /**
     * 月日历上下月是否可点击
     */
    public boolean lastNextMonthClickEnable;


    /**
     * 日历背景
     */
    public Drawable calendarBackground;


}
