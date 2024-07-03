package com.necer.ncalendar.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.NCalendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;


import java.time.LocalDate;
import java.util.List;

public class TestWeekActivity extends BaseActivity {


    private TextView tv_result;
    private NCalendar weekCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        tv_result = findViewById(R.id.tv_result);

        weekCalendar = findViewById(R.id.weekCalendar);
        weekCalendar.setCheckMode(checkModel);
        weekCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);

                Log.d(TAG, "setOnCalendarChangedListener:::" + year + "年" + month + "月" + "   当前页面选中 " + localDate);
            }


        });

        weekCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");

                Log.d(TAG, year + "年" + month + "月");
                Log.d(TAG, "当前页面选中：：" + currPagerCheckedList);
                Log.d(TAG, "全部选中：：" + totalCheckedList);
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
