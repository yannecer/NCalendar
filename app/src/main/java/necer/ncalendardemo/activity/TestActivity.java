package necer.ncalendardemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.necer.ncalendar.calendar.NMonthCalendar;
import com.necer.ncalendar.calendar.NWeekCalendar;
import com.necer.ncalendar.listener.OnWeekCalendarChangedListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;

/**
 * Created by 闫彬彬 on 2017/9/22.
 * QQ:619008099
 */

public class TestActivity extends Activity {


    private NMonthCalendar nmonthcalendar;

    private NWeekCalendar nweekcalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        nweekcalendar = (NWeekCalendar) findViewById(R.id.nweekcalendar);

        nweekcalendar.setOnWeekCalendarChangedListener(new OnWeekCalendarChangedListener() {
            @Override
            public void onWeekCalendarChanged(DateTime dateTime) {
                MyLog.d("onWeekCalendarChanged::" + dateTime);
            }
        });



       /* nmonthcalendar = (NMonthCalendar) findViewById(R.id.nmonthcalendar);
        nmonthcalendar.post(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                list.add("2017-9-21");
                list.add("2017-9-21");
                list.add("2017-9-23");
                list.add("2017-9-26");
                nmonthcalendar.setPointList(list);
            }
        });
        nmonthcalendar.setOnMonthCalendarChangedListener(new OnMonthCalendarChangedListener() {
            @Override
            public void onMonthCalendarChanged(DateTime dateTime) {
                MyLog.d("onMonthCalendarChanged::" + dateTime.toLocalDate());
            }
        });
*/
    }

    public void aaa(View view) {
       // nmonthcalendar.setDate("2018-09-01");
        nweekcalendar.setDate("2018-09-01");

    }
}
