package com.necer.calendar;

import com.necer.enumeration.MultipleCountModel;
import com.necer.enumeration.CheckModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.painter.CalendarAdapter;
import com.necer.painter.CalendarBackground;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * 日历功能接口 包含 月日历、周日历、折叠日历
 */
public interface ICalendar {

    /**
     * 设置选中模式
     *
     * @param checkModel SINGLE_DEFAULT_CHECKED-单选 默认每页选中  默认模式 每页都会有一个选中
     *                   SINGLE_DEFAULT_UNCHECKED-单选 默认不选中 点击、跳转选中
     *                   MULTIPLE-多选
     */
    void setCheckMode(CheckModel checkModel);

    /**
     * 获取日历的选中模式 折叠日历中用到
     */
    CheckModel getCheckModel();

    /**
     * 多选个数和模式
     *
     * @param multipleCount      多选个数
     * @param multipleCountModel FULL_CLEAR-超过清除所有
     *                           FULL_REMOVE_FIRST-超过清除第一个
     */
    void setMultipleCount(int multipleCount, MultipleCountModel multipleCountModel);


    /**
     * 默认选中时，是否翻页选中第一个，只在checkModel==SINGLE_DEFAULT_CHECKED有效
     */
    void setDefaultCheckedFirstDate(boolean isDefaultCheckedFirstDate);


    /**
     * 跳转日期
     *
     * @param formatDate 必须为 yyyy-MM-dd 的字符串
     */
    void jumpDate(String formatDate);

    /**
     * 跳转日期
     */
    void jumpDate(int year, int month, int day);

    /**
     * 跳转日期，跳转到对应月份
     */
    void jumpMonth(int year, int month);


    /**
     * 上一页 上一周 上一月
     */
    void toLastPager();

    /**
     * 下一页 下一周 下一月
     */
    void toNextPager();

    /**
     * 回到今天
     */
    void toToday();

    /**
     * 设置自定义绘制类
     *
     * @param calendarPainter 实现CalendarPainter接口
     */
    void setCalendarPainter(CalendarPainter calendarPainter);

    /**
     * 设置自定义适配器 继承CalendarAdapter，实现对应方法，自定义
     *
     * @param calendarAdapter 继承抽象类CalendarAdapter
     */
    void setCalendarAdapter(CalendarAdapter calendarAdapter);

    /**
     * 刷新日历 刷新viewpager中存在的view
     */
    void notifyCalendar();

    /**
     * 设置初始化日期
     *
     * @param formatInitializeDate 必须为 yyyy-MM-dd 的字符串
     */
    void setInitializeDate(String formatInitializeDate);

    /**
     * 设置初始化日期和可用区间  必须为 yyyy-MM-dd 的字符串
     *
     * @param startFormatDate      日历开始日期 默认1901-01-01
     * @param endFormatDate        日历结束日期 默认2099-12-31
     * @param formatInitializeDate 初始化选日期
     */
    void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate);

    /**
     * 设置可用区间 必须为 yyyy-MM-dd 的字符串
     *
     * @param startFormatDate 日历开始日期 默认1901-01-01
     * @param endFormatDate   日历结束日期 默认2099-12-31
     */
    void setDateInterval(String startFormatDate, String endFormatDate);

    /**
     * 单选日期变化监听
     */
    void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener);

    /**
     * 多选日期变化监听
     */
    void setOnCalendarMultipleChangedListener(OnCalendarMultipleChangedListener onCalendarMultipleChangedListener);

    /**
     * 设置点击了不可用日期监听
     */
    void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener);

    /**
     * 获取xml参数
     */
    Attrs getAttrs();

    /**
     * 获取绘制类
     */
    CalendarPainter getCalendarPainter();

    /**
     * 获取适配器
     */
    CalendarAdapter getCalendarAdapter();

    /**
     * 获取全部选中的日期集合
     */
    List<LocalDate> getTotalCheckedDateList();

    /**
     * 获取当前页面选中的日期集合
     */
    List<LocalDate> getCurrPagerCheckDateList();

    /**
     * 获取当前页面的日期集合 如果是月周折叠日历 周状态下获取的是一周的数据，月状态下获取的一月的数据
     */
    List<LocalDate> getCurrPagerDateList();

    /**
     * 月周折叠日历滑动过程中的滑动距离
     *
     * @param currentDistance 当前滑动的距离
     */
    void updateSlideDistance(int currentDistance);

    /**
     * 设置日历上下月能否点击
     */
    void setLastNextMonthClickEnable(boolean enable);


    /**
     * 设置日历是否可以左右滑动
     */
    void setScrollEnable(boolean scrollEnable);


    /**
     * @param calendarBackground 实现了CalendarBackground接口的背景
     * @throws IllegalAccessException 月周折叠日历不允许使用此方法
     *                                折叠日历请调用setMonthCalendarBackground()和setWeekCalendarBackground()
     */
    void setCalendarBackground(CalendarBackground calendarBackground) throws IllegalAccessException;

    /**
     * @return 获取日历的背景
     * @throws IllegalAccessException 月周折叠日历不允许使用此方法
     */
    CalendarBackground getCalendarBackground() throws IllegalAccessException;


    /**
     * 多选模式下，初始化时选中的日期
     *
     * @param dateList 日期几何 yyyy-MM-dd
     */
    void setCheckedDates(List<String> dateList);


}
