package com.necer.ncalendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.view.NMonthView;
import com.necer.ncalendar.view.NWeekView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NCalendarAdapter extends PagerAdapter {


    private Context mContext;
    private DateTime dateTime;
    private List<String> list;

    public NCalendarAdapter(Context mContext) {
        this.mContext = mContext;
        dateTime = new DateTime();
        list = new ArrayList<>();
    }

    private int mCount = 20;
    private int type;//0,,1


    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // MonthView monthView = new MonthView(mContext, dateTime, null, list);

        View view;
        if (type == 0) {
            view = new NMonthView(mContext);
        } else {
            view = new NWeekView(mContext);
        }
        MyLog.d("type:::" + type);
        container.addView(view);


        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public void notifyDataSetChanged(int mCount, int type) {

        this.mCount = mCount;
        this.type = type;
        notifyDataSetChanged();

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
