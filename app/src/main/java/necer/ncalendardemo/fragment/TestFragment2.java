package necer.ncalendardemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.calendar.MonthCalendar;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnMonthCalendarChangedListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by necer on 2018/2/5.
 */

public class TestFragment2 extends Fragment {

  //  MonthCalendar monthCalendar;


    private NCalendar ncalendar;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_2, null);
       /* monthCalendar = (MonthCalendar) inflate.findViewById(R.id.monthcalendar);

        monthCalendar.setOnMonthCalendarChangedListener(new OnMonthCalendarChangedListener() {
            @Override
            public void onMonthCalendarChanged(LocalDate dateTime) {
                MyLog.d("TestFragment2::;"+dateTime.toString());
            }
        });*/

        ncalendar = (NCalendar) inflate.findViewById(R.id.ncalendarrrr);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AAAdapter aaAdapter = new AAAdapter(getContext());
        recyclerView.setAdapter(aaAdapter);

        return inflate;

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            ncalendar.toToday();
        }
    }
}
