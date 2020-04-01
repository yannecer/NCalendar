package com.necer.enumeration;

import java.io.Serializable;

/**
 * 日历状态
 *
 * @author necer
 */
public enum CalendarState implements Serializable {

    /**
     * 折叠日历周状态
     */
    WEEK(100),

    /**
     * 折叠日历月状态
     */
    MONTH(101),

    /**
     * 折叠日历月拉伸状态
     */
    MONTH_STRETCH(102);

    private int value;

    CalendarState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CalendarState valueOf(int value) {
        switch (value) {
            case 100:
                return CalendarState.WEEK;
            case 101:
                return CalendarState.MONTH;
            case 102:
                return CalendarState.MONTH_STRETCH;
            default:
                return null;
        }
    }

}
