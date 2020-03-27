package com.necer.painter;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import org.joda.time.LocalDate;


/**
 * Created by necer on 2020/3/27.
 */
public interface CalendarBackground {

    Drawable getBackgroundDrawable(LocalDate localDate, int currentDistance, int totalDistance);

}
