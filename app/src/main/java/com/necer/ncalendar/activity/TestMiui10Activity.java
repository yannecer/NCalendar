package com.necer.ncalendar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;
import com.necer.painter.InnerPainter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by necer on 2018/11/12.
 */
public class TestMiui10Activity extends AppCompatActivity {

    Miui10Calendar miui10Calendar;

    TextView tv_result;

    private boolean isMultipleSelset;
    private boolean isDefaultSelect;

    public static void startActivity(Context context, boolean isDefaultSelect, boolean isMultipleSelset) {
        Intent intent = new Intent(context, TestMiui10Activity.class);
        intent.putExtra("isMultipleSelset", isMultipleSelset);
        intent.putExtra("isDefaultSelect", isDefaultSelect);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miui10);
        tv_result = findViewById(R.id.tv_result);
        List<String> pointList = Arrays.asList("2018-10-01", "2018-11-19", "2018-11-20", "2018-05-23", "2019-01-01", "2018-12-23");


        isMultipleSelset = getIntent().getBooleanExtra("isMultipleSelset", false);
        isDefaultSelect = getIntent().getBooleanExtra("isDefaultSelect", true);

        miui10Calendar = findViewById(R.id.miui10Calendar);
        miui10Calendar.setMultipleSelset(isMultipleSelset);
        miui10Calendar.setDefaultSelect(isDefaultSelect);


        //  miui10Calendar.setDateInterval("1901-01-01","2099-12-30");

        InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
        innerPainter.setPointList(pointList);

        Map<String, String> strMap = new HashMap<>();
        strMap.put("2019-01-25", "测试");
        strMap.put("2019-01-23", "测试1");
        strMap.put("2019-01-24", "测试2");
        innerPainter.setReplaceLunarStrMap(strMap);

        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("2019-07-25", Color.RED);

        colorMap.put("2019-07-5", Color.parseColor("#000000"));
        innerPainter.setReplaceLunarColorMap(colorMap);


        List<String> holidayList = new ArrayList<>();
        holidayList.add("2019-1-20");
        holidayList.add("2019-1-21");
        holidayList.add("2019-1-22");

        List<String> workdayList = new ArrayList<>();
        workdayList.add("2019-1-23");
        workdayList.add("2019-1-24");
        workdayList.add("2019-1-25");


        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            }
        });
        miui10Calendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currectSelectList, List<LocalDate> allSelectList) {

                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currectSelectList.size() + "个  总共选中" + allSelectList.size() + "个");
                MyLog.d(year + "年" + month + "月");
                MyLog.d("当前页面选中：：" + currectSelectList);
                MyLog.d("全部选中：：" + allSelectList);
            }
        });


    }



}
