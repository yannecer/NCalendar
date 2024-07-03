package com.necer.ncalendar.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.NCalendar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnCalendarScrollingListener;
import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.RecyclerViewAdapter;


import java.time.LocalDate;
import java.util.List;


/**
 * Created by necer on 2018/11/7.
 */
public class TestMiui9Activity extends BaseActivity {


    private NCalendar miui9Calendar;

    private TextView tv_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miui9);

        tv_result = findViewById(R.id.tv_result);

        miui9Calendar = findViewById(R.id.miui9Calendar);

        miui9Calendar.setCheckMode(checkModel);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);


        miui9Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            }


        });
        miui9Calendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");

            }


        });

    }

    public void jump_2018_08_11(View view) {
        miui9Calendar.jumpDate("2018-08-11");
    }

    public void jump_2019_06_20(View view) {
        miui9Calendar.jumpDate("1901-02-01");
    }

    public void jump_2020_08_11(View view) {
        miui9Calendar.jumpDate("2020-08-11");
    }

    public void lastPager(View view) {
        miui9Calendar.toLastPager();
    }

    public void nextPager(View view) {
        miui9Calendar.toNextPager();
    }

    public void today(View view) {
        miui9Calendar.toToday();
    }

    public void fold(View view) {
        if (miui9Calendar.getCalendarState() == CalendarState.WEEK) {
            miui9Calendar.toMonth();
        } else {
            miui9Calendar.toWeek();
        }
    }

}
