package com.necer.listener;

/**
 * 日历 月 周 拉伸 状态滑动监听
 *
 * @author necer
 */
public interface OnCalendarScrollingListener {

    /**
     * 折叠日历上下滑动的回调
     * @param dy 上下滑动
     */
    void onCalendarScrolling(float dy);
}
