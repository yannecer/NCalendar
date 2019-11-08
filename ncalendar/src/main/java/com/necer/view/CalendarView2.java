package com.necer.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarType;
import com.necer.helper.CalendarHelper;
import com.necer.painter.CalendarAdapter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器构造的日历页面
 */
public class CalendarView2 extends FrameLayout implements ICalendarView {

    private CalendarHelper mCalendarHelper;
    private CalendarAdapter mCalendarAdapter;
    private GridCalendarView mGridCalendarView;
    private View mCalendarBackgroundView;
    private List<LocalDate> mDateList;


    public CalendarView2(Context context, BaseCalendar calendar, LocalDate initialDate, CalendarType calendarType) {
        super(context);

        mCalendarHelper = new CalendarHelper(calendar, initialDate, calendarType);
        mCalendarAdapter = mCalendarHelper.getCalendarAdapter();
        mDateList = mCalendarHelper.getDateList();

        float height = mCalendarHelper.getCalendarHeight();
        float rectHeight5 = height / 5;
        float rectHeight6 = (height / 5) * 4 / 5;

        if ((mCalendarHelper.getLineNum()) == 6) {
            int padding = (int) ((rectHeight5 - rectHeight6) / 2);
            setPadding(0, padding, 0, padding);
        }

        mCalendarBackgroundView = mCalendarAdapter.getCalendarBackgroundView(context);
        if (mCalendarBackgroundView != null) {
            mCalendarAdapter.onBindCalendarBackgroundView(this, mCalendarBackgroundView, getMiddleLocalDate(), mCalendarHelper.getCalendarHeight(), mCalendarHelper.getInitialDistance());
            addView(mCalendarBackgroundView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < mDateList.size(); i++) {
            View calendarItem = mCalendarAdapter.getCalendarItemView(context);
            bindView(calendarItem, mDateList.get(i));
            viewList.add(calendarItem);
        }
        mGridCalendarView = new GridCalendarView(context, viewList);
        addView(mGridCalendarView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCalendarHelper.resetRectFSize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mCalendarHelper.onTouchEvent(event);
    }

    @Override
    public LocalDate getPagerInitialDate() {
        return mCalendarHelper.getPagerInitialDate();
    }

    @Override
    public LocalDate getMiddleLocalDate() {
        return mCalendarHelper.getMiddleLocalDate();
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
        if (mCalendarBackgroundView != null) {
            mCalendarAdapter.onBindCalendarBackgroundView(this, mCalendarBackgroundView, getMiddleLocalDate(), mCalendarHelper.getCalendarHeight(), currentDistance);
        }
    }

    @Override
    public List<LocalDate> getCurrentDateList() {
        return mCalendarHelper.getCurrentDateList();
    }

    @Override
    public void notifyCalendarView() {
        for (int i = 0; i < mGridCalendarView.getChildCount(); i++) {
            View view = mGridCalendarView.getChildAt(i);
            if (view != null) {
                bindView(view, mDateList.get(i));
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

    @Override
    public CalendarType getCalendarType() {
        return mCalendarHelper.getCalendarType();
    }


}
