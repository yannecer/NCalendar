package com.necer.ncalendar;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by necer on 2020/7/2.
 */
public class DensityUtil {

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}
