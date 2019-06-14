package com.necer.ncalendar.activity;

import android.content.Context;
import android.content.Intent;
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

    private boolean isMultipleSelset;
    private boolean isDefaultSelect;

    public static void startActivity(Context context, boolean isDefaultSelect, boolean isMultipleSelset) {
        Intent intent = new Intent(context, TestMiui9Activity.class);
        intent.putExtra("isMultipleSelset", isMultipleSelset);
        intent.putExtra("isDefaultSelect", isDefaultSelect);
        context.startActivity(intent);

    }


    private Miui9Calendar miui9Calendar;

    private TextView tv_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miui9);

        tv_result = findViewById(R.id.tv_result);

        isMultipleSelset = getIntent().getBooleanExtra("isMultipleSelset", false);
        isDefaultSelect = getIntent().getBooleanExtra("isDefaultSelect", true);

        miui9Calendar = findViewById(R.id.miui9Calendar);
        miui9Calendar.setMultipleSelset(isMultipleSelset);
        miui9Calendar.setDefaultSelect(isDefaultSelect);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AAAdapter aaAdapter = new AAAdapter(this);
        recyclerView.setAdapter(aaAdapter);


        miui9Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            }
        });
        miui9Calendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currectSelectList, List<LocalDate> allSelectList) {

                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currectSelectList.size() + "个  总共选中" + allSelectList.size() + "个");
                MyLog.d(year + "年" + month + "月");
                MyLog.d("当前页面选中：：" + currectSelectList);
                MyLog.d("全部选中：：" + allSelectList);
            }
        });

    }

    public void jump_2018_08_11(View view) {
        miui9Calendar.jumpDate("2018-08-11");
    }

    public void jump_2019_06_20(View view) {
        miui9Calendar.jumpDate("2019-06-20");
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
        int state = miui9Calendar.getCalendarState();
        if (state == Attrs.WEEK) {
            miui9Calendar.toMonth();
        } else {
            miui9Calendar.toWeek();
        }
    }

}
