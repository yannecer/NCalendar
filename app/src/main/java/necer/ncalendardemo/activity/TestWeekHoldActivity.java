package necer.ncalendardemo.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.necer.calendar.Miui9Calendar;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by necer on 2018/11/20.
 */
public class TestWeekHoldActivity extends BaseActivity {


    SwipeRefreshLayout refresh_layout;
    RecyclerView recyclerView;

    Miui9Calendar miui9Calendar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_week_hold;
    }

    @Override
    protected void onCreatee() {

       refresh_layout = findViewById(R.id.refresh_layout);
        recyclerView = findViewById(R.id.recyclerView);
        miui9Calendar = findViewById(R.id.miui9Calendar);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AAAdapter aaAdapter = new AAAdapter(this);
        recyclerView.setAdapter(aaAdapter);

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
