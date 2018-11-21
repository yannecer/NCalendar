package com.necer.ncalendar.activity;

import android.widget.TextView;

import com.necer.calendar.EmuiCalendar;
import com.necer.entity.NDate;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.ncalendar.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;


/**
 * Created by necer on 2018/11/12.
 */
public class TestEmuiActivity extends BaseActivity {



    TextView tv_lunar;
    TextView tv_date;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_emui;
    }

    @Override
    protected void onCreatee() {

        tv_lunar = findViewById(R.id.tv_lunar);
        tv_date = findViewById(R.id.tv_date);


        EmuiCalendar emuiCalendar = findViewById(R.id.emuiCalendar);
        emuiCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarDateChanged(NDate date) {
                tv_date.setText(date.localDate.getYear() + "年" + date.localDate.getMonthOfYear() + "月");
                tv_lunar.setText((Days.daysBetween(new LocalDate(), date.localDate).getDays() + "天后  ") + "农历" + date.lunar.lunarYearStr + "年 " + date.lunar.lunarMonthStr + date.lunar.lunarDayStr);
            }

            @Override
            public void onCalendarStateChanged(boolean isMonthSate) {

            }
        });
    }
}
