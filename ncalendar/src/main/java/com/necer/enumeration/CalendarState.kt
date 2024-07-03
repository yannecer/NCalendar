package com.necer.enumeration

import java.io.Serializable

/**
 * 日历状态
 *
 * @author necer
 */
enum class CalendarState(private val value: Int) : Serializable {
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

    open fun getValue(): Int {
        return value
    }

    companion object {
        fun valueOf(value: Int): CalendarState? {
            return when (value) {
                100 -> WEEK
                101 -> MONTH
                102 -> MONTH_STRETCH
                else -> null
            }
        }
    }
}