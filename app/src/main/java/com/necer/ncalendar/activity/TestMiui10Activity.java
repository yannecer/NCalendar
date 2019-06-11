package com.necer.ncalendar.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.calendar.Miui10Calendar;
import com.necer.entity.NDate;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.ncalendar.R;
import com.necer.painter.InnerPainter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by necer on 2018/11/12.
 */
public class TestMiui10Activity extends BaseActivity {

    TextView tv_month;
    TextView tv_week;
    TextView tv_year;
    TextView tv_lunar;
    TextView tv_lunar_tg;

    Miui10Calendar miui10Calendar;


    private final String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日",};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_miui10;
    }

    @Override
    protected void onCreatee() {
        tv_month = findViewById(R.id.tv_month);
        tv_week = findViewById(R.id.tv_week);
        tv_year = findViewById(R.id.tv_year);
        tv_lunar = findViewById(R.id.tv_lunar);
        tv_lunar_tg = findViewById(R.id.tv_lunar_tg);

        List<String> pointList = Arrays.asList("2018-10-01", "2018-11-19", "2018-11-20", "2018-05-23", "2019-01-01", "2018-12-23");

        miui10Calendar = findViewById(R.id.miui10Calendar);
        miui10Calendar.setDateInterval("1901-01-01","2099-12-30");

        InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
        innerPainter.setPointList(pointList);

        Map<String, String> strMap = new HashMap<>();
        strMap.put("2019-01-25", "测试");
        strMap.put("2019-01-23", "测试1");
        strMap.put("2019-01-24", "测试2");
        innerPainter.setReplaceLunarStrMap(strMap);

        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("2019-01-25", Color.RED);

        colorMap.put("2019-01-24", Color.parseColor("#000000"));
        innerPainter.setReplaceLunarColorMap(colorMap);


        List<String> holidayList = new ArrayList<>();
        holidayList.add("2019-1-20");
        holidayList.add("2019-1-21");
        holidayList.add("2019-1-22");

        List<String> workdayList = new ArrayList<>();
        workdayList.add("2019-1-23");
        workdayList.add("2019-1-24");
        workdayList.add("2019-1-25");

       // innerPainter.setHolidayAndWorkdayList(holidayList,workdayList);


//        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
//            @Override
//            public void onCalendarDateChanged(NDate date) {
//                tv_year.setText(date.localDate.getYear() + "年");
//                tv_month.setText(date.localDate.getMonthOfYear() + "月");
//                tv_week.setText(weeks[date.localDate.getDayOfWeek() - 1]);
//                tv_lunar.setText("农历" + date.lunar.lunarYearStr + "年 ");
//                tv_lunar_tg.setText(date.lunar.lunarMonthStr + date.lunar.lunarDayStr + (TextUtils.isEmpty(date.lunarHoliday) ? "" : (" | " + date.lunarHoliday)) + (TextUtils.isEmpty(date.solarHoliday) ? "" : (" | " + date.solarHoliday)));
//
//            }
//
//            @Override
//            public void onCalendarStateChanged(boolean isMonthSate) {
//
//            }
//        });
//
//        miui10Calendar.setOnClickDisableDateListener(new OnClickDisableDateListener() {
//            @Override
//            public void onClickDisableDate(NDate nDate) {
//                MyLog.d("nDate::" + nDate.localDate);
//            }
//        });

        //miui10Calendar.setInitializeDate("2018-12-23");
    }


    public void aaa(View view) {
        miui10Calendar.jumpDate("2018-10-12");
      //  miui10Calendar.setVisibility(View.VISIBLE);
    }
}
