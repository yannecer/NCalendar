package com.necer.ncalendar;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.necer.ncalendar.adapter.ViewPagerAdapter;
import com.necer.ncalendar.fragment.Fragment1;
import com.necer.ncalendar.fragment.Fragment2;
import com.necer.ncalendar.fragment.Fragment3;
import com.necer.ncalendar.fragment.Fragment4;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tl_tabs);




        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());
        titles.add("测试1");
        titles.add("测试2");
        titles.add("测试3");
        titles.add("日历");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
