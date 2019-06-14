package com.necer.ncalendar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.EmuiCalendar;
import com.necer.entity.NDate;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;
import com.necer.painter.CalendarPainter;
import com.necer.painter.InnerPainter;
import com.necer.utils.Attrs;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.List;


/**
 * Created by necer on 2018/11/12.
 */
public class TestEmuiActivity extends AppCompatActivity {

    EmuiCalendar emuiCalendar;

    TextView tv_result;

    private boolean isMultipleSelset;
    private boolean isDefaultSelect;

    public static void startActivity(Context context, boolean isDefaultSelect, boolean isMultipleSelset) {
        Intent intent = new Intent(context, TestEmuiActivity.class);
        intent.putExtra("isMultipleSelset", isMultipleSelset);
        intent.putExtra("isDefaultSelect", isDefaultSelect);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emui);

        emuiCalendar = findViewById(R.id.emuiCalendar);


        tv_result = findViewById(R.id.tv_result);

        isMultipleSelset = getIntent().getBooleanExtra("isMultipleSelset", false);
        isDefaultSelect = getIntent().getBooleanExtra("isDefaultSelect", true);

        emuiCalendar.setMultipleSelset(isMultipleSelset);
        emuiCalendar.setDefaultSelect(isDefaultSelect);




        emuiCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            }
        });
        emuiCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currectSelectList, List<LocalDate> allSelectList) {

                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currectSelectList.size() + "个  总共选中" + allSelectList.size() + "个");
                MyLog.d(year + "年" + month + "月");
                MyLog.d("当前页面选中：：" + currectSelectList);
                MyLog.d("全部选中：：" + allSelectList);
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
        int state = emuiCalendar.getCalendarState();
        if (state == Attrs.WEEK) {
            emuiCalendar.toMonth();
        } else {
            emuiCalendar.toWeek();
        }
    }
}
