package com.necer.enumeration;

import java.io.Serializable;

/**
 * 日历构造类型 绘制draw 适配器 adapter
 * @author necer
 */
public enum CalendarBuild implements Serializable {

    /**
     * 绘制 -> CalendarView
     */
    DRAW,

    /**
     * 适配器 -> CalendarView2
     */
    ADAPTER
}
