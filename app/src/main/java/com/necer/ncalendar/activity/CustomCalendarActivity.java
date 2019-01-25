package com.necer.ncalendar.activity;

import android.view.View;

import com.necer.calendar.Miui10Calendar;
import com.necer.ncalendar.CustomPainter;
import com.necer.ncalendar.R;
import com.necer.painter.CalendarPainter;

/**
 * Created by necer on 2019/1/4.
 */
public class CustomCalendarActivity extends BaseActivity{


    Miui10Calendar miui10Calendar;
    CustomPainter customPainter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom;
    }

    @Override
    protected void onCreatee() {

        miui10Calendar = findViewById(R.id.miui10Calendar);
        customPainter = new CustomPainter(this);
        miui10Calendar.setCalendarPainter(customPainter);

    }

}
