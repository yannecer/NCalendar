package com.necer.ncalendar.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.necer.ncalendar.adapter.NAdapter;
import com.necer.ncalendar.utils.MyLog;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NCalendarViewPager extends ViewPager {

    private NAdapter adapter;
    public NCalendarViewPager(Context context) {
        this(context, null);
    }

    public NCalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new NAdapter(context);
        setAdapter(adapter);
    }




    public void change() {
        adapter.notifyDataSetChanged(30, 1);
        MyLog.d("change");
    }
}
