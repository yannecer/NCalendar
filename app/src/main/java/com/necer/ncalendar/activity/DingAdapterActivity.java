package com.necer.ncalendar.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.BaseCalendar;
import com.necer.calendar.ICalendar;
import com.necer.entity.CalendarDate;
import com.necer.enumeration.CalendarType;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.ncalendar.R;
import com.necer.painter.CalendarAdapter;
import com.necer.utils.CalendarUtil;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DingAdapterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adapter);


        ICalendar miui10Calendar = findViewById(R.id.miui10Calendar);
        miui10Calendar.setCalendarAdapter(new DingAdapter());
       // miui10Calendar.setSelectedMode(SelectedModel.MULTIPLE);
        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                Log.e("onCalendarChange", "onCalendarChange:::" + localDate);
            }

        });

    }



    public static class DingAdapter extends CalendarAdapter {
        @Override
        public View getCalendarItemView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.item_calendar, null);
        }


        @Override
        public void onBindToadyView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {

            View ll_content = view.findViewById(R.id.ll_content);

            TextView tv_item = view.findViewById(R.id.tv_item);

            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));

            setLunar(view, localDate, totalCheckedDateList);

            if (totalCheckedDateList.contains(localDate)) {
                tv_item.setTextColor(Color.WHITE);
                ll_content.setBackgroundResource(R.drawable.bg_checked_ding);
            } else {
                tv_item.setTextColor(Color.BLACK);
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }
        }

        @Override
        public void onBindCurrentMonthOrWeekView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {

            View ll_content = view.findViewById(R.id.ll_content);

            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setTextColor(Color.BLACK);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));

            setLunar(view, localDate, totalCheckedDateList);

            if (totalCheckedDateList.contains(localDate)) {
                tv_item.setTextColor(Color.WHITE);
                ll_content.setBackgroundResource(R.drawable.bg_checked_ding);
            } else {
                tv_item.setTextColor(Color.BLACK);
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }

        }

        @Override
        public void onBindLastOrNextMonthView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
            View ll_content = view.findViewById(R.id.ll_content);
            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
            setLunar(view, localDate, totalCheckedDateList);
            if (totalCheckedDateList.contains(localDate)) {
                tv_item.setTextColor(Color.WHITE);
                ll_content.setBackgroundResource(R.drawable.bg_checked_ding_last_next);
            } else {
                tv_item.setTextColor(Color.GRAY);
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }
        }


        private void setLunar(View view, LocalDate localDate, List<LocalDate> selectedDateList) {

            TextView tv_lunar = view.findViewById(R.id.tv_lunar);
            CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
            tv_lunar.setText(calendarDate.lunar.lunarOnDrawStr);
            if (selectedDateList.contains(localDate)) {
                tv_lunar.setTextColor(Color.WHITE);
            } else {
                tv_lunar.setTextColor(Color.GRAY);
            }
        }
    }

}
