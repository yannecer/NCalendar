package necer.ncalendardemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.necer.ncalendar.calendar.WeekCalendar;

import necer.ncalendardemo.R;

/**
 * Created by necer on 2017/10/30.
 * QQ群:127278900
 */

public class WeekActivity extends Activity {


    private WeekCalendar weekCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        weekCalendar = (WeekCalendar) findViewById(R.id.weekCalendar);

       /* weekCalendar.post(new Runnable() {
            @Override
            public void run() {

                weekCalendar.setOnWeekCalendarChangedListener(new OnWeekCalendarChangedListener() {
                    @Override
                    public void onWeekCalendarChanged(DateTime dateTime) {
                        MyLog.d("dateTime::" + dateTime);
                    }
                });
                weekCalendar.setDate("2017-11-5");
            }
        });*/



    }
}
