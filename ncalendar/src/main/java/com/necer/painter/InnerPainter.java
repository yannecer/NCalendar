package com.necer.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.necer.R;
import com.necer.calendar.ICalendar;
import com.necer.entity.CalendarDate;
import com.necer.utils.Attrs;
import com.necer.utils.CalendarUtil;
import com.necer.utils.DrawableUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author necer
 * @date 2019/1/3
 */
public class InnerPainter implements CalendarPainter {

    private Attrs mAttrs;
    private Paint mTextPaint;

    private int noAlphaColor = 255;

    private List<LocalDate> mHolidayList;
    private List<LocalDate> mWorkdayList;

    private List<LocalDate> mPointList;
    private Map<LocalDate, String> mReplaceLunarStrMap;
    private Map<LocalDate, Integer> mReplaceLunarColorMap;
    private Map<LocalDate, String> mStretchStrMap;

    private ICalendar mCalendar;

    private Drawable mDefaultCheckedBackground;
    private Drawable mTodayCheckedBackground;

    private Drawable mDefaultCheckedPoint;
    private Drawable mDefaultUnCheckedPoint;
    private Drawable mTodayCheckedPoint;
    private Drawable mTodayUnCheckedPoint;


    private Context mContext;


    public InnerPainter(Context context, ICalendar calendar) {
        this.mAttrs = calendar.getAttrs();
        this.mContext = context;
        this.mCalendar = calendar;

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mPointList = new ArrayList<>();
        mHolidayList = new ArrayList<>();
        mWorkdayList = new ArrayList<>();
        mReplaceLunarStrMap = new HashMap<>();
        mReplaceLunarColorMap = new HashMap<>();
        mStretchStrMap = new HashMap<>();

        mDefaultCheckedBackground = ContextCompat.getDrawable(context, mAttrs.defaultCheckedBackground);
        mTodayCheckedBackground = ContextCompat.getDrawable(context, mAttrs.todayCheckedBackground);

        mDefaultCheckedPoint = ContextCompat.getDrawable(context, mAttrs.defaultCheckedPoint);
        mDefaultUnCheckedPoint = ContextCompat.getDrawable(context, mAttrs.defaultUnCheckedPoint);
        mTodayCheckedPoint = ContextCompat.getDrawable(context, mAttrs.todayCheckedPoint);
        mTodayUnCheckedPoint = ContextCompat.getDrawable(context, mAttrs.todayUnCheckedPoint);

        List<String> holidayList = CalendarUtil.getHolidayList();
        for (int i = 0; i < holidayList.size(); i++) {
            mHolidayList.add(new LocalDate(holidayList.get(i)));
        }
        List<String> workdayList = CalendarUtil.getWorkdayList();
        for (int i = 0; i < workdayList.size(); i++) {
            mWorkdayList.add(new LocalDate(workdayList.get(i)));
        }
    }


