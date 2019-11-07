package com.necer.ncalendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.Miui10Calendar;
import com.necer.calendar.MonthCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.painter.CalendarAdapter;

import org.joda.time.LocalDate;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestAdapterCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_adapter);


//        MonthCalendar monthCalendar = findViewById(R.id.monthCalendar);
//        monthCalendar.setCalendarAdapter(new TestAdapter());

      //  WeekCalendar weekCalendar = findViewById(R.id.weekCalendar);

     //
      //  weekCalendar.setCalendarAdapter(new TestAdapter());


        Miui10Calendar miui10Calendar = findViewById(R.id.miui10Calendar);
        miui10Calendar.setMonthStretchEnable(true);
        miui10Calendar.setCalendarAdapter(new TestAdapter());

    }


    public static class TestAdapter extends CalendarAdapter {

        @Override
        public View getCalendarItemView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_calendar, null);
            return view;
        }

        @Override
        public void onBindToadyView(View view, LocalDate localDate, List<LocalDate> selectedDateList) {
            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
            tv_item.setTextColor(selectedDateList.contains(localDate) ? Color.RED : Color.BLACK);
        }

        @Override
        public void onBindCurrentMonthOrWeekView(View view, LocalDate localDate, List<LocalDate> selectedDateList) {
            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
            tv_item.setBackgroundColor(selectedDateList.contains(localDate) ? Color.RED : Color.WHITE);
        }

        @Override
        public void onBindLastOrNextMonthView(View view, LocalDate localDate, List<LocalDate> selectedDateList) {
            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
            tv_item.setTextColor(Color.GREEN);
        }

        @Override
        public void onBindDisableDateView(View view, LocalDate localDate) {

        }

//        @Override
//        public void onBindToadyView(View view, LocalDate localDate, boolean isCheck) {
//            TextView textView = view.findViewById(R.id.tv_item);
//            textView.setText(localDate.getDayOfMonth()+"");
//            view.setBackgroundColor(isCheck ? Color.parseColor("#ff00ff") : Color.parseColor("#ffffff"));
//
//        }
//
//        @Override
//        public void onBindCurrentMonthOrWeekView(View view, LocalDate localDate, boolean isCheck) {
//            TextView textView = view.findViewById(R.id.tv_item);
//            textView.setText(localDate.getDayOfMonth()+"");
//            view.setBackgroundColor(isCheck ? Color.parseColor("#ff00ff") : Color.parseColor("#ffffff"));
//
//        }
    }
}
