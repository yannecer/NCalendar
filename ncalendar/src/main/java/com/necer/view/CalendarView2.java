package com.necer.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
public class CalendarView2 extends GridView implements ICalendarView {

    private CalendarHelper mCalendarHelper;
    private int mCurrentDistance;//折叠日历滑动当前的距离

    private CalendarAdapter mCalendarAdapter;

    private List<LocalDate> mDateList;

    public CalendarView2(Context context, CalendarHelper calendarHelper) {
        super(context);
        setNumColumns(7);
        mCalendarHelper = calendarHelper;
        mCalendarAdapter = calendarHelper.getCalendarAdapter();
        mDateList = mCalendarHelper.getDateList();


        List<View> viewList = new ArrayList<>();

        for (int i = 0; i < mDateList.size(); i++) {
            View calendarItem = mCalendarAdapter.getCalendarView(context);
            calendarItem.setTag(mDateList.get(i));
            viewList.add(calendarItem);
        }

        setAdapter(new GridAdapter(viewList, this));
    }

    @Override
    public LocalDate getInitialDate() {
        return mCalendarHelper.getInitialDate();
    }


    @Override
    public LocalDate getMiddleLocalDate() {
        return mCalendarHelper.getMiddleLocalDate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mCalendarHelper.getGestureDetector().onTouchEvent(event);
    }

    @Override
    public int getDistanceFromTop(LocalDate localDate) {
        return mCalendarHelper.getDistanceFromTop(localDate);
    }


    @Override
    public LocalDate getPivotDate() {
        return mCalendarHelper.getPivotDate();
    }

    @Override
    public int getPivotDistanceFromTop() {
        return mCalendarHelper.getPivotDistanceFromTop();
    }

    @Override
    public List<LocalDate> getCurrentSelectDateList() {
        return mCalendarHelper.getCurrentSelectDateList();
    }

    @Override
    public void updateSlideDistance(int currentDistance) {
        this.mCurrentDistance = currentDistance;
        notifyCalendarView();
    }

    @Override
    public List<LocalDate> getCurrentDateList() {
        return mCalendarHelper.getCurrentDateList();
    }

    @Override
    public void notifyCalendarView() {
        for (int i = 0; i < mDateList.size(); i++) {
            LocalDate localDate = mDateList.get(i);
            View view = getChildAt(i);
            if (view != null) {
                bindView(view, localDate);
            }
        }
    }

    private void bindView(View view, LocalDate localDate) {
        if (mCalendarHelper.isAvailableDate(localDate)) {
            if (mCalendarHelper.isCurrentMonthOrWeek(localDate)) {  //当月日期
                if (CalendarUtil.isToday(localDate)) {  //当天
                    mCalendarAdapter.onBindToadyView(view, localDate, mCalendarHelper.getAllSelectListDate());
                } else { //不是当天的当月其他日期
                    mCalendarAdapter.onBindCurrentMonthOrWeekView(view, localDate, mCalendarHelper.getAllSelectListDate());
                }
            } else {  //不是当月的日期
                mCalendarAdapter.onBindLastOrNextMonthView(view, localDate, mCalendarHelper.getAllSelectListDate());
            }
        } else { //日期区间之外的日期
            mCalendarAdapter.onBindDisableDateView(view, localDate);
        }
    }

    //周或者月的第一天
    @Override
    public LocalDate getFirstDate() {
        return mCalendarHelper.getFirstDate();
    }


    public static class GridAdapter extends BaseAdapter {

        private List<View> viewList;
        private CalendarView2 calendarView2;

        public GridAdapter(List<View> viewList, CalendarView2 calendarView2) {
            this.viewList = viewList;
            this.calendarView2 = calendarView2;
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
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calendarView2.getMeasuredHeight() / (viewList.size() / 7));
            calendarItem.setLayoutParams(params);

            LocalDate localDate = (LocalDate) calendarItem.getTag();
            calendarView2.bindView(calendarItem, localDate);

            Log.e("getView", "getView::iiii::" + i);

            return calendarItem;
        }
    }


}
