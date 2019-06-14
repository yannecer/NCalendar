package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.MonthCalendar;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.MyPagerAdapter;

import org.joda.time.LocalDate;

public class TestActivity extends AppCompatActivity {



    MonthCalendar monthCalendar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        monthCalendar = findViewById(R.id.monthCalendar);


        monthCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                MyLog.d("BaseCalendar::" + localDate);
            }
        });


      //  aaa = findViewById(R.id.aaa);

//
//
//        ViewPager view_pager = findViewById(R.id.view_pager);
//
//
//        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this);
//
//        view_pager.setAdapter(myPagerAdapter);
//

    }


    public void aaa(View view) {

        monthCalendar.setVisibility(View.VISIBLE);
    }
}
