package com.necer.ncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.necer.MyLog;
import com.necer.ncalendar.R;

import org.joda.time.LocalDate;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本：" + Utils.getCurrentVersion(this));


        LocalDate localDate = new LocalDate();
        LocalDate localDate1 = new LocalDate("2018-12-19");
        LocalDate localDate2 = new LocalDate("2018-12-18");
        LocalDate localDate3 = new LocalDate("2018-12-20");
        LocalDate localDate4 = new LocalDate("2018-12-10");

        MyLog.d("localDate::" + localDate.isAfter(localDate1));
        MyLog.d("localDate1::" + localDate.compareTo(localDate1));
        MyLog.d("localDate2::" + localDate.compareTo(localDate2));
        MyLog.d("localDate3::" + localDate.compareTo(localDate3));
        MyLog.d("localDate4::" + localDate.compareTo(localDate4));



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


}