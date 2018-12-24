package com.necer.ncalendar.activity;

import android.view.View;

import com.necer.calendar.Miui10Calendar;
import com.necer.ncalendar.R;
import com.necer.utils.Attrs;

/**
 * Created by necer on 2018/12/24.
 */
public class TestAddViewActivity extends BaseActivity {


    private Miui10Calendar miui10Calendar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_view;
    }

    @Override
    protected void onCreatee() {

        miui10Calendar = findViewById(R.id.miui10Calendar);
        findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (miui10Calendar.getState() == Attrs.MONTH) {
                    miui10Calendar.toWeek();
                } else {
                    miui10Calendar.toMonth();
                }
            }
        });
    }
}
