package com.necer.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.necer.MyLog;
import com.necer.entity.NDate;
import com.necer.utils.Attrs;
import com.necer.utils.Util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by necer on 2019/1/3.
 */
public class InnerPainter implements CalendarPainter {

    private Attrs mAttrs;
    protected Paint mTextPaint;
    protected Paint mCirclePaint;

    private int noAlphaColor = 255;

    protected List<LocalDate> mHolidayList;
    protected List<LocalDate> mWorkdayList;

    private List<LocalDate> mPointList;
    private Map<LocalDate, String> mReplaceLunarStrMap;
    private Map<LocalDate, Integer> mReplaceLunarColorMap;


    public InnerPainter(Attrs attrs) {
        this.mAttrs = attrs;

        mTextPaint = getPaint();
        mCirclePaint = getPaint();
        mPointList = new ArrayList<>();
        mHolidayList = new ArrayList<>();
        mWorkdayList = new ArrayList<>();
        mReplaceLunarStrMap = new HashMap<>();
        mReplaceLunarColorMap = new HashMap<>();

        List<String> holidayList = Util.getHolidayList();
        for (int i = 0; i < holidayList.size(); i++) {
            mHolidayList.add(new LocalDate(holidayList.get(i)));
        }
        List<String> workdayList = Util.getWorkdayList();
        for (int i = 0; i < workdayList.size(); i++) {
            mWorkdayList.add(new LocalDate(workdayList.get(i)));
        }
    }


    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }


    @Override
    public void onDrawToday(Canvas canvas, Rect rect, NDate nDate, boolean isSelect) {
        if (isSelect) {
            drawSolidCircle(canvas, rect);
            drawTodaySolar(canvas, rect, true, nDate.localDate);
            drawLunar(canvas, rect, true, noAlphaColor, nDate);
            drawPoint(canvas, rect, true, noAlphaColor, nDate.localDate);
            drawHolidays(canvas, rect, true, noAlphaColor, nDate.localDate);
        } else {
            drawTodaySolar(canvas, rect, false, nDate.localDate);
            drawLunar(canvas, rect, false, noAlphaColor, nDate);
            drawPoint(canvas, rect, false, noAlphaColor, nDate.localDate);
            drawHolidays(canvas, rect, false, noAlphaColor, nDate.localDate);
        }
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, Rect rect, NDate nDate) {
        drawOtherSolar(canvas, rect, mAttrs.disabledAlphaColor, nDate.localDate);
        drawLunar(canvas, rect, false, mAttrs.disabledAlphaColor, nDate);
        drawPoint(canvas, rect, false, mAttrs.disabledAlphaColor, nDate.localDate);
        drawHolidays(canvas, rect, false, mAttrs.disabledAlphaColor, nDate.localDate);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, Rect rect, NDate nDate, boolean isSelect) {
        if (isSelect) {
            drawHollowCircle(canvas, rect);
            drawOtherSolar(canvas, rect, noAlphaColor, nDate.localDate);
            drawLunar(canvas, rect, false, noAlphaColor, nDate);
            drawPoint(canvas, rect, false, noAlphaColor, nDate.localDate);
            drawHolidays(canvas, rect, false, noAlphaColor, nDate.localDate);
        } else {
            drawOtherSolar(canvas, rect, noAlphaColor, nDate.localDate);
            drawLunar(canvas, rect, false, noAlphaColor, nDate);
            drawPoint(canvas, rect, false, noAlphaColor, nDate.localDate);
            drawHolidays(canvas, rect, false, noAlphaColor, nDate.localDate);
        }
    }

    @Override
    public void onDrawNotCurrentMonth(Canvas canvas, Rect rect, NDate nDate) {
        drawOtherSolar(canvas, rect, mAttrs.alphaColor, nDate.localDate);
        drawLunar(canvas, rect, false, mAttrs.alphaColor, nDate);
        drawPoint(canvas, rect, false, mAttrs.alphaColor, nDate.localDate);
        drawHolidays(canvas, rect, false, mAttrs.alphaColor, nDate.localDate);
    }


    //空心圆
    private void drawHollowCircle(Canvas canvas, Rect rect) {
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        mCirclePaint.setColor(mAttrs.hollowCircleColor);
        mCirclePaint.setAlpha(noAlphaColor);
        canvas.drawCircle(rect.centerX(), rect.centerY(), mAttrs.selectCircleRadius, mCirclePaint);
    }

    //实心圆
    private void drawSolidCircle(Canvas canvas, Rect rect) {
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        mCirclePaint.setColor(mAttrs.selectCircleColor);
        mCirclePaint.setAlpha(noAlphaColor);
        canvas.drawCircle(rect.centerX(), rect.centerY(), mAttrs.selectCircleRadius, mCirclePaint);

    }

    //今天的公历
    private void drawTodaySolar(Canvas canvas, Rect rect, boolean isSelect, LocalDate date) {
        if (isSelect) {
            mTextPaint.setColor(mAttrs.todaySolarSelectTextColor);
        } else {
            mTextPaint.setColor(mAttrs.todaySolarTextColor);
        }
        mTextPaint.setAlpha(noAlphaColor);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), mAttrs.isShowLunar ? rect.centerY() : getBaseLineY(rect), mTextPaint);
    }

    //绘制公历
    private void drawOtherSolar(Canvas canvas, Rect rect, int alphaColor, LocalDate date) {
        mTextPaint.setColor(mAttrs.solarTextColor);
        mTextPaint.setAlpha(alphaColor);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), mAttrs.isShowLunar ? rect.centerY() : getBaseLineY(rect), mTextPaint);

    }

    //绘制圆点
    private void drawPoint(Canvas canvas, Rect rect, boolean isTodaySelect, int alphaColor, LocalDate date) {
        if (mPointList != null && mPointList.contains(date)) {
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(isTodaySelect ? mAttrs.bgCalendarColor : mAttrs.pointColor);
            mCirclePaint.setAlpha(alphaColor);
            canvas.drawCircle(rect.centerX(), mAttrs.pointLocation == Attrs.DOWN ? (rect.centerY() + mAttrs.pointDistance) : (rect.centerY() - mAttrs.pointDistance), mAttrs.pointSize, mCirclePaint);
        }
    }

    //绘制农历
    private void drawLunar(Canvas canvas, Rect rec, boolean isTodaySelect, int alphaColor, NDate nDate) {
        if (mAttrs.isShowLunar) {
            //优先顺序 替换的文字、农历节日、节气、公历节日、正常农历日期
            String lunarString = mReplaceLunarStrMap.get(nDate.localDate);
            if (lunarString == null) {
                if (!TextUtils.isEmpty(nDate.lunarHoliday)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.bgCalendarColor : mAttrs.lunarHolidayTextColor);
                    lunarString = nDate.lunarHoliday;
                } else if (!TextUtils.isEmpty(nDate.solarTerm)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.bgCalendarColor : mAttrs.solarTermTextColor);
                    lunarString = nDate.solarTerm;
                } else if (!TextUtils.isEmpty(nDate.solarHoliday)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.bgCalendarColor : mAttrs.solarHolidayTextColor);
                    lunarString = nDate.solarHoliday;
                } else {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.bgCalendarColor : mAttrs.lunarTextColor);
                    lunarString = nDate.lunar.lunarDrawStr;
                }
            } else {
                Integer color = mReplaceLunarColorMap.get(nDate.localDate);
                if (color == null) {
                    mTextPaint.setColor(mAttrs.lunarTextColor);
                } else {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.bgCalendarColor : color);
                }
            }
            mTextPaint.setTextSize(mAttrs.lunarTextSize);
            mTextPaint.setAlpha(alphaColor);
            canvas.drawText(lunarString, rec.centerX(), rec.centerY() + mAttrs.lunarDistance, mTextPaint);
        }
    }


    //绘制节假日
    private void drawHolidays(Canvas canvas, Rect rect, boolean isTodaySelect, int alphaColor, LocalDate date) {
        if (mAttrs.isShowHoliday) {
            int[] holidayLocation = getHolidayLocation(rect.centerX(), rect.centerY());
            mTextPaint.setTextSize(mAttrs.holidayTextSize);
            if (mHolidayList.contains(date)) {
                mTextPaint.setColor(isTodaySelect ? mAttrs.bgCalendarColor : mAttrs.holidayColor);
                mTextPaint.setAlpha(alphaColor);
                canvas.drawText("休", holidayLocation[0], holidayLocation[1], mTextPaint);
            } else if (mWorkdayList.contains(date)) {
                mTextPaint.setColor(isTodaySelect ? mAttrs.bgCalendarColor : mAttrs.workdayColor);
                mTextPaint.setAlpha(alphaColor);
                canvas.drawText("班", holidayLocation[0], holidayLocation[1], mTextPaint);
            }
        }
    }

    //Holiday的位置
    private int[] getHolidayLocation(int centerX, int centerY) {
        int[] location = new int[2];
        int solarTexyCenterY = getSolarTextCenterY(centerY);
        switch (mAttrs.holidayLocation) {
            case Attrs.TOP_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayDistance);
                location[1] = solarTexyCenterY;
                break;
            case Attrs.BOTTOM_RIGHT:
                location[0] = (int) (centerX + mAttrs.holidayDistance);
                location[1] = centerY;
                break;
            case Attrs.BOTTOM_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayDistance);
                location[1] = centerY;
                break;
            case Attrs.TOP_RIGHT:
            default:
                location[0] = (int) (centerX + mAttrs.holidayDistance);
                location[1] = solarTexyCenterY;
                break;
        }
        return location;

    }


    //公历文字的竖直中心y
    private int getSolarTextCenterY(int centerY) {
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int ascent = fontMetricsInt.ascent;
        int descent = fontMetricsInt.descent;
        int textCenterY = descent / 2 + centerY + ascent / 2;//文字的中心y
        return textCenterY;
    }


    private int getBaseLineY(Rect rect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }


    //设置标记
    public void setPointList(List<String> list) {
        List<LocalDate> localDates = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            try {
                localDates.add(new LocalDate(list.get(i)));
            } catch (Exception e) {
                throw new RuntimeException("setPointList的参数需要 yyyy-MM-dd 格式的日期");
            }
        }
        mPointList.clear();
        mPointList.addAll(localDates);
    }

    //设置替换农历的文字
    public void setReplaceLunarStrMap(Map<String, String> replaceLunarStrMap) {
        Map<LocalDate, String> map = new HashMap<>();
        for (String key : replaceLunarStrMap.keySet()) {
            try {
                map.put(new LocalDate(key), replaceLunarStrMap.get(key));
            } catch (Exception e) {
                throw new RuntimeException("setReplaceLunarStrMap的参数需要 yyyy-MM-dd 格式的日期");
            }
        }
        mReplaceLunarStrMap.clear();
        mReplaceLunarStrMap.putAll(map);
    }

    //设置替换农历的颜色
    public void setReplaceLunarColorMap(Map<String, Integer> replaceLunarColorMap) {
        Map<LocalDate, Integer> map = new HashMap<>();
        for (String key : replaceLunarColorMap.keySet()) {
            try {
                map.put(new LocalDate(key), replaceLunarColorMap.get(key));
            } catch (Exception e) {
                throw new RuntimeException("setReplaceLunarColorMap的参数需要 yyyy-MM-dd 格式的日期");
            }
        }
        mReplaceLunarColorMap.clear();
        mReplaceLunarColorMap.putAll(map);
    }


    //设置法定节假日和补班
    public void setHolidayAndWorkdayList(List<String> holidayList, List<String> workdayList) {
        mHolidayList.clear();
        mWorkdayList.clear();
        try{
            for (int i = 0; i < holidayList.size(); i++) {
                mHolidayList.add(new LocalDate(holidayList.get(i)));
            }
            for (int i = 0; i < workdayList.size(); i++) {
                mWorkdayList.add(new LocalDate(workdayList.get(i)));
            }
        }catch (Exception e){
            throw new RuntimeException("setHolidayAndWorkdayList集合中的参数需要 yyyy-MM-dd 格式的日期");
        }

    }

}
