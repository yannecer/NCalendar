package com.necer.listener;

import com.necer.entity.NDate;


/**
 * Created by necer on 2017/6/13.
 */

public interface OnClickMonthViewListener {

    void onClickCurrentMonth(NDate nDate);

    void onClickLastMonth(NDate nDate);

    void onClickNextMonth(NDate nDate);

}
