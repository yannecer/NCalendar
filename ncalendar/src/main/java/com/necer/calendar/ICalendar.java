package com.necer.calendar;

import com.necer.enumeration.MultipleNumModel;
import com.necer.enumeration.CheckModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.painter.CalendarAdapter;
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
     * @param checkModel SINGLE_DEFAULT_CHECKED-单个 默认每页选中  默认模式 每页都会有一个选中
     *                   SINGLE_DEFAULT_UNCHECKED-单个 默认不选中 点击、跳转选中
     *                   MULTIPLE-多选
     */
    void setCheckMode(CheckModel checkModel);

    /**
     * 多选个数和模式
     *
     * @param multipleNum      多选个数
     * @param multipleNumModel FULL_CLEAR-超过清除所有
     *                         FULL_REMOVE_FIRST-超过清除第一个
     */
    void setMultipleNum(int multipleNum, MultipleNumModel multipleNumModel);


    /**
     * 默认选中时，是否翻页选中第一个，只在checkModel==SINGLE_DEFAULT_CHECKED有效
     *
     * @param isDefaultCheckedFirstDate
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
     *
     * @param year
     * @param month
     * @param day
     */
    void jumpDate(int year, int month, int day);

    /**
     * 跳转日期，跳转到对应月份
     *
     * @param year
     * @param month
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
     * @param calendarPainter 实现CalendarPainter接口，自定义绘制
     */
    void setCalendarPainter(CalendarPainter calendarPainter);

    /**
     * 设置自定义适配器 继承CalendarAdapter，实现对应方法，自定义
     *
     * @param calendarAdapter
     */
    void setCalendarAdapter(CalendarAdapter calendarAdapter);

    /**
     * 刷新日历
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
     * @param startFormatDate
     * @param endFormatDate
     * @param formatInitializeDate
     */
    void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate);

    /**
     * 设置可用区间 必须为 yyyy-MM-dd 的字符串
     *
     * @param startFormatDate
     * @param endFormatDate
     */
    void setDateInterval(String startFormatDate, String endFormatDate);

    /**
     * 单选日期变化监听
     *
     * @param onCalendarChangedListener
     */
    void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener);

    /**
     * 多选日期变化监听
     *
     * @param onCalendarMultipleChangedListener
     */
    void setOnCalendarMultipleChangedListener(OnCalendarMultipleChangedListener onCalendarMultipleChangedListener);

    /**
     * 设置点击了不可用日期监听
     *
     * @param onClickDisableDateListener
     */
    void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener);

    /**
     * 获取xml参数
     *
     * @return
     */
    Attrs getAttrs();

    /**
     * 获取绘制类
     *
     * @return
     */
    CalendarPainter getCalendarPainter();

    /**
     * 获取适配器
     *
     * @return
     */
    CalendarAdapter getCalendarAdapter();

    /**
     * 获取全部选中的日期集合
     *
     * @return
     */
    List<LocalDate> getAllCheckedDateList();

    /**
     * 获取当前页面选中的日期集合
     *
     * @return
     */
    List<LocalDate> getCurrPagerCheckDateList();

    /**
     * 获取当前页面的日期集合 如果是月周折叠日历 周状态下获取的是一周的数据，月状态下获取的一月的数据
     *
     * @return
     */
    List<LocalDate> getCurrPagerDateList();

    /**
     * 月周折叠日历滑动过程中的滑动距离
     *
     * @param currentDistance
     */
    void updateSlideDistance(int currentDistance);

    /**
     * 设置日历上下月能否点击
     *
     * @param enable
     */
    void setLastNextMonthClickEnable(boolean enable);


    /**
     * 设置日历是否可以左右滑动
     * @param scrollEnable
     */
    void setScrollEnable(boolean scrollEnable);

}
