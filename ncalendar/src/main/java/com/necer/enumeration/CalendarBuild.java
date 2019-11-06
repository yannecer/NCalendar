package com.necer.enumeration;

import java.io.Serializable;

/**
 * 日历构造类型 绘制draw 适配器 adapter
 */
public enum CalendarBuild  implements Serializable {

    DRAW, //绘制 -> CalendarView
    ADAPTER //适配器 -> CalendarView2
}
