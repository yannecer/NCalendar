package com.necer.enumeration;

import java.io.Serializable;

/**
 * 日历日期变化行为
 */
public enum DateChangeBehavior implements Serializable {

    INITIALIZE,//初始化
    CLICK,//点击
    PAGE,//手势翻页和上一页、下一页
    CLICK_PAGE,//点击上月/下月翻页
    API //API跳转
}
