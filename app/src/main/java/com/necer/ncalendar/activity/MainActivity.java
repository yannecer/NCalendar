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


    }

    public void testMonth(View view) {
        startActivity(new Intent(this, TestMonthActivity.class));
    }

    public void testWeek(View view) {
        startActivity(new Intent(this, TestWeekActivity.class));
    }


    public void miui9_0(View view) {
        TestMiui9Activity.startActivity(this, true, false);
    }

    public void miui9_1(View view) {
        TestMiui9Activity.startActivity(this, false, false);
    }

    public void miui9_2(View view) {
        TestMiui9Activity.startActivity(this, false, true);
    }

    public void miui10_0(View view) {
        TestMiui10Activity.startActivity(this, true, false);
    }

    public void miui10_1(View view) {
        TestMiui10Activity.startActivity(this, false, false);
    }

    public void miui10_2(View view) {
        TestMiui10Activity.startActivity(this, false, true);
    }


    public void emiui_0(View view) {
        TestEmuiActivity.startActivity(this, true, false);
    }

    public void emiui_1(View view) {
        TestEmuiActivity.startActivity(this, false, false);
    }

    public void emiui_2(View view) {
        TestEmuiActivity.startActivity(this, false, true);
    }


    public void toHoldWeek(View view) {

        startActivity(new Intent(this, TestWeekHoldActivity.class));
    }

    public void addView(View view) {

        startActivity(new Intent(this, TestAddViewActivity.class));
    }

    public void customCalendar(View view) {

        startActivity(new Intent(this, CustomCalendarActivity.class));
    }

}