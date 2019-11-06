package com.necer.ncalendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.MonthCalendar;
import com.necer.painter.CalendarAdapter;

import org.joda.time.LocalDate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestAdapterCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_adapter);


        MonthCalendar monthCalendar = findViewById(R.id.monthCalendar);

        monthCalendar.setCalendarAdapter(new TestAdapter());
    }


    public static class TestAdapter extends CalendarAdapter {

        @Override
        public View getCalendarView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_calendar, null);
            return view;
        }

        @Override
        public void onBindToadyView(View view, LocalDate localDate, boolean isCheck) {
            TextView textView = view.findViewById(R.id.tv_item);
            textView.setText(localDate.getDayOfMonth()+"");
            view.setBackgroundColor(isCheck ? Color.parseColor("#ff00ff") : Color.parseColor("#ffffff"));

        }

        @Override
        public void onBindCurrentMonthOrWeekView(View view, LocalDate localDate, boolean isCheck) {
            TextView textView = view.findViewById(R.id.tv_item);
            textView.setText(localDate.getDayOfMonth()+"");
            view.setBackgroundColor(isCheck ? Color.parseColor("#ff00ff") : Color.parseColor("#ffffff"));

        }
    }
}
