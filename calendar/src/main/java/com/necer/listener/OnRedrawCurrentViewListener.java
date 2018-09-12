package com.necer.listener;
import com.necer.view.BaseCalendarView;

/**
 * Created by necer on 2018/9/12.
 * qq群：127278900
 */
public interface OnRedrawCurrentViewListener {

    void onRedrawCurrentView(BaseCalendarView currectView,BaseCalendarView lastView,int position);
}
