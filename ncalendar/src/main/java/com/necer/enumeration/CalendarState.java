package com.necer.enumeration;

import java.io.Serializable;

/**
 * 日历状态
 */
public enum CalendarState implements Serializable {

    WEEK(100), MONTH(101);

    private int value;

    CalendarState(int value) {
        this.value = value;
    }

    public static CalendarState valueOf(int value) {
        switch (value) {
            case 100:
                return CalendarState.WEEK;
            case 101:
                return CalendarState.MONTH;
            default:
                return null;
        }
    }

}
