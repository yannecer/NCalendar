package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui9Calendar;
import com.necer.entity.NDate;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.AAAdapter;
import com.necer.utils.Attrs;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * Created by necer on 2018/11/7.
 */
public class TestMiui9Activity extends AppCompatActivity {


    private Miui9Calendar miui9Calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miui9);


        miui9Calendar = findViewById(R.id.miui9Calendar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AAAdapter aaAdapter = new AAAdapter(this);
        recyclerView.setAdapter(aaAdapter);




        miui9Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {

                MyLog.d("setOnCalendarChangedListener:::" + year + "年" + month + "月" + "   当前页面选中 " + localDate);

            }
        });
        miui9Calendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currectSelectList, List<LocalDate> allSelectList) {
                MyLog.d(year + "年" + month + "月");
                MyLog.d("当前页面选中：：" + currectSelectList);
                MyLog.d("全部选中：：" + allSelectList);            }
        });


    }

    public void lastPager(View view) {

    }

    public void nextPager(View view) {

    }

    public void today(View view) {

    }

    public void fold(View view) {


        int state = miui9Calendar.getCalendarState();
        if (state == Attrs.WEEK) {
            miui9Calendar.toMonth();
        } else {
            miui9Calendar.toWeek();
        }

    }


}
