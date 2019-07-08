package com.necer.entity;

import org.joda.time.LocalDate;

import java.io.Serializable;

/***
 * 日历日期 包含公历、农历、节气、节日
 */
public class CalendarDate implements Serializable {

    public LocalDate localDate;//公历日期
    public Lunar lunar;//农历
    public String solarHoliday;//公历节日
    public String lunarHoliday;//农历节日
    public String solarTerm;//节气
}
