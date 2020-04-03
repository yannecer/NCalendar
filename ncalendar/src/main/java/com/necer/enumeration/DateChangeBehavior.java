package com.necer.enumeration;

import java.io.Serializable;

/**
 * 日历日期变化行为
 * @author necer
 */
public enum DateChangeBehavior implements Serializable {

    /**
     * 初始化
     */
    INITIALIZE,

    /**
     * 点击
     */
    CLICK,

    /**
     * 手势翻页和上一页、下一页
     */
    PAGE,

    /**
     * 点击上月、下月翻页选中
     */
    CLICK_PAGE,

    /**
     * API跳转
     */
    API
}
