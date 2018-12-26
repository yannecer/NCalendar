package com.necer.ncalendar.activity;

import android.view.View;
import android.widget.TextView;

import com.necer.calendar.MonthCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.entity.NDate;
import com.necer.listener.OnMonthSelectListener;
import com.necer.ncalendar.R;

/**
 * Created by necer on 2018/11/28.
 */
public class TestMonthWeekActivity extends BaseActivity {


    MonthCalendar monthCalendar;
    WeekCalendar weekCalendar;

    TextView tv_date;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_month_week;
    }

    @Override
    protected void onCreatee() {

        monthCalendar = findViewById(R.id.monthCalendar);
        weekCalendar = findViewById(R.id.weekCalendar);

        tv_date = findViewById(R.id.tv_date);


        /*app:startDate="2018-12-2"
        app:endDate="2018-12-31"*/
     //    monthCalendar.setDateInterval("2018-12-01", "2018-12-31");
        monthCalendar.setDateInterval("1901-01-01", "2099-12-31");

        // monthCalendar.setInitializeDate("2018-12-11");
        monthCalendar.setOnMonthSelectListener(new OnMonthSelectListener() {
            @Override
            public void onMonthSelect(NDate date) {
                tv_date.setText(date.localDate + "");
            }
        });

      /*  weekCalendar.setOnWeekSelectListener(new OnWeekSelectListener() {
            @Override
            public void onWeekSelect(NDate date) {

                tv_date.setText(date.localDate + "");
            }
        });*/

    }

    public void toToday(View view) {
        //  weekCalendar.toToday();
        // monthCalendar.toToday();

        //  weekCalendar.jumpDate("2018-01-31");
        monthCalendar.setVisibility(View.VISIBLE);
    }
}
