package com.necer.ncalendar.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.NCalendar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;


import java.time.LocalDate;
import java.util.List;


/**
 * Created by necer on 2018/11/12.
 */
public class TestEmuiActivity extends BaseActivity {

    private NCalendar emuiCalendar;

    private TextView tv_result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emui);

        emuiCalendar = findViewById(R.id.emuiCalendar);

        tv_result = findViewById(R.id.tv_result);

        emuiCalendar.setCheckMode(checkModel);

        emuiCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            }

        });
        emuiCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");
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
