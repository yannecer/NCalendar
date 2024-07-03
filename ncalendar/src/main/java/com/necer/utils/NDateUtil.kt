package com.necer.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters


object NDateUtil {


    fun getMonthDate(localDate: LocalDate, weekType: Int, isAllMonthSixLine: Boolean): List<LocalDate> {
        // weekType: 301 表示周一开始,其他表示周日开始
        val firstDayOfWeek = if (weekType == NAttrs.MONDAY) DayOfWeek.MONDAY else DayOfWeek.SUNDAY

        // 获取给定日期所在月份的第一天和最后一天
        val firstDayOfMonth = localDate.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth())

        // 计算第一行开始的日期（基于weekType）
        val startDate = firstDayOfMonth.minusDays((firstDayOfMonth.dayOfWeek.value - firstDayOfWeek.value + (if (firstDayOfWeek.value > firstDayOfMonth.dayOfWeek.value) 7 else 0)).toLong())

        // 创建一个列表来保存日期
        val dates = ArrayList<LocalDate>()

        // 填充日期，直到达到所需的行数
        var currentDay = startDate
        while (dates.size < 35 ) {
            dates.add(currentDay)
            currentDay = currentDay.plusDays(1)
        }

        if (isAllMonthSixLine || dates[dates.size - 1].isBefore(lastDayOfMonth)) {
            while (dates.size < 42) {
                dates.add(currentDay)
                currentDay = currentDay.plusDays(1)
            }
        }
        return dates
    }


    fun getWeekDate(localDate: LocalDate, weekType: Int): List<LocalDate> {

        val firstDayOfWeek = if (weekType == NAttrs.MONDAY) DayOfWeek.MONDAY else DayOfWeek.SUNDAY

        // 获取该周的第一天
        val firstDayOfWeekDate =
            localDate.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))

        // 创建一个列表来保存日期
        val weekDates = ArrayList<LocalDate>()

        // 填充一周的日期
        for (dayOfWeek in 0..6) {
            weekDates.add(firstDayOfWeekDate.plusDays(dayOfWeek.toLong()))
        }

        // 返回一周的日期列表
        return weekDates
    }


    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    fun getIntervalMonths(startDate: LocalDate, endDate: LocalDate): Int {
        // 确保startDate在endDate之前
        if (startDate.isAfter(endDate)) {
            return -getIntervalMonths(endDate, startDate)
        }
       val date1  = LocalDate.of(startDate.year,startDate.month,1)
       val date2  = LocalDate.of(endDate.year,endDate.month,1)

        // 使用ChronoUnit.MONTHS来计算月份差异
        val months = ChronoUnit.MONTHS.between(date1, date2)
        return months.toInt() // 转换为int，因为月份差异应该是整数

    }


    /**
     * 获得两个日期距离几周
     *
     * @param date1
     * @param date2
     * @param type  一周
     * @return
     */
    fun getIntervalWeek(date1: LocalDate, date2: LocalDate, weekType: Int): Int {
        // weekType: 301 表示周一开始， 其他表示周日开始
        val firstDayOfWeek = if (weekType == NAttrs.MONDAY) DayOfWeek.MONDAY else DayOfWeek.SUNDAY

        // 将两个日期都调整到它们所在周的第一天
        val adjustedDate1 = date1.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
        val adjustedDate2 = date2.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))

        // 计算天数差
        val daysBetween = ChronoUnit.DAYS.between(adjustedDate1, adjustedDate2)

        // 除以7得到周数（由于我们已将日期调整到周的第一天，因此直接除以7即可）
        return (daysBetween / 7).toInt()
    }


    /**
     * 两个日期是否同月
     */
    fun isEqualsMonth(date1: LocalDate, date2: LocalDate): Boolean {
        return date1.year == date2.year && date1.monthValue == date2.monthValue
    }


    fun isToday(date: LocalDate?): Boolean {
        // 获取今天的日期并与给定的日期进行比较
        return LocalDate.now().isEqual(date)
    }

    /**
     * 第一个是不是第二个的上一个月
     *
     * @param date1
     * @param date2
     * @return
     */
    fun isLastMonth(date1: LocalDate, date2: LocalDate): Boolean {
        val date = date2.plusMonths(-1)
        return date1.monthValue == date.monthValue
    }


    /**
     * 第一个是不是第二个的下一个月
     *
     * @param date1
     * @param date2
     * @return
     */
    fun isNextMonth(date1: LocalDate, date2: LocalDate): Boolean {
        val date = date2.plusMonths(1)
        return date1.monthValue == date.monthValue
    }


}

