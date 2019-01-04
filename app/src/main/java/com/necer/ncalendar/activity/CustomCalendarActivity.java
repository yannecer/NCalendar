package com.necer.ncalendar.activity;

import com.necer.calendar.Miui10Calendar;
import com.necer.ncalendar.CustomPainter;
import com.necer.ncalendar.R;

/**
 * Created by necer on 2019/1/4.
 */
public class CustomCalendarActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom;
    }

    @Override
    protected void onCreatee() {


        Miui10Calendar miui10Calendar = findViewById(R.id.miui10Calendar);


        CustomPainter customPainter = new CustomPainter(this);


        miui10Calendar.setPainter(customPainter);


    }
}
