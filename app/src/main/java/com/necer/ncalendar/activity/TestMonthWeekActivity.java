package com.necer.ncalendar.activity;

import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.MonthCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.listener.OnCalendarChangeListener;
import com.necer.ncalendar.R;

import org.joda.time.LocalDate;

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
        monthCalendar = findViewById(R.id.monthCalendar);
        // weekCalendar = findViewById(R.id.weekCalendar);


        monthCalendar.setOnCalendarChangeListener(new OnCalendarChangeListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currentSelectDateList, List<LocalDate> allSelectDateList) {
                MyLog.d("onCalendarChange:11::" + year);
                MyLog.d("onCalendarChange:22::" + month);
                MyLog.d("onCalendarChange:33::" + currentSelectDateList);
                MyLog.d("onCalendarChange:44::" + allSelectDateList);
            }
        });


    }

    public void toToday(View view) {
        monthCalendar.jump(new LocalDate("2018-10-10"),false);

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
