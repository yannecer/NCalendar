package necer.ncalendardemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.calendar.MonthCalendar;
import com.necer.ncalendar.listener.OnMonthCalendarChangedListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;

/**
 * Created by necer on 2018/2/5.
 */

public class TestFragment2 extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_2, null);
        MonthCalendar monthCalendar = (MonthCalendar) inflate.findViewById(R.id.monthcalendar);

        monthCalendar.setOnMonthCalendarChangedListener(new OnMonthCalendarChangedListener() {
            @Override
            public void onMonthCalendarChanged(DateTime dateTime) {
                MyLog.d("TestFragment2::;"+dateTime.toString());
            }
        });



        return inflate;

    }
}
