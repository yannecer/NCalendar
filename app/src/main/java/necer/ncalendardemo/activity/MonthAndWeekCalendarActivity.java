package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.necer.ncalendar.calendar.WeekCalendar;
import necer.ncalendardemo.view.MWCalendar;
import necer.ncalendardemo.R;

/**
 * Created by necer on 2017/6/15.
 */

public class MonthAndWeekCalendarActivity extends AppCompatActivity {


    private MWCalendar mwCalendar;
    private WeekCalendar weekCalendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wm);

        mwCalendar = (MWCalendar) findViewById(R.id.mwCalendar);
        weekCalendar = (WeekCalendar) findViewById(R.id.weekCalendar);

        mwCalendar.setWeekCalendar(weekCalendar);


    }
}
