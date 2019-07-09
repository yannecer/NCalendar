package com.necer.entity;

import java.io.Serializable;

/**
 * Created by necer on 2018/11/16.
 */
public class Lunar implements Serializable {

    public boolean isLeap;//是否闰年
    public int lunarDay;//农历天
    public int lunarMonth;//农历月
    public int lunarYear;//农历年
    public int leapMonth;//闰月

    public String lunarOnDrawStr;//农历位置需要绘制的文字
    public String lunarDayStr;//农历天 描述 廿二等
    public String lunarMonthStr;//农历月 描述
    public String lunarYearStr;//农历年 描述
    public String animals;//生肖
    public String chineseEra;//天干地支

}
