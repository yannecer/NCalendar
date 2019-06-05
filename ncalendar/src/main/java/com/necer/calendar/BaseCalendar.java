package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Toast;

import com.necer.MyLog;
import com.necer.adapter.BaseCalendarAdapter;
import com.necer.entity.NDate;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.listener.OnYearMonthChangedListener;
import com.necer.painter.InnerPainter;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;
import com.necer.utils.AttrsUtil;
import com.necer.utils.Util;
import com.necer.view.BaseCalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class BaseCalendar extends ViewPager {

    private Context mContext;

    private Attrs attrs;
    protected BaseCalendarView mCurrView;//当前显示的页面
    // protected BaseCalendarView mLastView;//当前显示的页面的上一个页面
    //  protected BaseCalendarView mNextView;//当前显示的页面的下一个页面

    // protected LocalDate mSelectDate;//日历上面点击选中的日期,包含点击选中和翻页选中


    protected LocalDate mOnClickDate;//点击选中的日期


    //这两个不能同时为真
    private boolean isDefaultSelect = false; //默认选中 不能多选
    private boolean isMultiple = true;  // 多选情况下 不能默认选中


    protected OnYearMonthChangedListener onYearMonthChangedListener;
    protected OnClickDisableDateListener onClickDisableDateListener;

    protected LocalDate startDate, endDate, initializeDate;

    protected CalendarPainter mCalendarPainter;

    private List<LocalDate> mSelectDateList;


    public BaseCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.attrs = AttrsUtil.getAttrs(context, attributeSet);
        this.mCalendarPainter = new InnerPainter(attrs);
        init(context);
    }


    public BaseCalendar(Context context, Attrs attrs, CalendarPainter calendarPainter) {
        super(context);
        this.attrs = attrs;
        this.mCalendarPainter = calendarPainter;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mSelectDateList = new ArrayList<>();
        mSelectDateList.add(new LocalDate());
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        drawView(position);
                    }
                });
            }
        });

        initDate(new LocalDate());
    }


    private void initDate(LocalDate initializeDate) {

        String startDateString = attrs.startDateString;
        String endDateString = attrs.endDateString;
        try {
            startDate = new LocalDate(startDateString);
            endDate = new LocalDate(endDateString);
        } catch (Exception e) {
            throw new RuntimeException("startDate、endDate需要 yyyy-MM-dd 格式的日期");
        }


        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("startDate必须在endDate之前");
        }

        if (startDate.isBefore(new LocalDate("1901-01-01"))) {
            throw new RuntimeException("startDate必须在1901-01-01之后");
        }

        if (endDate.isAfter(new LocalDate("2099-12-31"))) {
            throw new RuntimeException("endDate必须在2099-12-31之前");
        }


      /*  if (startDate.isAfter(initializeDate) || endDate.isBefore(initializeDate)) {
            throw new RuntimeException("日期区间需要包含今天");
        }*/

        BaseCalendarAdapter calendarAdapter = getCalendarAdapter(mContext, startDate, endDate, initializeDate, attrs.firstDayOfWeek);
        int currItem = calendarAdapter.getCurrItem();
        setAdapter(calendarAdapter);

        //当前item为0时，OnPageChangeListener不回调
        if (currItem == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    //  drawView(getCurrentItem());
                }
            });
        }

        setCurrentItem(currItem);

    }

    public void setDateInterval(String startFormatDate, String endFormatDate) {
        attrs.startDateString = startFormatDate;
        attrs.endDateString = endFormatDate;
        initDate(initializeDate);
    }

    public void setInitializeDate(String formatInitializeDate) {
        try {
            initializeDate = new LocalDate(formatInitializeDate);
        } catch (Exception e) {
            throw new RuntimeException("setInitializeDate的参数需要 yyyy-MM-dd 格式的日期");
        }
        initDate(initializeDate);
    }

    public void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate) {
        attrs.startDateString = startFormatDate;
        attrs.endDateString = endFormatDate;
        try {
            initializeDate = new LocalDate(formatInitializeDate);
        } catch (Exception e) {
            throw new RuntimeException("setInitializeDate的参数需要 yyyy-MM-dd 格式的日期");
        }
        initDate(initializeDate);
    }

    private void drawView(int position) {
        BaseCalendarView currectCalendarView = findViewWithTag(position);
        if (currectCalendarView == null) {
            return;
        }

        if (isDefaultSelect) {//默认选中，只有一种情况 单选
            LocalDate initialDate = currectCalendarView.getInitialDate();//当前页面初始化的日期
            LocalDate lastDate = mSelectDateList.get(0);//上个页面选中的日期
            //当前面页面的初始值和上个页选中的日期，相差几月或几周，再又上个页面选中的日期得出当前页面选中的日期
            int currNum = getTwoDateCount(lastDate, initialDate, attrs.firstDayOfWeek);//得出两个页面相差几个
            LocalDate localDate = getDate(lastDate, currNum);
            mSelectDateList.clear();
            mSelectDateList.add(localDate);
            currectCalendarView.invalidate();
        } else {//不默认选中，分为两种情况，1、单选 2、多选  这两种情况只需要绘制页面
            currectCalendarView.invalidate();
        }


//        LocalDate initialDate = mCurrView.getInitialDate();
//        //当前面页面的初始值和上个页选中的日期，相差几月或几周，再又上个页面选中的日期得出当前页面选中的日期
//        int currNum = getTwoDateCount(mSelectDate, initialDate, attrs.firstDayOfWeek);//得出两个页面相差几个
//        if (currNum != 0) {
//            mSelectDate = getDate(mSelectDate, currNum);
//        }
//        mSelectDate = getSelectDate(mSelectDate);
//
//        //绘制的规则：1、默认选中，每个页面都会有选中。1、默认不选中，但是点击了当前页面的某个日期
//        //当前页面的回调，绘制
//        boolean isCurrViewDraw = attrs.isDefaultSelect || (mCurrView.contains(mOnClickDate));
//
//        //翻页回调，会有重复，但是通过callBackDate可避免重复回调
//        callBack(isCurrViewDraw, false);
//        notifyView();
    }

    //刷新页面
