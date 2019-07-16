package com.necer.ncalendar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necer.ncalendar.R;

public class ViewPagerAdapter extends PagerAdapter {


    private Context mContext;

    public ViewPagerAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.item_view_pager, null);

        TextView textView = view.findViewById(R.id.tv);
        textView.setText(view + "");
        container.addView(view);
        return view;
    }

}
