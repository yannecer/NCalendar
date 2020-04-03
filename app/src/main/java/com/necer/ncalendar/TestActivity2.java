package com.necer.ncalendar;

import android.os.Bundle;
import android.util.Log;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.MonthCalendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;

import org.joda.time.LocalDate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by necer on 2020/3/24.
 */
public class TestActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);


       MonthCalendar monthCalendar = findViewById(R.id.monthCalendar);
       monthCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
           @Override
           public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
               MyLog.d("onCalendarChange:::"+localDate);
           }

       });

    }
}
