package com.necer.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.necer.utils.Attrs;

/**
 * Created by necer on 2018/11/7.
 */
public class Miui9Calendar extends FrameLayout {


    protected WeekCalendar weekCalendar;
    protected MonthCalendar monthCalendar;

    protected int duration;//动画时间
    protected int weekHeigh;//周日历的高度
    protected int monthHeigh;//月日历的高度,是日历整个的高

    protected int STATE;//默认月


    public Miui9Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        monthCalendar = new MonthCalendar(context, attrs);
        weekCalendar = new WeekCalendar(context, attrs);

        Attrs attrss = monthCalendar.getAttrs();

        duration = attrss.duration;
        monthHeigh = attrss.monthCalendarHeight;
        STATE = attrss.defaultCalendar;

        weekHeigh = monthHeigh / 5;

        addView(monthCalendar);
        addView(weekCalendar);

        ViewGroup.LayoutParams monthLayoutParams = monthCalendar.getLayoutParams();
        ViewGroup.LayoutParams weekLayoutParams = weekCalendar.getLayoutParams();


        monthLayoutParams.height = monthHeigh;
        weekLayoutParams.height = weekHeigh;
        monthCalendar.setLayoutParams(monthLayoutParams);
        weekCalendar.setLayoutParams(weekLayoutParams);

        weekCalendar.setVisibility(STATE == Attrs.MONTH ? INVISIBLE : VISIBLE);



    }
}
