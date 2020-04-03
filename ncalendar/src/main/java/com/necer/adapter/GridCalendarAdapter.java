package com.necer.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.necer.view.CalendarView2;

import java.util.List;
/**
 * @author necer
 * QQ群:127278900
 */

public class GridCalendarAdapter extends BaseAdapter {

    private List<View> viewList;

    public GridCalendarAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

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
        View calendarItemView = viewList.get(position);
        int realHeight = parent.getMeasuredHeight() - parent.getPaddingBottom() - parent.getPaddingTop();
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, realHeight / (viewList.size() / 7));
        calendarItemView.setLayoutParams(params);
        ((CalendarView2) parent).bindView(position, calendarItemView);
        return calendarItemView;
    }
}
