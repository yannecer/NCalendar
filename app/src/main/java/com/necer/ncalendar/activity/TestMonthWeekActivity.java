package com.necer.ncalendar.activity;

import android.view.View;

import com.necer.calendar.MonthCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.entity.NDate;
import com.necer.listener.OnWeekSelectListener;
import com.necer.ncalendar.R;

/**
 * Created by necer on 2018/11/28.
 */
public class TestMonthWeekActivity extends BaseActivity {


    MonthCalendar monthCalendar;
    WeekCalendar weekCalendar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_month_week;
    }

    @Override
    protected void onCreatee() {

        monthCalendar = findViewById(R.id.monthCalendar);
        weekCalendar = findViewById(R.id.weekCalendar);


        weekCalendar.setOnWeekSelectListener(new OnWeekSelectListener() {
            @Override
            public void onWeekSelect(NDate date) {

            }
        });

    }

    public void toToday(View view) {
        weekCalendar.toToday();
        // monthCalendar.toToday();


    }
}