//    protected void notifyView() {
//
//        if (mCurrView == null) {
//            mCurrView = findViewWithTag(getCurrentItem());
//        }
//        if (mCurrView != null) {
//            boolean isDraw = attrs.isDefaultSelect || (mCurrView.contains(mOnClickDate));
//            mCurrView.setSelectDate((mCurrView.contains(mOnClickDate) ? mOnClickDate : mSelectDate), isDraw);
//        }
//        if (mLastView == null) {
//            mLastView = findViewWithTag(getCurrentItem() - 1);
//        }
//        if (mLastView != null) {
//            boolean isDraw = attrs.isDefaultSelect || (mLastView.contains(mOnClickDate));
//            mLastView.setSelectDate(getSelectDate((mLastView.contains(mOnClickDate) ? mOnClickDate : getLastSelectDate(mSelectDate))), isDraw);
//        }
//        if (mNextView == null) {
//            mNextView = findViewWithTag(getCurrentItem() + 1);
//        }
//        if (mNextView != null) {
//            boolean isDraw = attrs.isDefaultSelect || (mNextView.contains(mOnClickDate));
//            mNextView.setSelectDate(getSelectDate((mNextView.contains(mOnClickDate) ? mOnClickDate : getNextSelectDate(mSelectDate))), isDraw);
//        }
//    }

    //刷新所有页面
    public void notifyAllView() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseCalendarView calendarView = (BaseCalendarView) getChildAt(i);
            if (calendarView != null) {
                calendarView.invalidate();
            }
        }
    }

    //跳转
    protected void jumpDate(LocalDate localDate, boolean isDraw) {
//        localDate = getSelectDate(localDate);
//        int num = getTwoDateCount(mSelectDate, localDate, attrs.firstDayOfWeek);
//        onClickDate(localDate, num);
    }

    //点击处理
    protected void onClickDate(LocalDate localDate, int indexOffset) {
//        mOnClickDate = localDate;
//        mSelectDate = localDate;
//        //点击回调 先回调，等跳转之后callBackDate值已经变化，不会再重新回调
//        callBack(true, true);
//        if (indexOffset != 0) {
//            setCurrentItem(getCurrentItem() + indexOffset, Math.abs(indexOffset) == 1);
//        } else {
//          //  notifyView();
//        }
    }


    //回调
