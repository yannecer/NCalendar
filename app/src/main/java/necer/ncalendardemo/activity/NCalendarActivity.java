package necer.ncalendardemo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by necer on 2017/9/27.
 * QQ群:127278900
 */

public class NCalendarActivity extends AppCompatActivity implements OnCalendarChangedListener {


    private NCalendar ncalendar;
    private RecyclerView recyclerView;
    private TextView tv_month;
    private TextView tv_date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ncalendar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        ncalendar = (NCalendar) findViewById(R.id.ncalendarrrr);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_date = (TextView) findViewById(R.id.tv_date);


     //   ncalendar.setDateInterval("2017-04-02","2018-01-01");



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AAAdapter aaAdapter = new AAAdapter(this);
        recyclerView.setAdapter(aaAdapter);
        ncalendar.setOnCalendarChangedListener(this);

        ncalendar.post(new Runnable() {
            @Override
            public void run() {

                List<String> list = new ArrayList<>();
                list.add("2017-09-21");
                list.add("2017-10-21");
                list.add("2017-10-1");
                list.add("2017-10-15");
                list.add("2017-10-18");
                list.add("2017-10-26");
                list.add("2017-11-21");

                ncalendar.setPoint(list);
            }
        });
    }


    @Override
    public void onCalendarChanged(DateTime dateTime) {
        tv_month.setText(dateTime.getMonthOfYear() + "月");
        tv_date.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月" + dateTime.getDayOfMonth() + "日");

        MyLog.d("dateTime::" + dateTime);
    }


    public void setDate(View view) {
        ncalendar.setDate("2017-12-31");
    }

    public void toMonth(View view) {
        ncalendar.toMonth();
    }

    public void toWeek(View view) {
        ncalendar.toWeek();
    }

    public void toToday(View view) {
        ncalendar.toToday();
    }

    public void toNextPager(View view) {
        ncalendar.toNextPager();
    }

    public void toLastPager(View view) {
        ncalendar.toLastPager();
    }


    public void setPoint(View view) {
        List<String> list = new ArrayList<>();
        list.add("2017-09-21");
        list.add("2017-10-21");
        list.add("2017-10-1");
        list.add("2017-10-15");
        list.add("2017-10-18");
        list.add("2017-10-26");
        list.add("2017-11-21");

        ncalendar.setPoint(list);
    }
}
