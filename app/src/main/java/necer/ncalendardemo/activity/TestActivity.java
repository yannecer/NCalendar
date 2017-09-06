package necer.ncalendardemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.necer.ncalendar.calendar.MWCalendar2;
import com.necer.ncalendar.listener.OnCalendarChangeListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by 闫彬彬 on 2017/8/28.
 * QQ:619008099
 */

public class TestActivity extends Activity{


    private RecyclerView recyclerView;
    private MWCalendar2 mwCalendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wm2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mwCalendar = (MWCalendar2) findViewById(R.id.mWCalendar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AAAdapter(this));

        mwCalendar.setOnClickCalendarListener(new OnCalendarChangeListener() {
            @Override
            public void onClickCalendar(DateTime dateTime) {
                MyLog.d("dateTime::" + dateTime.toLocalDate());
            }

            @Override
            public void onCalendarPageChanged(DateTime dateTime) {

            }
        });
    }

}
