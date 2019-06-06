package com.necer.ncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.ncalendar.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本：" + Utils.getCurrentVersion(this));


        List<String> list = new ArrayList<>();

        list.add("aaa");
        list.add("aaa");
        list.add("aaa");
        list.add("aaa");

        MyLog.d("list:::"+list.size());


    }


    public void miui10(View v) {
        startActivity(new Intent(this, TestMiui10Activity.class));
    }

    public void miui9(View v) {
        startActivity(new Intent(this, TestMiui9Activity.class));
    }

    public void emui(View v) {
        startActivity(new Intent(this, TestEmuiActivity.class));
    }

    public void toHoldWeek(View view) {
        startActivity(new Intent(this, TestWeekHoldActivity.class));
    }

    public void toMonthWeek(View view) {
        startActivity(new Intent(this, TestMonthWeekActivity.class));
    }

    public void addView(View view) {
        startActivity(new Intent(this, TestAddViewActivity.class));
    }
    public void customCalendar(View view) {
        startActivity(new Intent(this, CustomCalendarActivity.class));
    }


    public void test(View view) {
        startActivity(new Intent(this,TestCalendarActivity.class));
    }

}