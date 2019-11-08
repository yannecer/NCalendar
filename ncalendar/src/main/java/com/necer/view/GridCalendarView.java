package com.necer.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 适配器构造的日历页面
 */
public class GridCalendarView extends GridView {

    public GridCalendarView(Context context) {
        super(context);
        setNumColumns(7);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
