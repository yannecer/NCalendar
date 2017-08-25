package com.necer.ncalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.view.TextVvIEW;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 闫彬彬 on 2017/8/25.
 * QQ:619008099
 */

public class NAdapter extends PagerAdapter {


    private Context mContext;
    private DateTime dateTime;
    private List<String> list;

    public NAdapter(Context mContext) {
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

        TextVvIEW textVvIEW = null;
        if (type == 0) {
            textVvIEW = new TextVvIEW(mContext, Color.BLACK);
        } else {
            textVvIEW = new TextVvIEW(mContext, Color.BLUE);

        }
        MyLog.d("type:::" + type);
        container.addView(textVvIEW);


        return textVvIEW;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public void notifyDataSetChanged(int mCount, int type) {

        this.mCount = mCount;
        this.type = type;
        notifyDataSetChanged();

        MyLog.d("notifyDataSetChanged");
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
