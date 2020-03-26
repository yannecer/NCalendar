package com.necer.calendar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.necer.R;
import com.necer.adapter.BasePagerAdapter;
import com.necer.enumeration.CalendarBuild;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.enumeration.MultipleNumModel;
import com.necer.enumeration.CheckModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.listener.OnMWDateChangeListener;
import com.necer.painter.CalendarAdapter;
import com.necer.painter.CalendarPainter;
import com.necer.painter.InnerPainter;
import com.necer.utils.Attrs;
import com.necer.utils.AttrsUtil;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public abstract class BaseCalendar extends ViewPager implements ICalendar {


    //5.0
    //修改部分参数命名
    //增加多选预置 可取消可不取消
    //重写InnerPainter
    //增加日期变化行为参数  1、点击选中  2、点击跳转（上下月的情况） 3、滑动翻页 4、api跳转 等
    //跳转日期不用post
    //优化adapter模式


    //变更
    //1、单选重复点击会重复回调


    private Context mContext;
    private Attrs mAttrs;
    private boolean mScrollEnable = true;
    private CheckModel mCheckModel;//选中模式

    private boolean mDefaultCheckedFirstDate;//默认选择时，翻页选中第一个日期

    protected OnClickDisableDateListener mOnClickDisableDateListener;//点击区间之外的日期回调
    private OnMWDateChangeListener mOnMWDateChangeListener;//月周切换是折叠中心的回调
    private OnCalendarChangedListener mOnCalendarChangedListener;//单选时回调，当前页面无选中返回null
    private OnCalendarMultipleChangedListener mOnCalendarMultipleChangedListener;//多选时回调

    protected LocalDate mStartDate, mEndDate, mInitializeDate;
    protected CalendarPainter mCalendarPainter;
    private List<LocalDate> mTotalCheckedDateList;

    private MultipleNumModel mMultipleNumModel;//多选数量模式
    private int mMultipleNum;//多选个数

    private int mFirstDayOfWeek;//日历的周一开始、周日开始
    private boolean mAllMonthSixLine;//月日历是否都是6行

    private CalendarBuild mCalendarBuild;

    private CalendarAdapter mCalendarAdapter;

    private int mCalendarPagerSize;//日历总页数
    private int mCalendarCurrIndex;//日历当前页码
    private boolean mLastNextMonthClickEnable;//上下月是否可点击

    private DateChangeBehavior mDateChangeBehavior;

    public BaseCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAttrs = AttrsUtil.getAttrs(context, attributeSet);
        this.mContext = context;
        mCheckModel = CheckModel.SINGLE_DEFAULT_CHECKED;
        mCalendarBuild = CalendarBuild.DRAW;
        mDateChangeBehavior = DateChangeBehavior.INITIALIZE;
        mTotalCheckedDateList = new ArrayList<>();
        mInitializeDate = new LocalDate();
        mStartDate = new LocalDate("1901-01-01");
        mEndDate = new LocalDate("2099-12-31");

        //背景颜色
        int calendarBgColor = mAttrs.bgCalendarColor;
        setBackgroundColor(calendarBgColor);

        mFirstDayOfWeek = mAttrs.firstDayOfWeek;
        mAllMonthSixLine = mAttrs.allMonthSixLine;
        mLastNextMonthClickEnable = mAttrs.lastNextMonthClickEnable;

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                post(() -> drawView(position));
            }

            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    mDateChangeBehavior = DateChangeBehavior.PAGE;
                }
            }
        });

        initAdapter();
    }


    private void initAdapter() {

        if (mCheckModel == CheckModel.SINGLE_DEFAULT_CHECKED) {
            mTotalCheckedDateList.clear();
            mTotalCheckedDateList.add(mInitializeDate);
        }

        if (mStartDate.isAfter(mEndDate)) {
            throw new IllegalArgumentException("startDate必须在endDate之前");
        }

        if (mStartDate.isBefore(new LocalDate("1901-01-01"))) {
            throw new IllegalArgumentException("startDate必须在1901-01-01之后");
        }

        if (mEndDate.isAfter(new LocalDate("2099-12-31"))) {
            throw new IllegalArgumentException("endDate必须在2099-12-31之前");
        }

        if (mStartDate.isAfter(mInitializeDate) || mEndDate.isBefore(mInitializeDate)) {
            throw new IllegalArgumentException("日期区间必须包含初始化日期");
        }

        mCalendarPagerSize = getTwoDateCount(mStartDate, mEndDate, mFirstDayOfWeek) + 1;
        mCalendarCurrIndex = getTwoDateCount(mStartDate, mInitializeDate, mFirstDayOfWeek);

        BasePagerAdapter calendarAdapter = getPagerAdapter(mContext, this);
        setAdapter(calendarAdapter);
        setCurrentItem(mCalendarCurrIndex);
    }


    @Override
    public void setDateInterval(String startFormatDate, String endFormatDate) {
        try {
            mStartDate = new LocalDate(startFormatDate);
            mEndDate = new LocalDate(endFormatDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("startDate、endDate需要 yyyy-MM-dd 格式的日期");
        }
        initAdapter();
    }

    @Override
    public void setInitializeDate(String formatInitializeDate) {
        try {
            mInitializeDate = new LocalDate(formatInitializeDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("setInitializeDate的参数需要 yyyy-MM-dd 格式的日期");
        }
        initAdapter();
    }

    @Override
    public void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate) {
        try {
            mStartDate = new LocalDate(startFormatDate);
            mEndDate = new LocalDate(endFormatDate);
            mInitializeDate = new LocalDate(formatInitializeDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("setInitializeDate的参数需要 yyyy-MM-dd 格式的日期");
        }
        initAdapter();
    }


    private void drawView(int position) {

        ICalendarView iCalendarView = findViewWithTag(position);

        if (iCalendarView == null) {
            return;
        }

        //单选并且手势左右滑动、toNextPager、toLastPager 才需要处理这里的日期
        if (mCheckModel == CheckModel.SINGLE_DEFAULT_CHECKED && mDateChangeBehavior == DateChangeBehavior.PAGE) {

            //当前页面初始化的日期
            LocalDate pagerInitialDate = iCalendarView.getPagerInitialDate();
            //上个页面选中的日期
            LocalDate lastDate = mTotalCheckedDateList.get(0);
            //当前面页面的初始值和上个页选中的日期，相差几月或几周，
            int dateCount = getTwoDateCount(lastDate, pagerInitialDate, mFirstDayOfWeek);
            //再由上个页面选中的日期得出当前页面选中的日期
            LocalDate tempLocalDate = getIntervalDate(lastDate, dateCount);
            //当前页面需要选中的日期
            LocalDate currentDate;
            //默认选中第一个
            if (mDefaultCheckedFirstDate) {
                currentDate = getFirstDate();
            } else {
                currentDate = tempLocalDate;
            }
            //判断日期是否在有效日期之内
            LocalDate localDate = getAvailableDate(currentDate);

            mTotalCheckedDateList.clear();
            mTotalCheckedDateList.add(localDate);
        }

        iCalendarView.notifyCalendarView();
        callBack();
    }


    public void onClickCurrentMonthOrWeekDate(LocalDate localDate) {
        mDateChangeBehavior = DateChangeBehavior.CLICK;
        jump(localDate, true);
    }

    public void onClickLastMonthDate(LocalDate localDate) {
        if (mLastNextMonthClickEnable && mScrollEnable) {
            mDateChangeBehavior = DateChangeBehavior.CLICK_PAGE;
            jump(localDate, true);
        }
    }

    public void onClickNextMonthDate(LocalDate localDate) {
        if (mLastNextMonthClickEnable && mScrollEnable) {
            mDateChangeBehavior = DateChangeBehavior.CLICK_PAGE;
            jump(localDate, true);
        }
    }

    public void jump(LocalDate localDate, boolean isCheck) {
        //判断日期是否合法
        if (!isAvailable(localDate)) {
            if (getVisibility() == VISIBLE) {
                if (mOnClickDisableDateListener != null) {
                    mOnClickDisableDateListener.onClickDisableDate(localDate);
                } else {
                    Toast.makeText(getContext(), TextUtils.isEmpty(mAttrs.disabledString) ? getResources().getString(R.string.N_disabledString) : mAttrs.disabledString, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
            int indexOffset = getTwoDateCount(localDate, iCalendarView.getPagerInitialDate(), mFirstDayOfWeek);//得出两个页面相差几个
            if (isCheck) {
                if (mCheckModel == CheckModel.MULTIPLE) {
                    if (!mTotalCheckedDateList.contains(localDate)) {
                        if (mTotalCheckedDateList.size() == mMultipleNum && mMultipleNumModel == MultipleNumModel.FULL_CLEAR) {
                            mTotalCheckedDateList.clear();
                        } else if (mTotalCheckedDateList.size() == mMultipleNum && mMultipleNumModel == MultipleNumModel.FULL_REMOVE_FIRST) {
                            mTotalCheckedDateList.remove(0);
                        }
                        mTotalCheckedDateList.add(localDate);
                    } else {
                        mTotalCheckedDateList.remove(localDate);
                    }
                } else {
                    //单选
                    mTotalCheckedDateList.clear();
                    mTotalCheckedDateList.add(localDate);
                }
            }

            if (indexOffset == 0) {
                drawView(getCurrentItem());
            } else {
                setCurrentItem(getCurrentItem() - indexOffset, Math.abs(indexOffset) == 1);
            }
        }
    }

    private void callBack() {
        ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
        LocalDate middleLocalDate = iCalendarView.getMiddleLocalDate();
        List<LocalDate> currPagerCheckDateList = iCalendarView.getCurrPagerCheckDateList();
        LocalDate yearMonthLocalDate;

        if (BaseCalendar.this instanceof MonthCalendar) {
            //月日历返回初始化的月份
            yearMonthLocalDate = iCalendarView.getPagerInitialDate();
        } else {
            if (currPagerCheckDateList.size() == 0) {
                yearMonthLocalDate = middleLocalDate;
            } else {
                yearMonthLocalDate = currPagerCheckDateList.get(0);
            }
        }

        //月周折叠日历使用
        if (mOnMWDateChangeListener != null) {
            mOnMWDateChangeListener.onMwDateChange(BaseCalendar.this, iCalendarView.getPivotDate(), mTotalCheckedDateList);
        }

        //单选
        if (mOnCalendarChangedListener != null && !(mCheckModel == CheckModel.MULTIPLE) && getVisibility() == VISIBLE) {
            mOnCalendarChangedListener.onCalendarChange(BaseCalendar.this, yearMonthLocalDate.getYear(), yearMonthLocalDate.getMonthOfYear(), currPagerCheckDateList.size() == 0 ? null : currPagerCheckDateList.get(0), mDateChangeBehavior);
        }

        //多选
        if (mOnCalendarMultipleChangedListener != null && mCheckModel == CheckModel.MULTIPLE && getVisibility() == VISIBLE) {
            mOnCalendarMultipleChangedListener.onCalendarChange(BaseCalendar.this, yearMonthLocalDate.getYear(), yearMonthLocalDate.getMonthOfYear(), currPagerCheckDateList, mTotalCheckedDateList, mDateChangeBehavior);
        }
    }


    @Override
    public void notifyCalendar() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ICalendarView) {
                ICalendarView iCalendarView = (ICalendarView) childAt;
                iCalendarView.notifyCalendarView();
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


    @Override
    public void toNextPager() {
        mDateChangeBehavior = DateChangeBehavior.PAGE;
        setCurrentItem(getCurrentItem() + 1, true);
    }

    @Override
    public void toLastPager() {
        mDateChangeBehavior = DateChangeBehavior.PAGE;
        setCurrentItem(getCurrentItem() - 1, true);
    }

    @Override
    public void toToday() {
        mDateChangeBehavior = DateChangeBehavior.API;
        jump(new LocalDate(), true);
    }


    @Override
    public void jumpDate(String formatDate) {
        mDateChangeBehavior = DateChangeBehavior.API;
        LocalDate jumpDate;
        try {
            jumpDate = new LocalDate(formatDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("jumpDate的参数需要 yyyy-MM-dd 格式的日期");
        }

        jump(jumpDate, true);
    }


    @Override
    public void jumpDate(int year, int month, int day) {
        mDateChangeBehavior = DateChangeBehavior.API;
        LocalDate jumpDate;
        try {
            jumpDate = new LocalDate(year, month, day);
        } catch (Exception e) {
            throw new IllegalArgumentException("jumpDate的参数需要正确的年月日数据");
        }
        jump(jumpDate, true);
    }

    @Override
    public void jumpMonth(int year, int month) {
        mDateChangeBehavior = DateChangeBehavior.API;
        LocalDate jumpDate;
        try {
            jumpDate = new LocalDate(year, month, 1);
        } catch (Exception e) {
            throw new IllegalArgumentException("jumpDate的参数需要正确的年月日数据");
        }
        jump(jumpDate, mCheckModel == CheckModel.SINGLE_DEFAULT_CHECKED);
    }

    @Override
    public List<LocalDate> getAllCheckedDateList() {
        return mTotalCheckedDateList;
    }

    //点击的日期是否可用
    public boolean isAvailable(LocalDate localDate) {
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


    public LocalDate getInitializeDate() {
        return mInitializeDate;
    }

    public int getCalendarCurrIndex() {
        return mCalendarCurrIndex;
    }

    public int getCalendarPagerSize() {
        return mCalendarPagerSize;
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    public boolean isAllMonthSixLine() {
        return mAllMonthSixLine;
    }

    public CalendarBuild getCalendarBuild() {
        return mCalendarBuild;
    }

    //设置绘制类
    @Override
    public void setCalendarPainter(CalendarPainter calendarPainter) {
        this.mCalendarBuild = CalendarBuild.DRAW;
        this.mCalendarPainter = calendarPainter;
        notifyCalendar();
    }


    @Override
    public void setCalendarAdapter(CalendarAdapter calendarAdapter) {
        this.mCalendarBuild = CalendarBuild.ADAPTER;
        this.mCalendarAdapter = calendarAdapter;
        notifyCalendar();
    }

    @Override
    public CalendarAdapter getCalendarAdapter() {
        return mCalendarAdapter;
    }

    @Override
    public CalendarPainter getCalendarPainter() {
        if (mCalendarPainter == null) {
            mCalendarPainter = new InnerPainter(getContext(), this);
        }
        return mCalendarPainter;
    }


    @Override
    public void updateSlideDistance(int currentDistance) {
        ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
        if (iCalendarView != null) {
            iCalendarView.updateSlideDistance(currentDistance);
        }
    }

    //月周切换时交换数据，保证月日历和周日历有相同的选中日期
    public void exchangeCheckedDateList(List<LocalDate> dateList) {
        mTotalCheckedDateList.clear();
        mTotalCheckedDateList.addAll(dateList);
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
        ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
        if (iCalendarView != null) {
            return iCalendarView.getCurrPagerFirstDate();
        }
        return null;
    }


    //获取PivotDate
    public LocalDate getPivotDate() {
        ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
        if (iCalendarView != null) {
            return iCalendarView.getPivotDate();
        }
        return null;
    }

    //localDate到顶部的距离
    public int getDistanceFromTop(LocalDate localDate) {
        ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
        if (iCalendarView != null) {
            return iCalendarView.getDistanceFromTop(localDate);
        }
        return 0;
    }

    //PivotDate到顶部的距离
    public int getPivotDistanceFromTop() {
        ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
        if (iCalendarView != null) {
            return iCalendarView.getPivotDistanceFromTop();
        }
        return 0;
    }

    //回去viewpager的adapter
    protected abstract BasePagerAdapter getPagerAdapter(Context context, BaseCalendar baseCalendar);

    //两个日期的相差数量
    protected abstract int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type);

    //相差count之后的的日期
    protected abstract LocalDate getIntervalDate(LocalDate localDate, int count);

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        if (!mIsInflateFinish) {
//            drawView(getCurrentItem());
//            mIsInflateFinish = true;
//        }
//    }

    @Override
    public List<LocalDate> getCurrPagerCheckDateList() {
        ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
        if (iCalendarView != null) {
            return iCalendarView.getCurrPagerCheckDateList();
        }
        return null;
    }

    @Override
    public List<LocalDate> getCurrPagerDateList() {
        ICalendarView iCalendarView = findViewWithTag(getCurrentItem());
        if (iCalendarView != null) {
            return iCalendarView.getCurrPagerDateList();
        }
        return null;
    }

    @Override
    public Attrs getAttrs() {
        return mAttrs;
    }


    @Override
    public void setLastNextMonthClickEnable(boolean enable) {
        this.mLastNextMonthClickEnable = enable;
    }

    @Override
    public void setCheckMode(CheckModel checkModel) {
        this.mCheckModel = checkModel;
        mTotalCheckedDateList.clear();
        if (mCheckModel == CheckModel.SINGLE_DEFAULT_CHECKED) {
            mTotalCheckedDateList.add(mInitializeDate);
        }
    }

    @Override
    public void setDefaultCheckedFirstDate(boolean isDefaultCheckedFirstDate) {
        this.mDefaultCheckedFirstDate = isDefaultCheckedFirstDate;
    }

    @Override
    public void setMultipleNum(int multipleNum, MultipleNumModel multipleNumModel) {
        this.mCheckModel = CheckModel.MULTIPLE;
        this.mMultipleNumModel = multipleNumModel;
        this.mMultipleNum = multipleNum;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollEnable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public void setScrollEnable(boolean scrollEnable) {
        this.mScrollEnable = scrollEnable;
    }
}
