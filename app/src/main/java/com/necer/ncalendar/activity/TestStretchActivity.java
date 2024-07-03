package com.necer.ncalendar.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.necer.calendar.NCalendar;
import com.necer.ncalendar.R;
import com.necer.painter.InnerPainter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStretchActivity extends AppCompatActivity {


    private NCalendar miui10Calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretch);

        miui10Calendar = findViewById(R.id.miui10Calendar);

        List<String> pointList = Arrays.asList("2019-07-01", "2019-07-19", "2019-07-25", "2019-05-23", "2019-01-01", "2018-12-23");

        InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
        innerPainter.setPointList(pointList);


        Map<String, String> strMap = new HashMap<>();
        strMap.put("2024-07-01", "测试");
        strMap.put("2024-07-19", "测试1");
        strMap.put("2024-07-25", "测试2");
        strMap.put("2024-08-25", "测试3");
        strMap.put("2024-08-28", "测试4");
        strMap.put("2024-11-26", "测试5");
        innerPainter.setStretchStrMap(strMap);

    }

}
