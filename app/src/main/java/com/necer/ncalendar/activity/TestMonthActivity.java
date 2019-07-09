package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.BaseCalendar;
import com.necer.calendar.MonthCalendar;
import com.necer.enumeration.MultipleModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;
import com.necer.ncalendar.painter.LigaturePainter;
import com.necer.ncalendar.painter.TicketPainter;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMonthActivity extends AppCompatActivity {


    private TextView tv_result;
    private MonthCalendar monthCalendar;
    private final static String TAG = "NECER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        tv_result = findViewById(R.id.tv_result);


        monthCalendar = findViewById(R.id.monthCalendar);

        monthCalendar.setMultipleSelset(false);




        // monthCalendar.setMultipleNum(3,MultipleModel.FULL_REMOVE_FIRST);


        // monthCalendar.setDateInterval("2019-5-1","2019-5-20");
        //   monthCalendar.setInitializeDate("2019-6-2");

        monthCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);

                Log.d(TAG, "setOnCalendarChangedListener:::" + year + "年" + month + "月" + "   当前页面选中 " + localDate);
            }
        });

        monthCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currectSelectList, List<LocalDate> allSelectList) {

                Log.d(TAG, year + "年" + month + "月");
                Log.d(TAG, "当前页面选中：：" + currectSelectList);
                Log.d(TAG, "全部选中：：" + allSelectList);

            }
        });

    }

    public void lastMonth(View view) {
        monthCalendar.toLastPager();

//        Map<LocalDate, String> priceMap = new HashMap<>();
//        priceMap.put(new LocalDate("2019-06-07"), "￥350");
//        priceMap.put(new LocalDate("2019-07-07"), "￥350");
//        priceMap.put(new LocalDate("2019-06-30"), "￥350");
//        priceMap.put(new LocalDate("2019-07-03"), "￥350");
//        priceMap.put(new LocalDate("2019-07-04"), "￥350");
//        priceMap.put(new LocalDate("2019-07-10"), "￥350");
//        priceMap.put(new LocalDate("2019-07-15"), "￥350");
//        priceMap.put(new LocalDate("2019-07-30"), "￥350");
//        priceMap.put(new LocalDate("2019-08-04"), "￥350");
//        priceMap.put(new LocalDate("2019-08-29"), "￥350");
//
//
//        ticketPainter.setPriceMap(priceMap);

    }

    public void nextMonth(View view) {
        monthCalendar.toNextPager();
    }

    public void jump_2018_10_10(View view) {
        monthCalendar.jumpDate("2018-10-10");
    }

    public void jump_2019_10_10(View view) {
        monthCalendar.jumpDate("2019-10-10");
    }

    public void jump_2019_6_10(View view) {
        monthCalendar.jumpDate("2019-6-10");
    }


    public void today(View view) {
        monthCalendar.toToday();
    }
}
