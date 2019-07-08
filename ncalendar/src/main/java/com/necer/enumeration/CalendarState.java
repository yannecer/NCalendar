package com.necer.enumeration;

/**
 * 日历状态
 */
public enum CalendarState {

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
