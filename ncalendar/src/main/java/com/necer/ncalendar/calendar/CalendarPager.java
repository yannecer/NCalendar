package com.necer.ncalendar.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.CalendarAdapter;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.CalendarView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public abstract class CalendarPager extends ViewPager {

    protected CalendarAdapter calendarAdapter;
    protected DateTime startDateTime;
    protected DateTime endDateTime;
    protected int mPageSize;
    protected int mCurrPage;
    protected DateTime mInitialDateTime;//日历初始化datetime，即今天
    protected DateTime mSelectDateTime;//当前页面选中的datetime
    protected List<String> pointList;//圆点

    protected boolean isPagerChanged = true;//是否是手动翻页
    protected DateTime lastSelectDateTime;//上次选中的datetime
    protected boolean isDefaultSelect = true;//是否默认选中


    private OnPageChangeListener onPageChangeListener;

    public CalendarPager(Context context) {
        this(context, null);
    }

    public CalendarPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        Attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, getResources().getColor(R.color.solarTextColor));
        Attrs.lunarTextColor = ta.getColor(R.styleable.NCalendar_lunarTextColor, getResources().getColor(R.color.lunarTextColor));
        Attrs.selectCircleColor = ta.getColor(R.styleable.NCalendar_selectCircleColor, getResources().getColor(R.color.selectCircleColor));
        Attrs.hintColor = ta.getColor(R.styleable.NCalendar_hintColor, getResources().getColor(R.color.hintColor));
        Attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, Utils.sp2px(context, 18));
        Attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, Utils.sp2px(context, 10));
        Attrs.selectCircleRadius = ta.getInt(R.styleable.NCalendar_selectCircleRadius, (int) Utils.dp2px(context, 20));
        Attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, true);

        Attrs.pointSize = ta.getDimension(R.styleable.NCalendar_pointSize, (int) Utils.dp2px(context, 2));
        Attrs.pointColor = ta.getColor(R.styleable.NCalendar_pointColor, getResources().getColor(R.color.pointColor));
        Attrs.hollowCircleColor = ta.getColor(R.styleable.NCalendar_hollowCircleColor, Color.WHITE);
        Attrs.hollowCircleStroke = ta.getInt(R.styleable.NCalendar_hollowCircleStroke, (int) Utils.dp2px(context, 1));


        Attrs.monthCalendarHeight = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, Utils.dp2px(context, 300));
        Attrs.duration = ta.getInt(R.styleable.NCalendar_duration, 240);

        Attrs.isShowHoliday = ta.getBoolean(R.styleable.NCalendar_isShowHoliday, true);
        Attrs.holidayColor = ta.getColor(R.styleable.NCalendar_holidayColor, getResources().getColor(R.color.holidayColor));
        Attrs.workdayColor = ta.getColor(R.styleable.NCalendar_workdayColor, getResources().getColor(R.color.workdayColor));

        Attrs.backgroundColor = ta.getColor(R.styleable.NCalendar_backgroundColor, getResources().getColor(R.color.white));

        String firstDayOfWeek = ta.getString(R.styleable.NCalendar_firstDayOfWeek);
        String defaultCalendar = ta.getString(R.styleable.NCalendar_defaultCalendar);

        String startString = ta.getString(R.styleable.NCalendar_startDate);
        String endString = ta.getString(R.styleable.NCalendar_endDate);

        Attrs.firstDayOfWeek = "Monday".equals(firstDayOfWeek) ? 1 : 0;
        Attrs.defaultCalendar = "Week".equals(defaultCalendar) ? NCalendar.WEEK : NCalendar.MONTH;

        ta.recycle();

        mInitialDateTime = new DateTime().withTimeAtStartOfDay();

        startDateTime = new DateTime(startString == null ? "1901-01-01" : startString);
        endDateTime = new DateTime(endString == null ? "2099-12-31" : endString);

        setDateInterval(null, null);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initCurrentCalendarView(mCurrPage);
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        setBackgroundColor(Attrs.backgroundColor);
    }

    public void setDateInterval(String startString,String endString) {
        if (startString != null && !"".equals(startString)) {
            startDateTime = new DateTime(startString);
        }
        if (endString != null && !"".equals(endString)) {
            endDateTime = new DateTime(endString);
        }


        if (mInitialDateTime.getMillis() < startDateTime.getMillis() || mInitialDateTime.getMillis() > endDateTime.getMillis()) {
            throw new RuntimeException(getResources().getString(R.string.range_date));
        }

        calendarAdapter = getCalendarAdapter();
        setAdapter(calendarAdapter);
        setCurrentItem(mCurrPage);


        if (onPageChangeListener != null) {
            removeOnPageChangeListener(onPageChangeListener);
        }

        onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initCurrentCalendarView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        addOnPageChangeListener(onPageChangeListener);

    }




    protected abstract CalendarAdapter getCalendarAdapter();

    protected abstract void initCurrentCalendarView(int position);

    protected abstract void setDateTime(DateTime dateTime);

    public void toToday() {
        setDateTime(new DateTime().withTimeAtStartOfDay());
    }


    /**
     * 下一页，月日历即是下一月，周日历即是下一周
     */
    public void toNextPager() {
        setCurrentItem(getCurrentItem() + 1, true);
    }

    /**
     * 上一页
     */
    public void toLastPager() {
        setCurrentItem(getCurrentItem() - 1, true);
    }

    //设置日期
    public void setDate(String formatDate) {
        setDateTime(new DateTime(formatDate));
    }

    public void setPointList(List<String> pointList) {

        List<String> formatList = new ArrayList<>();
        for (int i = 0; i < pointList.size(); i++) {
            String format = new DateTime(pointList.get(i)).toString("yyyy-MM-dd");
            formatList.add(format);
        }

        this.pointList = formatList;
        CalendarView calendarView = calendarAdapter.getCalendarViews().get(getCurrentItem());
        if (calendarView == null) {
            return;
        }
        calendarView.setPointList(formatList);
    }

    public void setDefaultSelect(boolean defaultSelect) {
        isDefaultSelect = defaultSelect;
    }




}
