package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.necer.calendar.Miui10Calendar;
import com.necer.ncalendar.R;
import com.necer.utils.Attrs;

public class TestAddViewActivity extends AppCompatActivity {

    private Miui10Calendar miui10Calendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view);

        miui10Calendar = findViewById(R.id.miui10Calendar);
        findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (miui10Calendar.getCalendarState() == Attrs.MONTH) {
                    miui10Calendar.toWeek();
                } else {
                    miui10Calendar.toMonth();
                }
            }
        });
    }
}
