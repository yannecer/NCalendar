package com.necer.listener;
import com.necer.entity.NDate;

/**
 * Created by necer on 2017/7/4.
 */

public interface OnCalendarChangedListener {
    void onCalendarDateChanged(NDate date);

    void onCalendarStateChanged(boolean isMonthSate);

}
