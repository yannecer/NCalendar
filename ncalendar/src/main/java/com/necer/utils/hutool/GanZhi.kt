package com.necer.utils.hutool

import java.time.LocalDate

/**
 * 天干地支类
 * 天干地支，简称为干支
 *
 * @author looly
 * @since 5.4.1
 */
object GanZhi {
    /**
     * 十天干：甲（jiǎ）、乙（yǐ）、丙（bǐng）、丁（dīng）、戊（wù）、己（jǐ）、庚（gēng）、辛（xīn）、壬（rén）、癸（guǐ）
     * 十二地支：子（zǐ）、丑（chǒu）、寅（yín）、卯（mǎo）、辰（chén）、巳（sì）、午（wǔ）、未（wèi）、申（shēn）、酉（yǒu）、戌（xū）、亥（hài）
     * 十二地支对应十二生肖:子-鼠，丑-牛，寅-虎，卯-兔，辰-龙，巳-蛇， 午-马，未-羊，申-猴，酉-鸡，戌-狗，亥-猪
     *
     * @see [天干地支：简称，干支](https://baike.baidu.com/item/%E5%A4%A9%E5%B9%B2%E5%9C%B0%E6%94%AF/278140)
     */
    private val GAN = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    private val ZHI = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")

    /**
     * 传入 月日的offset 传回干支, 0=甲子
     *
     * @param num 月日的offset
     * @return 干支
     */
    fun cyclicalm(num: Int): String {
        return GAN[num % 10] + ZHI[num % 12]
    }

    /**
     * 传入年传回干支
     *
     * @param year 农历年
     * @return 干支
     * @since 5.4.7
     */
    fun getGanzhiOfYear(year: Int): String {
        // 1864年（1900 - 36）是甲子年，用于计算基准的干支年
        return cyclicalm(year - LunarInfo.BASE_YEAR + 36)
    }

    /**
     * 获取干支月
     *
     * @param year  公历年
     * @param month 公历月，从1开始
     * @param day   公历日
     * @return 干支月
     * @since 5.4.7
     */
    fun getGanzhiOfMonth(year: Int, month: Int, day: Int): String {
        //返回当月「节」为几日开始
        val firstNode = SolarTerms.getTerm(year, month * 2 - 1)
        // 依据12节气修正干支月
        var monthOffset = (year - LunarInfo.BASE_YEAR) * 12 + month + 11
        if (day >= firstNode) {
            monthOffset++
        }
        return cyclicalm(monthOffset)
    }

    /**
     * 获取干支日
     *
     * @param year  公历年
     * @param month 公历月，从1开始
     * @param day   公历日
     * @return 干支
     * @since 5.4.7
     */
    fun getGanzhiOfDay(year: Int, month: Int, day: Int): String {
        // 与1970-01-01相差天数，不包括当天
        val days = LocalDate.of(year, month, day).toEpochDay() - 1
        //1899-12-21是农历1899年腊月甲子日  41：相差1900-01-31有41天
        return cyclicalm((days - LunarInfo.BASE_DAY + 41).toInt())
    }
}