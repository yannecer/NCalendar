package com.necer.ncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.ViewPagerAdapter;

public class TestViewPagerActivity extends AppCompatActivity {


    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setPageMargin(100);
        viewPager.setAdapter(new ViewPagerAdapter(this));

    }
}
