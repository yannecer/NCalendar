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
import com.necer.listener.OnCalendarChangeListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.listener.OnDateChangeListener;
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

    //这两个不能同时为真
    private boolean isDefaultSelect = false; //默认选中 不能多选
    private boolean isMultiple = true;  // 多选情况下 不能默认选中


    protected OnYearMonthChangedListener onYearMonthChangedListener;
    protected OnClickDisableDateListener onClickDisableDateListener;

    protected LocalDate startDate, endDate, initializeDate;

    protected CalendarPainter mCalendarPainter;

    private List<LocalDate> mAllSelectDateList;


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
        mAllSelectDateList = new ArrayList<>();
        mAllSelectDateList.add(new LocalDate());

        post(new Runnable() {
            @Override
            public void run() {
                drawView(getCurrentItem());
            }
        });

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {

                drawView(position);

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

        MyLog.d("currItem:::::" + currItem);

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
            LocalDate lastDate = mAllSelectDateList.get(0);//上个页面选中的日期
            //当前面页面的初始值和上个页选中的日期，相差几月或几周，再又上个页面选中的日期得出当前页面选中的日期
            int currNum = getTwoDateCount(lastDate, initialDate, attrs.firstDayOfWeek);//得出两个页面相差几个
            LocalDate localDate = getDate(lastDate, currNum);
            mAllSelectDateList.clear();
            mAllSelectDateList.add(localDate);
            currectCalendarView.invalidate();
        } else {//不默认选中，分为两种情况，1、单选 2、多选  这两种情况只需要绘制页面

            currectCalendarView.invalidate();
        }

        callBack();
    }


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
//    protected void jumpDate(LocalDate localDate, boolean isDraw) {
////        localDate = getSelectDate(localDate);
////        int num = getTwoDateCount(mSelectDate, localDate, attrs.firstDayOfWeek);
////        onClickDate(localDate, num);
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


    public List<LocalDate> getAllSelectDateList() {
        return mAllSelectDateList;
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
        LocalDate jumpDate;
        try {
            jumpDate = new LocalDate(formatDate);
        } catch (Exception e) {
            throw new RuntimeException("jumpDate的参数需要 yyyy-MM-dd 格式的日期");
        }

        jump(jumpDate,true);
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
            //多选  集合不包含就添加，包含就移除
            if (!mAllSelectDateList.contains(localDate)) {
                mAllSelectDateList.add(localDate);
            } else {
                mAllSelectDateList.remove(localDate);
            }
            notifyAllView();
            //当前页面选中改变，需要回调
            post(new Runnable() {
                @Override
                public void run() {
                    callBack();
                }
            });
        } else {
            //单选  集合中有且只有一个元素 ，不包含就清空集合添加，包含该元素不做处理，因为前面已经绘制
            if (!mAllSelectDateList.contains(localDate)) {
                mAllSelectDateList.clear();
                mAllSelectDateList.add(localDate);
                notifyAllView();
                //当前页面选中改变，需要回调
                post(new Runnable() {
                    @Override
                    public void run() {
                        callBack();
                    }
                });

            }
        }
    }

    public void onClickLastMonthDate(LocalDate localDate) {
        jump(localDate,true);
    }


    public void onClickNextMonthDate(LocalDate localDate) {
        jump(localDate,true);
    }


    public void jump(LocalDate localDate,boolean isDraw) {
        BaseCalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        int indexOffset = getTwoDateCount(localDate, currectCalendarView.getInitialDate(), attrs.firstDayOfWeek);//得出两个页面相差几个
        if (isMultiple) {
            //多选  点击的日期不清除，只翻页，如果需要清除，等到翻页之后再次点击
            if (!mAllSelectDateList.contains(localDate) && isDraw) {
                mAllSelectDateList.add(localDate);
            }
        } else {
            //单选
            if (!mAllSelectDateList.contains(localDate)&& isDraw) {
                mAllSelectDateList.clear();
                mAllSelectDateList.add(localDate);
            }
        }
        setCurrentItem(getCurrentItem() - indexOffset, Math.abs(indexOffset) == 1);
    }

    private void callBack() {
        BaseCalendarView currectCalendarView = findViewWithTag(getCurrentItem());
        LocalDate initialDate = currectCalendarView.getInitialDate();
        if (onCalendarChangeListener != null) {
            onCalendarChangeListener.onCalendarChange(this, initialDate.getYear(), initialDate.getMonthOfYear(), currectCalendarView.getCurrentSelectDateList(),mAllSelectDateList);
        }

        if (onDateChangeListener != null) {
            onDateChangeListener.onDateChange(this,currectCalendarView.getPivot(), mAllSelectDateList);
        }


        MyLog.d("initialDate:11111::"+initialDate);
     //   MyLog.d("initialDate:2222::"+initialDate);



    }

    public void setSelectDateList(List<LocalDate> dateList) {
        mAllSelectDateList.clear();
        mAllSelectDateList.addAll(dateList);
        notifyAllView();
    }



    private OnCalendarChangeListener onCalendarChangeListener;
    private OnDateChangeListener onDateChangeListener;


    public void setOnCalendarChangeListener(OnCalendarChangeListener onCalendarChangeListener) {
        this.onCalendarChangeListener = onCalendarChangeListener;
    }

    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }

}
