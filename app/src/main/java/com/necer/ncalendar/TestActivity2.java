package com.necer.ncalendar;

import android.os.Bundle;

import com.necer.calendar.NCalendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

/**
 * Created by necer on 2020/3/24.
 */
public class TestActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);


       NCalendar monthCalendar = findViewById(R.id.monthCalendar);
       monthCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
           @Override
           public void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {

           }

       });

    }
}
