package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.necer.calendar.EmuiCalendar;
import com.necer.entity.NDate;
import com.necer.listener.OnCalendarChangedListener;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emui);

        emuiCalendar = findViewById(R.id.emuiCalendar);
    }


    public void lastPager(View view) {

    }

    public void nextPager(View view) {

    }

    public void today(View view) {

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
