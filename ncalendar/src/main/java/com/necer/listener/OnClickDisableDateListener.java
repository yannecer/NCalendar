package com.necer.listener;

import org.joda.time.LocalDate;

/**
 * 点击不可用的日期对调
 *
 * @author necer
 * @date 2018/12/20
 */
public interface OnClickDisableDateListener {

    /**
     * 点击不可用的日期对调 ，和日历设置区间方法（setDateInterval）对应
     * 日历内部处理了点击 Toast ，也可以通过自定义属性设置 Toast 内容
     * 设置了这个监听方法则内部的 Toast 不可用了
     * @param localDate
     */
    void onClickDisableDate(LocalDate localDate);
}
