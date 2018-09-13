package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.necer.calendar.MonthCalendar;

import java.util.ArrayList;
import java.util.List;

import necer.ncalendardemo.R;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_new);


        MonthCalendar monthcalendar = findViewById(R.id.monthcalendar);


        List<String> list = new ArrayList<>();
        list.add("2018-9-10");
        list.add("2018-9-12");
        list.add("2018-9-13");
        list.add("2018-10-10");
        list.add("2018-10-12");
        list.add("2018-10-16");


        monthcalendar.setPointList(list);


    }
}
