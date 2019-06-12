package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.calendar.Miui9Calendar;
import com.necer.entity.NDate;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.AAAdapter;
import com.necer.utils.Attrs;


/**
 * Created by necer on 2018/11/7.
 */
public class TestMiui9Activity extends AppCompatActivity {


    private Miui9Calendar miui9Calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miui9);


        miui9Calendar = findViewById(R.id.miui9Calendar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AAAdapter aaAdapter = new AAAdapter(this);
        recyclerView.setAdapter(aaAdapter);

    }

    public void lastPager(View view) {

    }

    public void nextPager(View view) {

    }

    public void today(View view) {

    }

    public void fold(View view) {


        int state = miui9Calendar.getState();
        if (state == Attrs.WEEK) {
            miui9Calendar.toMonth();
        } else {
            miui9Calendar.toWeek();
        }

    }


}
