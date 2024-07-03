package com.necer.ncalendar.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.necer.calendar.NCalendar;
import com.necer.enumeration.CheckModel;
import com.necer.ncalendar.R;
import com.necer.ncalendar.painter.LigaturePainter;
import com.necer.ncalendar.painter.TicketPainter;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by necer on 2024/1/4.
 */
public class CustomCalendarActivity extends AppCompatActivity {

    NCalendar miui10Calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        miui10Calendar = findViewById(R.id.miui10Calendar);
        miui10Calendar.setCheckMode(CheckModel.MULTIPLE);
        LigaturePainter painter = new LigaturePainter(this);
        miui10Calendar.setCalendarPainter(painter);

    }

    public void ligaturePainter(View view) {
        LigaturePainter painter = new LigaturePainter(this);
        miui10Calendar.setCalendarPainter(painter);
        miui10Calendar.notifyCalendar();
    }

    public void ticketPainter(View view) {
        TicketPainter ticketPainter = new TicketPainter(this, miui10Calendar);

        Map<LocalDate, String> priceMap = new HashMap<>();
        priceMap.put(LocalDate.parse("2024-06-07"), "￥350");
        priceMap.put(LocalDate.parse("2024-07-07"), "￥350");
        priceMap.put(LocalDate.parse("2024-06-30"), "￥350");
        priceMap.put(LocalDate.parse("2024-07-03"), "￥350");
        priceMap.put(LocalDate.parse("2024-07-04"), "￥350");
        priceMap.put(LocalDate.parse("2024-07-10"), "￥350");
        priceMap.put(LocalDate.parse("2024-07-15"), "￥350");
        priceMap.put(LocalDate.parse("2024-07-30"), "￥350");
        priceMap.put(LocalDate.parse("2024-08-04"), "￥350");
        priceMap.put(LocalDate.parse("2024-08-29"), "￥350");

        ticketPainter.setPriceMap(priceMap);

        miui10Calendar.setCalendarPainter(ticketPainter);
        miui10Calendar.notifyCalendar();
    }
}
