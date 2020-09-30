package com.necer.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.necer.adapter.GridCalendarAdapter;
import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarType;
import com.necer.helper.CalendarHelper;
import com.necer.painter.CalendarAdapter;
import com.necer.painter.CalendarBackground;
import com.necer.utils.CalendarUtil;
import com.necer.utils.DrawableUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器构造的日历页面
 */
public class CalendarView2 extends GridView implements ICalendarView {

    private CalendarHelper mCalendarHelper;
    private CalendarAdapter mCalendarAdapter;
    private List<LocalDate> mDateList;
    private int mCurrentDistance = -1;
    private BaseAdapter gridViewAdapter;


    public CalendarView2(Context context, BaseCalendar calendar, LocalDate initialDate, CalendarType calendarType) {
        super(context);
        setWillNotDraw(false);
        setNumColumns(7);

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

        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < mDateList.size(); i++) {
            View calendarItem = mCalendarAdapter.getCalendarItemView(context);
            viewList.add(calendarItem);
        }

        gridViewAdapter = new GridCalendarAdapter(viewList);
        setAdapter(gridViewAdapter);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景
        CalendarBackground calendarBackground = mCalendarHelper.getCalendarBackground();
        drawBackground(canvas, calendarBackground);

    }

    //绘制背景
    private void drawBackground(Canvas canvas, CalendarBackground calendarBackground) {
        int currentDistance = mCurrentDistance == -1 ? mCalendarHelper.getInitialDistance() : mCurrentDistance;
        Drawable backgroundDrawable = calendarBackground.getBackgroundDrawable(mCalendarHelper.getMiddleLocalDate(), currentDistance, mCalendarHelper.getCalendarHeight());
        Rect backgroundRectF = mCalendarHelper.getBackgroundRectF();
        backgroundDrawable.setBounds(DrawableUtil.getDrawableBounds(backgroundRectF.centerX(), backgroundRectF.centerY(), backgroundDrawable));
        backgroundDrawable.draw(canvas);
    }

    //刷新view
    public void bindView(int position,View itemView) {
            LocalDate localDate = mDateList.get(position);
            if (mCalendarHelper.isAvailableDate(localDate)) {
                if (mCalendarHelper.isCurrentMonthOrWeek(localDate)) {  //当月日期
                    if (CalendarUtil.isToday(localDate)) {  //当天
                        mCalendarAdapter.onBindToadyView(itemView, localDate, mCalendarHelper.getAllSelectListDate());
                    } else { //不是当天的当月其他日期
                        mCalendarAdapter.onBindCurrentMonthOrWeekView(itemView, localDate, mCalendarHelper.getAllSelectListDate());
                    }
                } else {  //不是当月的日期
                    mCalendarAdapter.onBindLastOrNextMonthView(itemView, localDate, mCalendarHelper.getAllSelectListDate());
                }
            } else { //日期区间之外的日期
                mCalendarAdapter.onBindDisableDateView(itemView, localDate);
            }
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
    public List<LocalDate> getCurrPagerCheckDateList() {
        return mCalendarHelper.getCurrentSelectDateList();
    }

    @Override
    public void updateSlideDistance(int currentDistance) {
        this.mCurrentDistance = currentDistance;
        invalidate();
    }

    @Override
    public List<LocalDate> getCurrPagerDateList() {
        return mCalendarHelper.getCurrentDateList();
    }

    @Override
    public void notifyCalendarView() {
        gridViewAdapter.notifyDataSetChanged();
    }

    //周或者月的第一天
    @Override
    public LocalDate getCurrPagerFirstDate() {
        return mCalendarHelper.getCurrPagerFirstDate();
    }

    @Override
    public CalendarType getCalendarType() {
        return mCalendarHelper.getCalendarType();
    }


}
