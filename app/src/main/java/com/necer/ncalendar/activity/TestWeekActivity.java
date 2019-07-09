package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.BaseCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;

import org.joda.time.LocalDate;

import java.util.List;

public class TestWeekActivity extends BaseActivity {


    private TextView tv_result;
    private WeekCalendar weekCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        tv_result = findViewById(R.id.tv_result);


        weekCalendar = findViewById(R.id.weekCalendar);
        weekCalendar.setSelectedMode(selectedModel);
        weekCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);

                Log.d(TAG, "setOnCalendarChangedListener:::" + year + "年" + month + "月" + "   当前页面选中 " + localDate);
            }
        });

        weekCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currectSelectList, List<LocalDate> allSelectList) {
                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currectSelectList.size() + "个  总共选中" + allSelectList.size() + "个");

                Log.d(TAG, year + "年" + month + "月");
                Log.d(TAG, "当前页面选中：：" + currectSelectList);
                Log.d(TAG, "全部选中：：" + allSelectList);

            }
        });

    }

    public void lastMonth(View view) {
        weekCalendar.toLastPager();
    }

    public void nextMonth(View view) {
        weekCalendar.toNextPager();
    }

    public void jump_2018_10_10(View view) {
        weekCalendar.jumpDate("2018-10-10");
    }

    public void jump_2019_10_10(View view) {
        weekCalendar.jumpDate("2019-10-10");
    }

    public void jump_2019_6_10(View view) {
        weekCalendar.jumpDate("2019-6-10");
    }


    public void today(View view) {
        weekCalendar.toToday();
    }
}
