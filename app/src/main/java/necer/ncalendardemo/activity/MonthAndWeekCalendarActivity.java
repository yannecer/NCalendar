package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.necer.ncalendar.calendar.MWCalendar;
import com.necer.ncalendar.listener.OnCalendarChangeListener;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by necer on 2017/6/15.
 */

public class MonthAndWeekCalendarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MWCalendar mwCalendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wm);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mwCalendar = (MWCalendar) findViewById(R.id.mWCalendar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AAAdapter(this));

        mwCalendar.setOnClickCalendarListener(new OnCalendarChangeListener() {
            @Override
            public void onClickCalendar(DateTime dateTime) {

              //  MyLog.d("dateTime:onClickCalendar:" + dateTime.toLocalDate());

            }

            @Override
            public void onCalendarPageChanged(DateTime dateTime) {
               // MyLog.d("dateTime:onCalendarPageChange:" + dateTime.toLocalDate());

            }
        });

    }

    public void close(View view) {
        mwCalendar.close();
    }

    public void open(View view) {
        mwCalendar.open();
    }
}
