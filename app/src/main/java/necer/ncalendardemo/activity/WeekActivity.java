package necer.ncalendardemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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

    }

    public void setDate(View view) {
        weekCalendar.setVisibility(View.VISIBLE);
        weekCalendar.post(new Runnable() {
            @Override
            public void run() {
                weekCalendar.setDate("2017-12-29");
            }
        });


    }
}
