package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.MyPagerAdapter;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        ViewPager view_pager = findViewById(R.id.view_pager);


        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this);

        view_pager.setAdapter(myPagerAdapter);


    }
}