    @Override
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mTodayCheckedBackground, rectF, noAlphaColor);
            drawSolar(canvas, rectF, localDate, mAttrs.todayCheckedSolarTextColor, noAlphaColor);
            drawLunar(canvas, rectF, localDate, mAttrs.todayCheckedLunarTextColor, noAlphaColor);
            drawPoint(canvas, rectF, localDate, mTodayCheckedPoint, noAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.todayCheckedHoliday, mAttrs.todayCheckedWorkday, mAttrs.todayCheckedHolidayTextColor, mAttrs.todayCheckedWorkdayTextColor, noAlphaColor);
        } else {
            drawSolar(canvas, rectF, localDate, mAttrs.todayUnCheckedSolarTextColor, noAlphaColor);
            drawLunar(canvas, rectF, localDate, mAttrs.todayUnCheckedLunarTextColor, noAlphaColor);
            drawPoint(canvas, rectF, localDate, mTodayUnCheckedPoint, noAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.todayUnCheckedHoliday, mAttrs.todayUnCheckedWorkday, mAttrs.todayUnCheckedHolidayTextColor, mAttrs.todayUnCheckedWorkdayTextColor, noAlphaColor);
        }
        drawStretchText(canvas, rectF, noAlphaColor, localDate);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mDefaultCheckedBackground, rectF, noAlphaColor);
            drawSolar(canvas, rectF, localDate, mAttrs.defaultCheckedSolarTextColor, noAlphaColor);
            drawLunar(canvas, rectF, localDate, mAttrs.defaultCheckedLunarTextColor, noAlphaColor);
            drawPoint(canvas, rectF, localDate, mDefaultCheckedPoint, noAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultCheckedHoliday, mAttrs.defaultCheckedWorkday, mAttrs.defaultCheckedHolidayTextColor, mAttrs.defaultCheckedWorkdayTextColor, noAlphaColor);

        } else {
            drawSolar(canvas, rectF, localDate, mAttrs.defaultUnCheckedSolarTextColor, noAlphaColor);
            drawLunar(canvas, rectF, localDate, mAttrs.defaultUnCheckedLunarTextColor, noAlphaColor);
            drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, noAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultUnCheckedHoliday, mAttrs.defaultUnCheckedWorkday, mAttrs.defaultUnCheckedHolidayTextColor, mAttrs.defaultUnCheckedWorkdayTextColor, noAlphaColor);
        }
        drawStretchText(canvas, rectF, noAlphaColor, localDate);

    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mDefaultCheckedBackground, rectF, mAttrs.lastNextMothAlphaColor);
            drawSolar(canvas, rectF, localDate, mAttrs.defaultCheckedSolarTextColor, mAttrs.lastNextMothAlphaColor);
            drawLunar(canvas, rectF, localDate, mAttrs.defaultCheckedLunarTextColor, mAttrs.lastNextMothAlphaColor);
            drawPoint(canvas, rectF, localDate, mDefaultCheckedPoint, mAttrs.lastNextMothAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultCheckedHoliday, mAttrs.defaultCheckedWorkday, mAttrs.defaultCheckedHolidayTextColor, mAttrs.defaultCheckedWorkdayTextColor, mAttrs.lastNextMothAlphaColor);
        } else {
            drawSolar(canvas, rectF, localDate, mAttrs.defaultUnCheckedSolarTextColor, mAttrs.lastNextMothAlphaColor);
            drawLunar(canvas, rectF, localDate, mAttrs.defaultUnCheckedLunarTextColor, mAttrs.lastNextMothAlphaColor);
            drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, mAttrs.lastNextMothAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultUnCheckedHoliday, mAttrs.defaultUnCheckedWorkday, mAttrs.defaultUnCheckedHolidayTextColor, mAttrs.defaultUnCheckedWorkdayTextColor, mAttrs.lastNextMothAlphaColor);
        }
        drawStretchText(canvas, rectF, mAttrs.lastNextMothAlphaColor, localDate);
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate) {
        drawSolar(canvas, rectF, localDate, mAttrs.defaultUnCheckedSolarTextColor, mAttrs.disabledAlphaColor);
        drawLunar(canvas, rectF, localDate, mAttrs.defaultUnCheckedLunarTextColor, mAttrs.disabledAlphaColor);
        drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, mAttrs.disabledAlphaColor);
        drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultUnCheckedHoliday, mAttrs.defaultUnCheckedWorkday, mAttrs.defaultUnCheckedHolidayTextColor, mAttrs.defaultUnCheckedWorkdayTextColor, mAttrs.disabledAlphaColor);
        drawStretchText(canvas, rectF, mAttrs.disabledAlphaColor, localDate);
    }

    //选中背景
    private void drawCheckedBackground(Canvas canvas, Drawable drawable, RectF rectF, int alphaColor) {
        Rect drawableBounds = DrawableUtil.getDrawableBounds((int) rectF.centerX(), (int) rectF.centerY(), drawable);
        drawable.setBounds(drawableBounds);
        drawable.setAlpha(alphaColor);
        drawable.draw(canvas);
    }


    //绘制公历
    private void drawSolar(Canvas canvas, RectF rectF, LocalDate date, int color, int alphaColor) {
        mTextPaint.setColor(color);
        mTextPaint.setAlpha(alphaColor);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        mTextPaint.setFakeBoldText(mAttrs.solarTextBold);
        canvas.drawText(date.getDayOfMonth() + "", rectF.centerX(), mAttrs.showLunar ? rectF.centerY() : getTextBaseLineY(rectF.centerY()), mTextPaint);
    }

    //绘制农历
    private void drawLunar(Canvas canvas, RectF rectF, LocalDate localDate, int color, int alphaColor) {
        if (mAttrs.showLunar) {
            CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
            //农历部分文字展示优先顺序 替换的文字、农历节日、节气、公历节日、正常农历日期
            String lunarString;
            String replaceString = mReplaceLunarStrMap.get(calendarDate.localDate);
            if (!TextUtils.isEmpty(replaceString)) {
                lunarString = replaceString;
            } else if (!TextUtils.isEmpty(calendarDate.lunarHoliday)) {
                lunarString = calendarDate.lunarHoliday;
            } else if (!TextUtils.isEmpty(calendarDate.solarTerm)) {
                lunarString = calendarDate.solarTerm;
            } else if (!TextUtils.isEmpty(calendarDate.solarHoliday)) {
                lunarString = calendarDate.solarHoliday;
            } else {
                lunarString = calendarDate.lunar.lunarOnDrawStr;
            }

            Integer replaceColor = mReplaceLunarColorMap.get(calendarDate.localDate);
            mTextPaint.setColor(replaceColor == null ? color : replaceColor);
            mTextPaint.setTextSize(mAttrs.lunarTextSize);
            mTextPaint.setAlpha(alphaColor);
            mTextPaint.setFakeBoldText(mAttrs.lunarTextBold);
            canvas.drawText(lunarString, rectF.centerX(), rectF.centerY() + mAttrs.lunarDistance, mTextPaint);
        }
    }


    //绘制标记
    private void drawPoint(Canvas canvas, RectF rectF, LocalDate date, Drawable drawable, int alphaColor) {
        if (mPointList.contains(date)) {
            float centerY = mAttrs.pointLocation == Attrs.DOWN ? (rectF.centerY() + mAttrs.pointDistance) : (rectF.centerY() - mAttrs.pointDistance);
            Rect drawableBounds = DrawableUtil.getDrawableBounds((int) rectF.centerX(), (int) centerY, drawable);
            drawable.setBounds(drawableBounds);
            drawable.setAlpha(alphaColor);
            drawable.draw(canvas);
        }
    }

    //绘制节假日
    private void drawHolidayWorkday(Canvas canvas, RectF rectF, LocalDate localDate, Drawable holidayDrawable, Drawable workdayDrawable, int holidayTextColor, int workdayTextColor, int alphaColor) {
        if (mAttrs.showHolidayWorkday) {
            int[] holidayLocation = getHolidayWorkdayLocation(rectF.centerX(), rectF.centerY());
            if (mHolidayList.contains(localDate)) {
                if (holidayDrawable == null) {
                    mTextPaint.setTextSize(mAttrs.holidayWorkdayTextSize);
                    mTextPaint.setColor(holidayTextColor);
                    canvas.drawText(TextUtils.isEmpty(mAttrs.holidayText) ? mContext.getString(R.string.N_holidayText) : mAttrs.holidayText, holidayLocation[0], getTextBaseLineY(holidayLocation[1]), mTextPaint);
                } else {
                    Rect drawableBounds = DrawableUtil.getDrawableBounds(holidayLocation[0], holidayLocation[1], holidayDrawable);
                    holidayDrawable.setBounds(drawableBounds);
                    holidayDrawable.setAlpha(alphaColor);
                    holidayDrawable.draw(canvas);
                }
            } else if (mWorkdayList.contains(localDate)) {
                if (workdayDrawable == null) {
                    mTextPaint.setTextSize(mAttrs.holidayWorkdayTextSize);
                    mTextPaint.setColor(workdayTextColor);
                    mTextPaint.setFakeBoldText(mAttrs.holidayWorkdayTextBold);
                    canvas.drawText(TextUtils.isEmpty(mAttrs.workdayText) ? mContext.getString(R.string.N_workdayText) : mAttrs.workdayText, holidayLocation[0], getTextBaseLineY(holidayLocation[1]), mTextPaint);
                } else {
                    Rect drawableBounds = DrawableUtil.getDrawableBounds(holidayLocation[0], holidayLocation[1], workdayDrawable);
                    workdayDrawable.setBounds(drawableBounds);
                    workdayDrawable.setAlpha(alphaColor);
                    workdayDrawable.draw(canvas);
                }
            }
        }
    }

    //绘制拉伸的文字
    private void drawStretchText(Canvas canvas, RectF rectF, int alphaColor, LocalDate localDate) {
        float v = rectF.centerY() + mAttrs.stretchTextDistance;
        //超出当前矩形 不绘制
        if (v <= rectF.bottom) {
            String stretchText = mStretchStrMap.get(localDate);
            if (!TextUtils.isEmpty(stretchText)) {
                mTextPaint.setTextSize(mAttrs.stretchTextSize);
                mTextPaint.setColor(mAttrs.stretchTextColor);
                mTextPaint.setAlpha(alphaColor);
                mTextPaint.setFakeBoldText(mAttrs.stretchTextBold);
                canvas.drawText(stretchText, rectF.centerX(), rectF.centerY() + mAttrs.stretchTextDistance, mTextPaint);
            }
        }
    }

    //canvas.drawText的基准线
    private float getTextBaseLineY(float centerY) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return centerY - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
    }


    //HolidayWorkday的位置
    private int[] getHolidayWorkdayLocation(float centerX, float centerY) {
        int[] location = new int[2];
        switch (mAttrs.holidayWorkdayLocation) {
            case Attrs.TOP_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayWorkdayDistance);
                location[1] = (int) (centerY - mAttrs.holidayWorkdayDistance / 2);
                break;
            case Attrs.BOTTOM_RIGHT:
                location[0] = (int) (centerX + mAttrs.holidayWorkdayDistance);
                location[1] = (int) (centerY + mAttrs.holidayWorkdayDistance / 2);
                break;
            case Attrs.BOTTOM_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayWorkdayDistance);
                location[1] = (int) (centerY + mAttrs.holidayWorkdayDistance / 2);
                break;
            case Attrs.TOP_RIGHT:
            default:
                location[0] = (int) (centerX + mAttrs.holidayWorkdayDistance);
                location[1] = (int) (centerY - mAttrs.holidayWorkdayDistance / 2);
                break;
        }
        return location;

    }

    //设置标记
    public void setPointList(List<String> list) {
        mPointList.clear();
        for (int i = 0; i < list.size(); i++) {
            LocalDate localDate = null;
            try {
                localDate = new LocalDate(list.get(i));
            } catch (Exception e) {
                throw new RuntimeException("setPointList的参数需要 yyyy-MM-dd 格式的日期");
            }
            mPointList.add(localDate);
        }
        mCalendar.notifyCalendar();
    }

    public void addPointList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            LocalDate localDate = null;
            try {
                localDate = new LocalDate(list.get(i));
            } catch (Exception e) {
                throw new RuntimeException("setPointList的参数需要 yyyy-MM-dd 格式的日期");
            }
            if (!mPointList.contains(localDate)) {
                mPointList.add(localDate);
            }
        }
        mCalendar.notifyCalendar();
    }

    //设置替换农历的文字
    public void setReplaceLunarStrMap(Map<String, String> replaceLunarStrMap) {
        mReplaceLunarStrMap.clear();
        for (String key : replaceLunarStrMap.keySet()) {
            LocalDate localDate;
            try {
                localDate = new LocalDate(key);
            } catch (Exception e) {
                throw new RuntimeException("setReplaceLunarStrMap的参数需要 yyyy-MM-dd 格式的日期");
            }
            mReplaceLunarStrMap.put(localDate, replaceLunarStrMap.get(key));
        }
        mCalendar.notifyCalendar();
    }

    //设置替换农历的颜色
    public void setReplaceLunarColorMap(Map<String, Integer> replaceLunarColorMap) {
        mReplaceLunarColorMap.clear();
        for (String key : replaceLunarColorMap.keySet()) {
            LocalDate localDate;
            try {
                localDate = new LocalDate(key);
            } catch (Exception e) {
                throw new RuntimeException("setReplaceLunarColorMap的参数需要 yyyy-MM-dd 格式的日期");
            }
            mReplaceLunarColorMap.put(localDate, replaceLunarColorMap.get(key));
        }
        mCalendar.notifyCalendar();
    }


    //设置法定节假日和补班
    public void setLegalHolidayList(List<String> holidayList, List<String> workdayList) {
        mHolidayList.clear();
        mWorkdayList.clear();

        for (int i = 0; i < holidayList.size(); i++) {
            LocalDate holidayLocalDate;
            try {
                holidayLocalDate = new LocalDate(holidayList.get(i));
            } catch (Exception e) {
                throw new RuntimeException("setLegalHolidayList集合中的参数需要 yyyy-MM-dd 格式的日期");
            }
            mHolidayList.add(holidayLocalDate);
        }

        for (int i = 0; i < workdayList.size(); i++) {
            LocalDate workdayLocalDate;
            try {
                workdayLocalDate = new LocalDate(workdayList.get(i));
            } catch (Exception e) {
                throw new RuntimeException("setLegalHolidayList集合中的参数需要 yyyy-MM-dd 格式的日期");
            }
            mWorkdayList.add(workdayLocalDate);
        }
        mCalendar.notifyCalendar();
    }


    public void setStretchStrMap(Map<String, String> stretchStrMap) {
        mStretchStrMap.clear();
        for (String key : stretchStrMap.keySet()) {
            LocalDate localDate;
            try {
                localDate = new LocalDate(key);

            } catch (Exception e) {
                throw new RuntimeException("setStretchStrMap的参数需要 yyyy-MM-dd 格式的日期");
            }
            mStretchStrMap.put(localDate, stretchStrMap.get(key));
        }
        mCalendar.notifyCalendar();
    }
}
