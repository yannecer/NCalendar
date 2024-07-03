package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import com.necer.calendar.NCalendar;
import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.RecyclerViewAdapter;


/**
 * Created by necer on 2018/11/20.
 */
public class TestWeekHoldActivity extends AppCompatActivity {


    SwipeRefreshLayout refresh_layout;
    RecyclerView recyclerView;

    NCalendar miui9Calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_hold);
        refresh_layout = findViewById(R.id.refresh_layout);
        recyclerView = findViewById(R.id.recyclerView);
        miui9Calendar = findViewById(R.id.miui9Calendar);

        miui9Calendar.setWeekHoldEnable(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh_layout.setRefreshing(false);
                    }
                }, 1000);

            }
        });
    }


    public void monthCalendar(View view) {
        miui9Calendar.toMonth();
    }
}
