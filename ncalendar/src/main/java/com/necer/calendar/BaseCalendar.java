package com.necer.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Toast;

import com.necer.adapter.BaseCalendarAdapter;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.listener.OnMWDateChangeListener;
import com.necer.painter.InnerPainter;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;
import com.necer.utils.AttrsUtil;
import com.necer.view.CalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class BaseCalendar extends ViewPager implements ICalendar {

    private Context mContext;
    private Attrs mAttrs;

    //这两个不能同时为真
    private boolean mIsDefaultSelect; //默认选中 不能多选
    private boolean mIsMultipleSelset;  // 多选情况下 不能默认选中

    private boolean mIsJumpClick;//是否是点击上月、下月或跳转，这个只在默认选中时有用
    private boolean mIsDefaultSelectFitst;//默认选择时，翻页选中第一个日期

    protected OnClickDisableDateListener mOnClickDisableDateListener;//点击区间之外的日期回调
    private OnMWDateChangeListener mOnMWDateChangeListener;//月周切换是折叠中心的回调
    private OnCalendarChangedListener mOnCalendarChangedListener;//单选时回调，当前页面无选中返回null
    private OnCalendarMultipleChangedListener mOnCalendarMultipleChangedListener;//多选时回调

    protected LocalDate mStartDate, mEndDate, mInitializeDate;

    protected CalendarPainter mCalendarPainter;

    private List<LocalDate> mAllSelectDateList;


    private boolean mIsInflateFinish;//是否加载完成，


    public BaseCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAttrs = AttrsUtil.getAttrs(context, attributeSet);
        this.mCalendarPainter = new InnerPainter(this);
        init(context);
    }


    public BaseCalendar(Context context, Attrs attrs, CalendarPainter calendarPainter) {
        super(context);
        this.mAttrs = attrs;
        this.mCalendarPainter = calendarPainter;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mAllSelectDateList = new ArrayList<>();
        mInitializeDate = new LocalDate();
        mIsMultipleSelset = mAttrs.isMultipleSelect;
        mIsDefaultSelect = mIsMultipleSelset ? false : mAttrs.isDefaultSelect;//当多选时，不能默认选中
        mIsDefaultSelectFitst = mAttrs.isDefaultSelectFitst;
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                drawView(position);
            }
        });
        initDate();
    }


    private void initDate() {
        if (mIsDefaultSelect) {
            mAllSelectDateList.clear();
            mAllSelectDateList.add(mInitializeDate);
        }
        String startDateString = mAttrs.startDateString;
        String endDateString = mAttrs.endDateString;
        try {
            mStartDate = new LocalDate(startDateString);
            mEndDate = new LocalDate(endDateString);
        } catch (Exception e) {
            throw new RuntimeException("startDate、endDate需要 yyyy-MM-dd 格式的日期");
        }


        if (mStartDate.isAfter(mEndDate)) {
            throw new RuntimeException("startDate必须在endDate之前");
        }

        if (mStartDate.isBefore(new LocalDate("1901-01-01"))) {
            throw new RuntimeException("startDate必须在1901-01-01之后");
        }

        if (mEndDate.isAfter(new LocalDate("2099-12-31"))) {
            throw new RuntimeException("endDate必须在2099-12-31之前");
        }

      /*  if (startDate.isAfter(initializeDate) || endDate.isBefore(initializeDate)) {
            throw new RuntimeException("日期区间需要包含今天");
        }*/

        BaseCalendarAdapter calendarAdapter = getCalendarAdapter(mContext, mStartDate, mEndDate, mInitializeDate, mAttrs.firstDayOfWeek);
        int currItem = calendarAdapter.getCurrItem();
        setAdapter(calendarAdapter);
        setCurrentItem(currItem);
    }

    @Override
    public void setDateInterval(String startFormatDate, String endFormatDate) {
        mAttrs.startDateString = startFormatDate;
        mAttrs.endDateString = endFormatDate;
        initDate();
    }

    @Override
    public void setInitializeDate(String formatInitializeDate) {
        try {
            mInitializeDate = new LocalDate(formatInitializeDate);
        } catch (Exception e) {
            throw new RuntimeException("setInitializeDate的参数需要 yyyy-MM-dd 格式的日期");
        }
        initDate();
    }

    @Override
    public void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate) {
        mAttrs.startDateString = startFormatDate;
        mAttrs.endDateString = endFormatDate;
        try {
            mInitializeDate = new LocalDate(formatInitializeDate);
        } catch (Exception e) {
            throw new RuntimeException("setInitializeDate的参数需要 yyyy-MM-dd 格式的日期");
        }
        initDate();
    }


    private void drawView(int position) {
        CalendarView currectCalendarView = findViewWithTag(position);
        if (currectCalendarView == null) {
            return;
        }
        if (mIsDefaultSelect) {//默认选中，只有一种情况 单选
            LocalDate initialDate = currectCalendarView.getInitialDate();//当前页面初始化的日期
            LocalDate lastDate = mAllSelectDateList.get(0);//上个页面选中的日期
            //当前面页面的初始值和上个页选中的日期，相差几月或几周，再又上个页面选中的日期得出当前页面选中的日期
            int currNum = getTwoDateCount(lastDate, initialDate, mAttrs.firstDayOfWeek);//得出两个页面相差几个
            LocalDate tempLocalDate = getIntervalDate(lastDate, currNum);
            LocalDate currectDate; //当前页面选中的日期
            if (mIsDefaultSelectFitst && !mIsJumpClick && !tempLocalDate.equals(new LocalDate())) {
                //默认选中第一个 且 不是点击或跳转 且 不等于今天（为了第一次进来日历选中的是今天）
                currectDate = getFirstDate();
            } else {
                currectDate = tempLocalDate;
            }

            currectDate = getAvailableDate(currectDate);

            mIsJumpClick = false;

            mAllSelectDateList.clear();
            mAllSelectDateList.add(currectDate);
            currectCalendarView.invalidate();
        } else {
            //不默认选中，分为两种情况，1、单选 2、多选  这两种情况只需要绘制页面
            currectCalendarView.invalidate();
        }

        callBack();
    }


    public void onClickCurrectMonthOrWeekDate(LocalDate localDate) {
        //判断日期是否合法
        if (!isAvailable(localDate)) {
            clickDisableDate(localDate);
            return;
        }

        if (mIsMultipleSelset) {
            //多选  集合不包含就添加，包含就移除
            if (!mAllSelectDateList.contains(localDate)) {
                mAllSelectDateList.add(localDate);
            } else {
                mAllSelectDateList.remove(localDate);
            }
            notifyCalendar();
            //当前页面选中改变，需要回调
            callBack();
        } else {
            //单选  集合中有且只有一个元素 ，不包含就清空集合添加，包含该元素不做处理，因为前面已经绘制
            if (!mAllSelectDateList.contains(localDate)) {
                mAllSelectDateList.clear();
                mAllSelectDateList.add(localDate);
                notifyCalendar();
                //当前页面选中改变，需要回调
                callBack();

            }
        }
    }

    public void onClickLastMonthDate(LocalDate localDate) {
        jump(localDate, true);
    }

    public void onClickNextMonthDate(LocalDate localDate) {
        jump(localDate, true);
    }

    public void jump(LocalDate localDate, boolean isDraw) {
        //判断日期是否合法
        if (!isAvailable(localDate)) {
            clickDisableDate(localDate);
            return;
        }

        mIsJumpClick = true;
        CalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        int indexOffset = getTwoDateCount(localDate, currectCalendarView.getInitialDate(), mAttrs.firstDayOfWeek);//得出两个页面相差几个
        if (mIsMultipleSelset) {
            //多选  点击的日期不清除，只翻页，如果需要清除，等到翻页之后再次点击
            if (!mAllSelectDateList.contains(localDate) && isDraw) {
                mAllSelectDateList.add(localDate);
            }
        } else {
            //单选
            if (!mAllSelectDateList.contains(localDate) && isDraw) {
                mAllSelectDateList.clear();
                mAllSelectDateList.add(localDate);
            }
        }
        if (indexOffset == 0) {
            drawView(getCurrentItem());
        } else {
            setCurrentItem(getCurrentItem() - indexOffset, Math.abs(indexOffset) == 1);
        }

    }

    private void callBack() {
        post(new Runnable() {
            @Override
            public void run() {
                CalendarView currectCalendarView = findViewWithTag(getCurrentItem());
                LocalDate initialDate = currectCalendarView.getInitialDate();
                List<LocalDate> currentSelectDateList = currectCalendarView.getCurrentSelectDateList();

                if (mOnMWDateChangeListener != null) {
                    mOnMWDateChangeListener.onMwDateChange(BaseCalendar.this, currectCalendarView.getPivotDate(), mAllSelectDateList);
                }

                if (mOnCalendarChangedListener != null && !mIsMultipleSelset && getVisibility() == VISIBLE) {
                    //单选
                    mOnCalendarChangedListener.onCalendarChange(BaseCalendar.this, initialDate.getYear(), initialDate.getMonthOfYear(), currentSelectDateList.size() == 0 ? null : currentSelectDateList.get(0));
                }

                if (mOnCalendarMultipleChangedListener != null && mIsMultipleSelset && getVisibility() == VISIBLE) {
                    //多选
                    mOnCalendarMultipleChangedListener.onCalendarChange(BaseCalendar.this, initialDate.getYear(), initialDate.getMonthOfYear(), currentSelectDateList, mAllSelectDateList);
                }
            }
        });
    }


    @Override
    public void notifyCalendar() {
        for (int i = 0; i < getChildCount(); i++) {
            CalendarView calendarView = (CalendarView) getChildAt(i);
            if (calendarView != null) {
                calendarView.invalidate();
            }
        }
    }


    //日期边界处理
    private LocalDate getAvailableDate(LocalDate localDate) {
        if (localDate.isBefore(mStartDate)) {
            return mStartDate;
        } else if (localDate.isAfter(mEndDate)) {
            return mEndDate;
        } else {
            return localDate;
        }
    }

    @Override
    public void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener) {
        this.mOnClickDisableDateListener = onClickDisableDateListener;
    }

    //点击不可用的日期处理
    private void clickDisableDate(LocalDate localDate) {
        if (getVisibility() != VISIBLE) {
            return;
        }
        if (mOnClickDisableDateListener != null) {
            mOnClickDisableDateListener.onClickDisableDate(localDate);
        } else {
            Toast.makeText(getContext(), TextUtils.isEmpty(mAttrs.disabledString) ? "日期超出许可范围" : mAttrs.disabledString, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void toNextPager() {
        setCurrentItem(getCurrentItem() + 1, true);
    }

    @Override
    public void toLastPager() {
        setCurrentItem(getCurrentItem() - 1, true);
    }

    @Override
    public void toToday() {
        jump(new LocalDate(), true);
    }


    @Override
    public void jumpDate(String formatDate) {
        LocalDate jumpDate;
        try {
            jumpDate = new LocalDate(formatDate);
        } catch (Exception e) {
            throw new RuntimeException("jumpDate的参数需要 yyyy-MM-dd 格式的日期");
        }

        jump(jumpDate, true);
    }

    @Override
    public List<LocalDate> getAllSelectDateList() {
        return mAllSelectDateList;
    }

    //点击的日期是否可用
    protected boolean isAvailable(LocalDate localDate) {
        return !localDate.isBefore(mStartDate) && !localDate.isAfter(mEndDate);
    }

    //获取区间开始日期
    public LocalDate getStartDate() {
        return mStartDate;
    }

    //获取区间结束日期
    public LocalDate getEndDate() {
        return mEndDate;
    }

    //设置绘制类
    @Override
    public void setCalendarPainter(CalendarPainter calendarPainter) {
        this.mCalendarPainter = calendarPainter;
        notifyCalendar();
    }

    @Override
    public CalendarPainter getCalendarPainter() {
        return mCalendarPainter;
    }


    //月周切换时交换数据，保证月日历和周日历有相同的选中日期
    public void exchangeSelectDateList(List<LocalDate> dateList) {
        mAllSelectDateList.clear();
        mAllSelectDateList.addAll(dateList);
        notifyCalendar();
    }


    public void setOnMWDateChangeListener(OnMWDateChangeListener onMWDateChangeListener) {
        this.mOnMWDateChangeListener = onMWDateChangeListener;
    }

    @Override
    public void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener) {
        this.mOnCalendarChangedListener = onCalendarChangedListener;
    }

    @Override
    public void setOnCalendarMultipleChangedListener(OnCalendarMultipleChangedListener onCalendarMultipleChangedListener) {
        this.mOnCalendarMultipleChangedListener = onCalendarMultipleChangedListener;
    }

    //获取当前月，当前周的第一个日期
    public LocalDate getFirstDate() {
        CalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        if (currectCalendarView != null) {
            return currectCalendarView.getFirstDate();
        }
        return null;
    }


    //获取PivotDate
    public LocalDate getPivotDate() {
        CalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        if (currectCalendarView != null) {
            return currectCalendarView.getPivotDate();
        }
        return null;
    }


    //localDate到顶部的距离
    public int getDistanceFromTop(LocalDate localDate) {
        CalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        if (currectCalendarView != null) {
            return currectCalendarView.getDistanceFromTop(localDate);
        }
        return 0;
    }


    //PivotDate到顶部的距离
    public int getPivotDistanceFromTop() {
        CalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        if (currectCalendarView != null) {
            return currectCalendarView.getPivotDistanceFromTop();
        }
        return 0;
    }


    //回去viewpager的adapter
    protected abstract BaseCalendarAdapter getCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, int firstDayOfWeek);

    //两个日期的相差数量
    protected abstract int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type);

    //相差count之后的的日期
    protected abstract LocalDate getIntervalDate(LocalDate localDate, int count);


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!mIsInflateFinish) {
            drawView(getCurrentItem());
            mIsInflateFinish = true;
        }
    }

    @Override
    public List<LocalDate> getCurrectSelectDateList() {
        CalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        if (currectCalendarView != null) {
            return currectCalendarView.getCurrentSelectDateList();
        }
        return null;
    }

    @Override
    public Attrs getAttrs() {
        return mAttrs;
    }

    @Override
    public void setDefaultSelect(boolean isDefaultSelect) {
        this.mIsDefaultSelect = isDefaultSelect;
        if (isDefaultSelect) {
            mIsMultipleSelset = false;
            if (mAllSelectDateList.size() == 0 ) {
                mAllSelectDateList.add(new LocalDate());
            }
        } else {
            mAllSelectDateList.clear();
        }
    }

    @Override
    public void setDefaultSelectFitst(boolean isDefaultSelectFitst) {
        this.mIsDefaultSelectFitst = isDefaultSelectFitst;
    }

    @Override
    public void setMultipleSelset(boolean isMultipleSelset) {
        this.mIsMultipleSelset = isMultipleSelset;
        if (mIsMultipleSelset) {
            mIsDefaultSelect = false;
        }
    }
}
