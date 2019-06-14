package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.necer.calendar.Miui10Calendar;
import com.necer.ncalendar.CustomPainter;
import com.necer.ncalendar.R;
import com.necer.painter.CalendarPainter;

/**
 * Created by necer on 2019/1/4.
 */
public class CustomCalendarActivity extends AppCompatActivity {

    Miui10Calendar miui10Calendar;
    CustomPainter customPainter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        miui10Calendar = findViewById(R.id.miui10Calendar);

        customPainter = new CustomPainter(this);
        miui10Calendar.setCalendarPainter(customPainter);

    }
}
