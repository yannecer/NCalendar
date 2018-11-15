package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.MonthCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.listener.OnDateChangedListener;
import com.necer.listener.OnMonthSelectListener;
import com.necer.listener.OnWeekSelectListener;
import com.necer.listener.OnYearMonthChangedListener;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import necer.ncalendardemo.R;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class TestActivity extends AppCompatActivity {
    MonthCalendar monthcalendar;
    WeekCalendar weekcalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_new);


        monthcalendar = findViewById(R.id.monthcalendar);
        weekcalendar = findViewById(R.id.weekcalendar);


        List<String> list = new ArrayList<>();
        list.add("2018-9-10");
        list.add("2018-9-12");
        list.add("2018-9-13");
        list.add("2018-10-10");
        list.add("2018-10-12");
        list.add("2018-10-16");
        list.add("2018-11-6");
        list.add("2018-11-7");

        monthcalendar.setPointList(list);
        weekcalendar.setPointList(list);


        monthcalendar.setOnMonthSelectListener(new OnMonthSelectListener() {
            @Override
            public void onMonthSelect(LocalDate date) {
                MyLog.d("datedatedatedate:  月:" + date);

            }
        });


        findViewById(R.id.bt)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        monthcalendar.jumpDate("2019-05-01");
                    }
                });

        weekcalendar.setOnWeekSelectListener(new OnWeekSelectListener() {
            @Override
            public void onWeekSelect(LocalDate date) {
                MyLog.d("datedatedatedate  周   ：：" + date);
            }
        });


        monthcalendar.setOnYearMonthChangeListener(new OnYearMonthChangedListener() {
            @Override
            public void onYearMonthChanged(BaseCalendar baseCalendar, int year, int month) {
                MyLog.d("OnYearMonthChangeListener:月::" + year + "-----" + month);
            }


        });
        weekcalendar.setOnYearMonthChangeListener(new OnYearMonthChangedListener() {
            @Override
            public void onYearMonthChanged(BaseCalendar baseCalendar,int year, int month) {
                MyLog.d("OnYearMonthChangeListener:周::" + year + "-----" + month);
            }
        });

/*

        monthcalendar.setOnDateChangedListener(new OnDateChangedListener() {
            @Override
            public void onDateChanged(BaseCalendar baseCalendar, LocalDate localDate) {
                MyLog.d("OnDateChangedListener:月:" + localDate);
            }


        });

         weekcalendar.setOnDateChangedListener(new OnDateChangedListener() {
            @Override
            public void onDateChanged(BaseCalendar baseCalendar, LocalDate localDate) {
                MyLog.d("OnDateChangedListener:周:" + localDate);
            }


        });
*/





    }
}
