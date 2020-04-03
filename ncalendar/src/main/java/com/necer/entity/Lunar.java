package com.necer.entity;

import java.io.Serializable;

/**
 *
 * @author necer
 * @date 2018/11/16
 */
public class Lunar implements Serializable {

    /**
     * 是否闰年
     */
    public boolean isLeap;

    /**
     * 农历的天
     */
    public int lunarDay;

    /**
     * 农历的月
     */
    public int lunarMonth;

    /**
     * 农历年
     */
    public int lunarYear;

    /**
     * 闰月
     */
    public int leapMonth;


    /**
     * 农历位置需要绘制的文字
     */
    public String lunarOnDrawStr;

    /**
     * 农历天 描述 廿二等
     */
    public String lunarDayStr;

    /**
     * 农历月 描述
     */
    public String lunarMonthStr;

    /**
     * 农历年 描述
     */
    public String lunarYearStr;

    /**
     *生肖
     */
    public String animals;

    /**
     * 天干地支
     */
    public String chineseEra;

}