//    private void callBack(boolean isDraw, boolean isClick) {
//        if (!mSelectDate.equals(callBackDate)) {
//            //选中回调 ,绘制了才会回到
//            if (isDraw) {
//                onSelcetDate(Util.getNDate(mSelectDate), isClick);
//                callBackDate = mSelectDate;
//            }
//            //年月回调
//            onYearMonthChanged(mSelectDate, isClick);
//        }
//    }

    //日期边界处理
    private LocalDate getSelectDate(LocalDate localDate) {
        if (localDate.isBefore(startDate)) {
            return startDate;
        } else if (localDate.isAfter(endDate)) {
            return endDate;
        } else {
            return localDate;
        }
    }


    public List<LocalDate> getSelectDateList() {
        return mSelectDateList;
    }

    //回去viewpager的adapter
    protected abstract BaseCalendarAdapter getCalendarAdapter(Context context, LocalDate startDate, LocalDate endDate, LocalDate initializeDate, int firstDayOfWeek);

    //两个日期的相差数量
    protected abstract int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type);

    //相差count之后的的日期
    protected abstract LocalDate getDate(LocalDate localDate, int count);

    //重绘当前页面时，获取上个月选中的日期
    protected abstract LocalDate getLastSelectDate(LocalDate currectSelectDate);

    //重绘当前页面时，获取下个月选中的日期
    protected abstract LocalDate getNextSelectDate(LocalDate currectSelectDate);


    //日历上面选中的日期，有选中圈的才会回调
    protected abstract void onSelcetDate(NDate nDate);

    //年份和月份变化回调,点击和翻页都会回调，不管有没有日期选中
    public void onYearMonthChanged(LocalDate localDate, boolean isClick) {
        if (onYearMonthChangedListener != null) {
            onYearMonthChangedListener.onYearMonthChanged(this, localDate.getYear(), localDate.getMonthOfYear(), isClick);
        }
    }

    //点击的日期是否可用
    protected boolean isClickDateEnable(LocalDate localDate) {
        return !(localDate.isBefore(startDate) || localDate.isAfter(endDate));
    }

    //点击不可用的日期处理
    protected void onClickDisableDate(LocalDate localDate) {
        if (onClickDisableDateListener != null) {
            onClickDisableDateListener.onClickDisableDate(Util.getNDate(localDate));
        } else {
            Toast.makeText(getContext(), TextUtils.isEmpty(attrs.disabledString) ? "不可用" : attrs.disabledString, Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnYearMonthChangeListener(OnYearMonthChangedListener onYearMonthChangedListener) {
        this.onYearMonthChangedListener = onYearMonthChangedListener;
    }

    public void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener) {
        this.onClickDisableDateListener = onClickDisableDateListener;
    }

    //下一页，月日历即是下一月，周日历即是下一周
    public void toNextPager() {
        setCurrentItem(getCurrentItem() + 1, true);
    }

    //上一页
    public void toLastPager() {
        setCurrentItem(getCurrentItem() - 1, true);
    }


    //跳转日期
    public void jumpDate(String formatDate) {
//        LocalDate jumpDate;
//        try {
//            jumpDate = new LocalDate(formatDate);
//        } catch (Exception e) {
//            throw new RuntimeException("jumpDate的参数需要 yyyy-MM-dd 格式的日期");
//        }
//
//        jumpDate(jumpDate, true);
    }

    //回到今天
    public void toToday() {
        //  jumpDate(new LocalDate(), true);
    }

    //获取区间开始日期
    public LocalDate getStartDate() {
        return startDate;
    }

    //获取区间结束日期
    public LocalDate getEndDate() {
        return endDate;
    }

    //设置绘制类
    public void setCalendarPainter(CalendarPainter calendarPainter) {
        this.mCalendarPainter = calendarPainter;
        // notifyAllView();
    }

    //BaseCalendarView绘制获取mCalendarPainter
    public CalendarPainter getCalendarPainter() {
        return mCalendarPainter;
    }


    public void onClickCurrectMonthOrWeekDate(LocalDate localDate) {
        if (isMultiple) {
            //多选
            if (!mSelectDateList.contains(localDate)) {
                mSelectDateList.add(localDate);
            } else {
                mSelectDateList.remove(localDate);
            }
            notifyAllView();
        } else {
            //单选
            if (!mSelectDateList.contains(localDate)) {
                mSelectDateList.clear();
                mSelectDateList.add(localDate);
                notifyAllView();
            }
        }

    }

    public void onClickLastMonthDate(LocalDate localDate) {

        //点击上月，点击的日期不清楚，只翻页，如果需要清除，等到翻页之后点击删除
        if (isMultiple) {
            //多选
            if (!mSelectDateList.contains(localDate)) {
                mSelectDateList.add(localDate);
            }
        } else {
            //单选
            if (!mSelectDateList.contains(localDate)) {
                mSelectDateList.clear();
                mSelectDateList.add(localDate);
            }
        }
        setCurrentItem(getCurrentItem() - 1);

    }

    public void onClickNextMonthDate(LocalDate localDate) {

        //点击上月，点击的日期不清楚，只翻页，如果需要清除，等到翻页之后点击删除
        if (isMultiple) {
            //多选
            if (!mSelectDateList.contains(localDate)) {
                mSelectDateList.add(localDate);
            }
        } else {
            //单选
            if (!mSelectDateList.contains(localDate)) {
                mSelectDateList.clear();
                mSelectDateList.add(localDate);
            }
        }
        setCurrentItem(getCurrentItem() + 1);
    }


    public void jump(String s) {
        BaseCalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        LocalDate localDate = new LocalDate(s);
        int currNum = getTwoDateCount(localDate, currectCalendarView.getInitialDate(), attrs.firstDayOfWeek);//得出两个页面相差几个

        MyLog.d("currNum:333:" + currNum);
        MyLog.d("currNum:4444:" + localDate);

        if (isMultiple) {
            //多选
            if (!mSelectDateList.contains(localDate)) {
                mSelectDateList.add(localDate);
            }
        } else {
            //单选
            if (!mSelectDateList.contains(localDate)) {
                mSelectDateList.clear();
                mSelectDateList.add(localDate);
            }
        }
        setCurrentItem(getCurrentItem() - currNum);

    }

}
