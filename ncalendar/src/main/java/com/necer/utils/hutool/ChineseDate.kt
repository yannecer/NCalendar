package com.necer.utils.hutool

import java.time.LocalDate

/**
 * 农历日期工具，最大支持到2099年，支持：
 *
 *
 *  * 通过公历日期构造获取对应农历
 *  * 通过农历日期直接构造
 *
 *
 * @author zjw, looly
 * @since 5.1.1
 */
class ChineseDate {


    private val DAY_NAME = arrayOf("一", "二", "三", "四", "五", "六", "七", "八", "九", "十")
    private val MONTH_NAME = arrayOf("一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二")
    private val MONTH_NAME_TRADITIONAL = arrayOf("正", "二", "三", "四", "五", "六", "七", "八", "九", "寒", "冬", "腊")
    private val CHINESE_ZODIACS = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")


    /**
     * 获得农历年份
     *
     * @return 返回农历年份
     */
    //农历年
    val chineseYear: Int

    /**
     * 获取农历的月，从1开始计数<br></br>
     * 此方法返回实际的月序号，如一月是闰月，则一月返回1，润一月返回2
     *
     * @return 农历的月
     * @since 5.2.4
     */
    //农历月，润N月这个值就是N+1，其他月按照显示月份赋值
    val month: Int

    /**
     * 当前农历月份是否为闰月
     *
     * @return 是否为闰月
     * @since 5.4.2
     */
    // 当前月份是否闰月
    val isLeapMonth: Boolean

    /**
     * 获取农历的日，从1开始计数
     *
     * @return 农历的日，从1开始计数
     * @since 5.2.4
     */
    //农历日
    val day: Int

    /**
     * 获取公历的年
     *
     * @return 公历年
     * @since 5.6.1
     */
    //公历年
    val gregorianYear: Int

    /**
     * 获取公历的月，从1开始计数
     *
     * @return 公历月
     * @since 5.6.1
     */
    //公历月，从1开始计数
    val gregorianMonthBase1: Int

    /**
     * 获取公历的日
     *
     * @return 公历日
     * @since 5.6.1
     */
    //公历日
    val gregorianDay: Int


    /**
     * 通过公历日期构造
     *
     * @param localDate 公历日期
     * @since 5.7.22
     */
    constructor(localDate: LocalDate) {
        // 公历
        gregorianYear = localDate.year
        gregorianMonthBase1 = localDate.monthValue
        gregorianDay = localDate.dayOfMonth

        // 求出和1900年1月31日相差的天数
        var offset = (localDate.toEpochDay() - LunarInfo.BASE_DAY).toInt()

        // 计算农历年份
        // 用offset减去每农历年的天数，计算当天是农历第几天，offset是当年的第几天
        var daysOfYear: Int
        var iYear: Int
        iYear = LunarInfo.BASE_YEAR
        while (iYear <= LunarInfo.MAX_YEAR) {
            daysOfYear = LunarInfo.yearDays(iYear)
            if (offset < daysOfYear) {
                break
            }
            offset -= daysOfYear
            iYear++
        }
        chineseYear = iYear
        // 计算农历月份
        val leapMonth = LunarInfo.leapMonth(iYear) // 闰哪个月,1-12
        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        var month: Int
        var daysOfMonth: Int
        var hasLeapMonth = false
        month = 1
        while (month < 13) {

            // 闰月，如润的是五月，则5表示五月，6表示润五月
            if (leapMonth > 0 && month == leapMonth + 1) {
                daysOfMonth = LunarInfo.leapDays(chineseYear)
                hasLeapMonth = true
            } else {
                // 普通月，当前面的月份存在闰月时，普通月份要-1，递补闰月的数字
                // 如2月是闰月，此时3月实际是第四个月
                daysOfMonth = LunarInfo.monthDays(chineseYear, if (hasLeapMonth) month - 1 else month)
            }
            if (offset < daysOfMonth) {
                // offset不足月，结束
                break
            }
            offset -= daysOfMonth
            month++
        }
        isLeapMonth = leapMonth > 0 && month == leapMonth + 1
        if (hasLeapMonth && false == isLeapMonth) {
            // 当前月份前有闰月，则月份显示要-1，除非当前月份就是润月
            month--
        }
        this.month = month
        day = offset + 1
    }

    /**
     * 获取公历的月，从0开始计数
     *
     * @return 公历月
     * @since 5.6.1
     */
    fun getGregorianMonth(): Int {
        return gregorianMonthBase1 - 1
    }

    /**
     * 获得农历月份（中文，例如二月，十二月，或者润一月）
     *
     * @return 返回农历月份
     */
    fun getChineseMonth(): String {
        return getChineseMonth(false)
    }

