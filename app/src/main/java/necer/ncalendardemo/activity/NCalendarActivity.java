package necer.ncalendardemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.necer.ncalendar.calendar.NCalendar;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NCalendarActivity extends Activity{

    private NCalendar ncalendar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncalendar);

        ncalendar = (NCalendar) findViewById(R.id.ncalendar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AAAdapter(this));
    }
}
