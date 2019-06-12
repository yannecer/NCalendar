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

        // TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        // tvVersion.setText("版本：" + Utils.getCurrentVersion(this));


    }

    public void testMonth(View view) {
        startActivity(new Intent(this, TestMonthActivity.class));
    }

    public void testWeek(View view) {
        startActivity(new Intent(this, TestWeekActivity.class));
    }


}