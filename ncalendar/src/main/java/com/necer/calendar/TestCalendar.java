package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.MyLog;
import com.necer.listener.OnDateChangeListener;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.List;

public class TestCalendar extends FrameLayout {



    protected WeekCalendar weekCalendar;
    protected MonthCalendar monthCalendar;
    protected int STATE=Attrs.WEEK;//默认月


    public TestCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        weekCalendar = new WeekCalendar(context, attrs);

        monthCalendar = new MonthCalendar(context, attrs);
        monthCalendar.setOnDateChangeListener(onDateChangeListener);
        weekCalendar.setOnDateChangeListener(onDateChangeListener);

        addView(monthCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Util.dp2px(context,300)));
        addView(weekCalendar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  (int) Util.dp2px(context,60)));


    }



    private OnDateChangeListener onDateChangeListener = new OnDateChangeListener() {
        @Override
        public void onDateChange(BaseCalendar baseCalendar, final LocalDate localDate, List<LocalDate> dateList) {

            MyLog.d("baseCalendar:1111::" + baseCalendar);
            MyLog.d("baseCalendar:2222::" + localDate);

            if (baseCalendar == monthCalendar && STATE == Attrs.MONTH) {
                //月日历变化,改变周的选中
                weekCalendar.jump(localDate,false);
                weekCalendar.setSelectDateList(dateList);
            } else if (baseCalendar == weekCalendar && STATE == Attrs.WEEK) {
                //周日历变化，改变月的选中
                monthCalendar.jump(localDate,false);

                MyLog.d("localDatelocalDatelocalDatelocalDate::1111:" + localDate);

                monthCalendar.setSelectDateList(dateList);
                post(new Runnable() {
                    @Override
                    public void run() {
                        //此时需要根据月日历的选中日期调整值
                        // post是因为在前面得到当前view是再post中完成，如果不这样直接获取位置信息，会出现老的数据，不能获取正确的数据
                     //   monthCalendar.setY(getMonthYOnWeekState(localDate));

                        monthCalendar.setLocation(localDate);

                        MyLog.d("localDatelocalDatelocalDatelocalDate::::2222::" + monthCalendar.getY());
                    }
                });
            }

        }
    };
}
