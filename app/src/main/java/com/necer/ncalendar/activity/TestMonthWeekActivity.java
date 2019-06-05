package com.necer.ncalendar.activity;

import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.MonthCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.entity.NDate;
import com.necer.listener.OnMonthSelectListener;
import com.necer.listener.OnYearMonthChangedListener;
import com.necer.ncalendar.CustomPainter;
import com.necer.ncalendar.R;
import com.necer.painter.CalendarPainter;
import com.necer.painter.InnerPainter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
//
     //   monthCalendar = findViewById(R.id.monthCalendar);
       // weekCalendar = findViewById(R.id.weekCalendar);
//
//        tv_date = findViewById(R.id.tv_date);
//
//        //  monthCalendar.setDateInterval("2018-12-01", "2019-01-2");
//
//        // monthCalendar.setPainter(new CustomPainter(this));
//
//
//        monthCalendar.setOnMonthSelectListener(new OnMonthSelectListener() {
//            @Override
//            public void onMonthSelect(NDate date) {
//
//                MyLog.d("onMonthSelect::::" + date.localDate);
//                MyLog.d("onMonthSelect::222::" );
//            }
//        });
//
//
//        monthCalendar.setOnYearMonthChangeListener(new OnYearMonthChangedListener() {
//            @Override
//            public void onYearMonthChanged(BaseCalendar baseCalendar, int year, int month, boolean isClick) {
//            }
//        });

    }

    public void toToday(View view) {


      //  monthCalendar.jump("2019-01-10");



        //  weekCalendar.toToday();
        // monthCalendar.toToday();

        //  weekCalendar.jumpDate("2018-01-31");
        // monthCalendar.setVisibility(View.VISIBLE);

        //  monthCalendar.jumpDate("2019-12-31");

//
//        List<String> list = new ArrayList<>();
//
//        list.add("2018-10-14");
//        list.add("2018-12-18");
//        list.add("2019-01-14");
//        list.add("2019-02-15");
//        list.add("2019-04-15");


     //   monthCalendar.setPointList(list);

     //   monthCalendar.notifyAllView();

    }
}
