package com.necer.entity;

import java.io.Serializable;

/**
 * Created by necer on 2018/11/16.
 */
public class Lunar implements Serializable {

    public boolean isLeap;
    public int lunarDay;
    public int lunarMonth;
    public int lunarYear;
    public int leapMonth;

    public String lunarOnDrawStr;//农历位置需要绘制的文字
    public String lunarDayStr;
    public String lunarMonthStr;
    public String lunarYearStr;
    public String animals;//生肖
    public String chineseEra;//天干地支

}
