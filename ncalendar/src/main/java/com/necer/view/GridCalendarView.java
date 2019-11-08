package com.necer.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;


import java.util.List;

/**
 * 适配器构造的日历页面
 */
public class GridCalendarView extends GridView {

    public GridCalendarView(Context context, final List<View> viewList) {
        super(context);
        setNumColumns(7);
        setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View calendarItem = viewList.get(position);
                int realHeight = parent.getMeasuredHeight() - parent.getPaddingBottom() - parent.getPaddingTop();
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, realHeight / (viewList.size() / 7));
                calendarItem.setLayoutParams(params);
                return calendarItem;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
