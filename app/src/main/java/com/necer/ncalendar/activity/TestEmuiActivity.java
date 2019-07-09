package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.BaseCalendar;
import com.necer.calendar.EmuiCalendar;
import com.necer.enumeration.CalendarState;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * Created by necer on 2018/11/12.
 */
public class TestEmuiActivity extends BaseActivity {

    private EmuiCalendar emuiCalendar;

    private TextView tv_result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emui);

        emuiCalendar = findViewById(R.id.emuiCalendar);

        tv_result = findViewById(R.id.tv_result);

        emuiCalendar.setSelectedMode(selectedModel);
        emuiCalendar.setDefaultSelectFitst(true);//只在selectedMode==SINGLE_SELECTED有效

        emuiCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
                Log.d(TAG, "当前页面选中：：" + localDate);
            }
        });
        emuiCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currectSelectList, List<LocalDate> allSelectList) {

                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currectSelectList.size() + "个  总共选中" + allSelectList.size() + "个");
                Log.d(TAG, year + "年" + month + "月");
                Log.d(TAG, "当前页面选中：：" + currectSelectList);
                Log.d(TAG, "全部选中：：" + allSelectList);
            }
        });


    }

    public void lastPager(View view) {
        emuiCalendar.toLastPager();
    }

    public void nextPager(View view) {
        emuiCalendar.toNextPager();
    }

    public void today(View view) {
        emuiCalendar.toToday();
    }


    public void fold(View view) {
        if (emuiCalendar.getCalendarState() == CalendarState.WEEK) {
            emuiCalendar.toMonth();
        } else {
            emuiCalendar.toWeek();
        }
    }
}
