package com.necer.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.necer.helper.CalendarHelper;
import com.necer.painter.CalendarAdapter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器构造的日历页面
 */
public class GridCalendarView extends GridView {

    private GridAdapter mGridAdapter;

    public GridCalendarView(Context context,List<View> viewList,GridView.OnItemClickListener onItemClickListener) {
        super(context);
        setNumColumns(7);

        setAdapter(new GridAdapter(viewList));
        setOnItemClickListener(onItemClickListener);
    }

    public static class GridAdapter extends BaseAdapter {

        private List<View> viewList;


        public GridAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View calendarItem = viewList.get(i);
            Drawable background = calendarItem.getBackground();
            if (background == null) {
                calendarItem.setBackgroundColor(Color.WHITE);
            }
            int realHeight = viewGroup.getMeasuredHeight() - viewGroup.getPaddingBottom() - viewGroup.getPaddingTop();
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, realHeight / (viewList.size() / 7));
            calendarItem.setLayoutParams(params);
            return calendarItem;
        }
    }
}
