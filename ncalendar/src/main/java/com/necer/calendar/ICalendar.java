package com.necer.calendar;

import com.necer.enumeration.MultipleModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * 日历功能接口 包含 月日历、周日历、折叠日历
 */
public interface ICalendar {

    //设置默认选中
    void setDefaultSelect(boolean isDefaultSelect);

    //默认选中时，是否翻页选中第一个，前提必须默认选中
    void setDefaultSelectFitst(boolean isDefaultSelectFitst);

    //是否多选
    void setMultipleSelset(boolean isMultipleSelset);

    //多选个数和模式 FULL_CLEAR-超过清除所有  FULL_REMOVE_FIRST-超过清除第一个
    void setMultipleNum(int multipleNum, MultipleModel multipleModel);

    //跳转日期
    void jumpDate(String formatDate);

    //上一页 上一周 上一月
    void toLastPager();

    //下一页 下一周 下一月
    void toNextPager();

    //回到今天
    void toToday();

    //设置自定义绘制类
    void setCalendarPainter(CalendarPainter calendarPainter);

    //刷新日历
    void notifyCalendar();

    //设置初始化日期
    void setInitializeDate(String formatInitializeDate);

    //设置初始化日期和可用区间
    void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate);

    //设置可用区间
    void setDateInterval(String startFormatDate, String endFormatDate);

    //单选日期变化监听
    void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener);

    //多选日期变化监听
    void setOnCalendarMultipleChangedListener(OnCalendarMultipleChangedListener onCalendarMultipleChangedListener);

    //设置点击了不可用日期监听
    void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener);

    //获取xml参数
    Attrs getAttrs();

    //获取绘制类
    CalendarPainter getCalendarPainter();

    //获取全部选中的日期集合
    List<LocalDate> getAllSelectDateList();

    //获取当前页面选中的日期集合
    List<LocalDate> getCurrectSelectDateList();

}
