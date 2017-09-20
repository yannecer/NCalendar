package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.necer.ncalendar.calendar.MWCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.necer.ncalendar.utils.MyLog;

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

        mwCalendar.setOnClickCalendarListener(new OnCalendarChangedListener()  {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                MyLog.d("dateTime::" + dateTime.toLocalDate());
            }

        });
    }

    public void close(View view) {
        mwCalendar.close();
       /* List<String> pointList = new ArrayList<>();
        pointList.add("2017-05-15");
        pointList.add("2017-06-20");
        pointList.add("2017-06-07");
        pointList.add("2017-07-07");
        pointList.add("2017-09-07");
        pointList.add("2017-10-07");
        pointList.add("2017-10-07");
        mwCalendar.setPointList(pointList);*/

    }

    public void open(View view) {
        mwCalendar.open();

       // mwCalendar.setDate(2018, 1, 1);
    }
}
