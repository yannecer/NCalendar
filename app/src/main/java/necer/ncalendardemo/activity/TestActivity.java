package necer.ncalendardemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.necer.ncalendar.calendar.NMonthCalendar;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;

/**
 * Created by 闫彬彬 on 2017/8/28.
 * QQ:619008099
 */

public class TestActivity extends Activity{


    private NMonthCalendar nmcalendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        DateTime dateTime = new DateTime();

        int dayOfWeek = dateTime.getDayOfWeek();


        DateTime dateTime1 = dateTime.plusMonths(1);




        MyLog.d("dayOfWeek:::" + dayOfWeek);


    }

}
