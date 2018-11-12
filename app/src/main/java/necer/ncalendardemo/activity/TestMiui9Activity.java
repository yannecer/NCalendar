package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.necer.MyLog;
import com.necer.calendar.Miui9Calendar;
import com.necer.listener.OnCalendarChangedListener;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by necer on 2018/11/7.
 */
public class TestMiui9Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_miui9);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AAAdapter aaAdapter = new AAAdapter(this);
        recyclerView.setAdapter(aaAdapter);


        Miui9Calendar miui9Calendar = findViewById(R.id.miui9Calendar);

        miui9Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarDateChanged(LocalDate date) {
                MyLog.d("OnCalendarChangedListener:::" + date);
            }

            @Override
            public void onCalendarStateChanged(boolean isMonthSate) {
                MyLog.d("OnCalendarChangedListener:::" + isMonthSate);
            }
        });






        findViewById(R.id.bt)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

    }
}
