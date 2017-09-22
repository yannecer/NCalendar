package com.necer.ncalendar.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.NCalendarAdapter;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.NCalendarView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public abstract class NCalendarPager extends ViewPager {

    protected NCalendarAdapter calendarAdapter;
    protected DateTime startDateTime;
    protected DateTime endDateTime;
    protected int mPageSize;
    protected int mCurrPage;
    protected boolean isSetDateTime;
    protected DateTime mInitialDateTime;//日历初始化datetime，即今天
    protected DateTime mSelectDateTime;//当前页面选中的datetime

    protected List<String> pointList;//圆点




    public NCalendarPager(Context context) {
        this(context, null);
    }

    public NCalendarPager(Context context, AttributeSet attrs) {
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
        Attrs.workdayColor = ta.getColor(R.styleable.NCalendar_holidayColor, getResources().getColor(R.color.workdayColor));



        String firstDayOfWeek = ta.getString(R.styleable.NCalendar_firstDayOfWeek);
        String defaultCalendar = ta.getString(R.styleable.NCalendar_defaultCalendar);

        Attrs.firstDayOfWeek = "Monday".equals(firstDayOfWeek) ? 1 : 0;
        Attrs.defaultCalendar = "Week".equals(defaultCalendar) ? NCalendar.WEEK : NCalendar.MONTH;

        ta.recycle();

        mInitialDateTime = new DateTime();
        startDateTime = new DateTime("1901-01-01");
        endDateTime = new DateTime("2099-12-31");





        calendarAdapter = getCalendarAdapter();
        setAdapter(calendarAdapter);
        setCurrentItem(mCurrPage);

        addOnPageChangeListener(new OnPageChangeListener() {
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
        });


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initCurrentCalendarView(mCurrPage);
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        setBackgroundColor(Color.WHITE);
    }


    protected abstract NCalendarAdapter getCalendarAdapter();

    protected abstract void initCurrentCalendarView(int position);

    protected abstract void setDateTime(DateTime dateTime);

    //设置日期
    public void setDate(String formatDate) {
        setDateTime(new DateTime(formatDate));
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setPointList(List<String> pointList) {

        List<String> formatList = new ArrayList<>();
        for (int i = 0; i < pointList.size(); i++) {
            String format = new DateTime(pointList.get(i)).toString("yyyy-MM-dd");
            formatList.add(format);
        }

        this.pointList = formatList;
        NCalendarView nCalendarView = calendarAdapter.getCalendarViews().get(getCurrentItem());
        if (nCalendarView == null) {
            return;
        }
        nCalendarView.setPointList(formatList);
    }


    private boolean isScrollEnable = true;

    public void setScrollEnable(boolean isScrollEnable) {
        this.isScrollEnable = isScrollEnable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollEnable ? super.onTouchEvent(ev) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollEnable ? super.onInterceptTouchEvent(ev) : false;
    }

}