    /**
     * 获得农历月称呼（中文，例如二月，腊月，或者润正月）
     *
     * @return 返回农历月份称呼
     */
    fun getChineseMonthName(): String {
        return getChineseMonth(true)
    }

    /**
     * 获得农历月份（中文，例如二月，十二月，或者润一月）
     *
     * @param isTraditional 是否传统表示，例如一月传统表示为正月
     * @return 返回农历月份
     * @since 5.7.18
     */
    fun getChineseMonth(isTraditional: Boolean): String {
        return getChineseMonthName(
            isLeapMonth,
            if (isLeapMonth) month - 1 else month, isTraditional
        )
    }

    /**
     * 获得农历日
     *
     * @return 获得农历日
     */
    fun getChineseDay(): String {
        val chineseTen = arrayOf("初", "十", "廿", "卅")
        val n = if (day % 10 == 0) 9 else day % 10 - 1
        return if (day > 30) {
            ""
        } else when (day) {
            10 -> "初十"
            20 -> "二十"
            30 -> "三十"
            else -> chineseTen[day / 10] + DAY_NAME[n]
        }
    }

    /**
     * 获得农历节日，闰月不计入节日中
     *
     * @return 获得农历节日
     */
    fun getLunarFestivals(): String? {
        return LunarFestival.getFestivals(chineseYear, month, day)
    }

    /**
     * 获得公历节日
     */
    fun getSolarFestivals(): String? {
        return SolarFestival.getFestivals(gregorianMonthBase1, gregorianDay)
    }


    /**
     * 获得年份生肖
     *
     * @return 获得年份生肖
     */
    fun getChineseZodiac(): String? {
        return getChineseZodiac(chineseYear)
    }

    /**
     * 获得年的天干地支
     *
     * @return 获得天干地支
     */
    fun getCyclical(): String {
        return GanZhi.getGanzhiOfYear(chineseYear)
    }

    /**
     * 干支纪年信息
     *
     * @return 获得天干地支的年月日信息
     */
    fun getCyclicalYMD(): String? {
        return if (gregorianYear >= LunarInfo.BASE_YEAR && gregorianMonthBase1 > 0 && gregorianDay > 0) {
            cyclicalm(gregorianYear, gregorianMonthBase1, gregorianDay)
        } else null
    }


    /**
     * 获得节气
     *
     * @return 获得节气
     * @since 5.6.3
     */
    fun getTerm(): String {
        return SolarTerms.getTerm(gregorianYear, gregorianMonthBase1, gregorianDay)
    }

    /**
     * 转换为标准的日期格式来表示农历日期，例如2020-01-13<br></br>
     * 如果存在闰月，显示闰月月份，如润二月显示2
     *
     * @return 标准的日期格式
     * @since 5.2.4
     */
    fun toStringNormal(): String {
        return String.format(
            "%04d-%02d-%02d", chineseYear,
            if (isLeapMonth) month - 1 else month, day
        )
    }

    override fun toString(): String {
        return String.format("%s%s年 %s%s", getCyclical(), getChineseZodiac(), getChineseMonthName(), getChineseDay())
    }
    // ------------------------------------------------------- private method start
    /**
     * 这里同步处理年月日的天干地支信息
     *
     * @param year  公历年
     * @param month 公历月，从1开始
     * @param day   公历日
     * @return 天干地支信息
     */
    private fun cyclicalm(year: Int, month: Int, day: Int): String {
        return GanZhi.getGanzhiOfYear(chineseYear) + "年" +
                GanZhi.getGanzhiOfMonth(year, month, day) + "月" +
                GanZhi.getGanzhiOfDay(year, month, day) + "日"

    }

    /**
     * 获得农历月称呼<br></br>
     * 当为传统表示时，表示为二月，腊月，或者润正月等
     * 当为非传统表示时，二月，十二月，或者润一月等
     *
     * @param isLeapMonth   是否闰月
     * @param month         月份，从1开始，如果是闰月，应传入需要显示的月份
     * @param isTraditional 是否传统表示，例如一月传统表示为正月
     * @return 返回农历月份称呼
     */
    private fun getChineseMonthName(isLeapMonth: Boolean, month: Int, isTraditional: Boolean): String {
        return (if (isLeapMonth) "闰" else "") + (if (isTraditional) MONTH_NAME_TRADITIONAL else MONTH_NAME)[month - 1] + "月"
    }


    /**
     * 计算生肖，只计算1900年后的日期
     *
     * @param year 农历年
     * @return 生肖名
     */
    private fun getChineseZodiac(year: Int): String? {
        return if (year < 1900) {
            null
        } else CHINESE_ZODIACS[(year - 1900) % CHINESE_ZODIACS.size]
    }
}