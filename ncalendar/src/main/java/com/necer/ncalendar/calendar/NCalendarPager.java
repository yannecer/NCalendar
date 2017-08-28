package com.necer.ncalendar.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import com.necer.ncalendar.adapter.NCalendarAdapter;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NCalendarPager extends ViewPager {

    private NCalendarAdapter adapter;
    public NCalendarPager(Context context) {
        this(context, null);
    }

    public NCalendarPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new NCalendarAdapter(context);
        setAdapter(adapter);
    }


  /*  @Override
    public NCalendarAdapter getAdapter() {
        return adapter;
    }
    */


    public void change(int type) {
        adapter.notifyDataSetChanged(100, 1);

    }








}
