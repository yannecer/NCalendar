package necer.ncalendardemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.necer.ncalendar.calendar.NWeekCalendar;
import com.necer.ncalendar.listener.OnClickWeekCalendarListener;
import com.necer.ncalendar.listener.OnWeekCalendarPageChangeListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;

/**
 * Created by 闫彬彬 on 2017/8/28.
 * QQ:619008099
 */

public class TestActivity extends Activity {

   // NMonthCalendar monthCalendar;
    NWeekCalendar weekCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wm2);


       // monthCalendar = (NMonthCalendar) findViewById(R.id.monthCalendar);
        weekCalendar = (NWeekCalendar) findViewById(R.id.weekCalendar);


       /* monthCalendar.setOnClickMonthCalendarListener(new OnClickMonthCalendarListener() {
            @Override
            public void onClickMonthCalendar(DateTime dateTime) {
                MyLog.d("onClickMonthCalendar::::" + dateTime.toLocalDate());
            }
        });
        monthCalendar.setOnMonthCalendarPageChangeListener(new OnMonthCalendarPageChangeListener() {
            @Override
            public void onMonthCalendarPageSelected(DateTime dateTime) {
                MyLog.d("onMonthCalendarPageSelected::::" + dateTime.toLocalDate());
            }
        });*/

        weekCalendar.setOnWeekCalendarPageChangeListener(new OnWeekCalendarPageChangeListener() {
            @Override
            public void onWeekCalendarPageSelected(DateTime dateTime) {
                MyLog.d("onWeekCalendarPageSelected::::" + dateTime.toLocalDate());
            }
        });

        weekCalendar.setOnClickWeekCalendarListener(new OnClickWeekCalendarListener() {
            @Override
            public void onClickWeekCalendar(DateTime dateTime) {
                MyLog.d("setOnClickWeekCalendarListener::::" + dateTime.toLocalDate());
            }
        });

    }

    public void aaa(View c) {
        DateTime dateTime = new DateTime(2018, 10, 23, 0, 0, 0);
        weekCalendar.setDateTime(dateTime);
    }

}
