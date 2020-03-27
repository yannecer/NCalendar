package com.necer.utils;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by necer on 2020/3/27.
 */
public class DrawableUtil {


    /**
     * 获取绘制Drawable的矩形
     * @param centerX
     * @param centerY
     * @param drawable
     * @return
     */
    public static Rect getDrawableBounds(int centerX, int centerY, Drawable drawable) {
        return new Rect(centerX - drawable.getIntrinsicWidth() / 2,
                centerY - drawable.getIntrinsicHeight() / 2,
                centerX + drawable.getIntrinsicWidth() / 2,
                centerY + drawable.getIntrinsicHeight() / 2);
    }
}
