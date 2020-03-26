package com.necer.ncalendar.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.entity.CalendarDate;
import com.necer.entity.Lunar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.ncalendar.R;
import com.necer.painter.InnerPainter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by necer on 2018/11/12.
 */
public class TestMiui10Activity extends BaseActivity {

    private Miui10Calendar miui10Calendar;

    private TextView tv_result;
    private TextView tv_data;
    private TextView tv_desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miui10);
        tv_result = findViewById(R.id.tv_result);
        tv_data = findViewById(R.id.tv_data);
        tv_desc = findViewById(R.id.tv_desc);
        List<String> pointList = Arrays.asList("2018-10-01", "2018-11-19", "2018-11-20", "2018-05-23", "2019-01-01", "2018-12-23");

        miui10Calendar = findViewById(R.id.miui10Calendar);
        miui10Calendar.setCheckMode(checkModel);
        InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
        innerPainter.setPointList(pointList);

        Map<String, String> strMap = new HashMap<>();
        strMap.put("2019-01-25", "测试");
        strMap.put("2019-01-23", "测试1");
        strMap.put("2019-01-24", "测试2");
        innerPainter.setReplaceLunarStrMap(strMap);

        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("2019-08-25", Color.RED);

        colorMap.put("2019-08-5", Color.parseColor("#000000"));
        innerPainter.setReplaceLunarColorMap(colorMap);


        List<String> holidayList = new ArrayList<>();
        holidayList.add("2019-7-20");
        holidayList.add("2019-7-21");
        holidayList.add("2019-7-22");

        List<String> workdayList = new ArrayList<>();
        workdayList.add("2019-7-23");
        workdayList.add("2019-7-24");
        workdayList.add("2019-7-25");

        innerPainter.setLegalHolidayList(holidayList, workdayList);

        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
                Log.d(TAG, "   当前页面选中 " + localDate);

                if (localDate != null) {
                    CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
                    Lunar lunar = calendarDate.lunar;
                    tv_data.setText(localDate.toString("yyyy年MM月dd日"));
                    tv_desc.setText(lunar.chineseEra + lunar.animals + "年" + lunar.lunarMonthStr + lunar.lunarDayStr);
                } else {
                    tv_data.setText("");
                    tv_desc.setText("");
                }
            }

        });
        miui10Calendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");
                Log.d(TAG, year + "年" + month + "月");
                Log.d(TAG, "当前页面选中：：" + currPagerCheckedList);
                Log.d(TAG, "全部选中：：" + totalCheckedList);
            }

        });


    }


}
