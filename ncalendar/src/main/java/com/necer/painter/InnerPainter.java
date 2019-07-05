package com.necer.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.necer.calendar.ICalendar;
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

    private ICalendar mCalendar;

    public InnerPainter(ICalendar calendar) {
        this.mAttrs = calendar.getAttrs();
        this.mCalendar = calendar;
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
    public void onDrawToday(Canvas canvas, Rect rect, LocalDate localDate, List<LocalDate> selectDateList) {
        if (selectDateList.contains(localDate)) {
            drawSelectBg(canvas, rect, noAlphaColor, true);
            drawSolar(canvas, rect, localDate, noAlphaColor, true, true);
            drawLunar(canvas, rect, true, noAlphaColor, localDate);
            drawPoint(canvas, rect, true, noAlphaColor, localDate);
            drawHolidays(canvas, rect, true, noAlphaColor, localDate);
        } else {
            drawSolar(canvas, rect, localDate, noAlphaColor, false, true);
            drawLunar(canvas, rect, false, noAlphaColor, localDate);
            drawPoint(canvas, rect, false, noAlphaColor, localDate);
            drawHolidays(canvas, rect, false, noAlphaColor, localDate);
        }
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, Rect rect, LocalDate localDate, List<LocalDate> selectDateList) {
        if (selectDateList.contains(localDate)) {
            drawSelectBg(canvas, rect, noAlphaColor, false);
            drawSolar(canvas, rect, localDate, noAlphaColor, true, false);
            drawLunar(canvas, rect, false, noAlphaColor, localDate);
            drawPoint(canvas, rect, false, noAlphaColor, localDate);
            drawHolidays(canvas, rect, false, noAlphaColor, localDate);
        } else {
            drawSolar(canvas, rect, localDate, noAlphaColor, false, false);
            drawLunar(canvas, rect, false, noAlphaColor, localDate);
            drawPoint(canvas, rect, false, noAlphaColor, localDate);
            drawHolidays(canvas, rect, false, noAlphaColor, localDate);
        }
    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, Rect rect, LocalDate localDate, List<LocalDate> selectDateList) {
        if (selectDateList.contains(localDate)) {
            drawSelectBg(canvas, rect, noAlphaColor, false);
            drawSolar(canvas, rect, localDate, mAttrs.alphaColor, true, false);
            drawLunar(canvas, rect, false, mAttrs.alphaColor, localDate);
            drawPoint(canvas, rect, false, mAttrs.alphaColor, localDate);
            drawHolidays(canvas, rect, false, mAttrs.alphaColor, localDate);
        } else {
            drawSolar(canvas, rect, localDate, mAttrs.alphaColor, false, false);
            drawLunar(canvas, rect, false, mAttrs.alphaColor, localDate);
            drawPoint(canvas, rect, false, mAttrs.alphaColor, localDate);
            drawHolidays(canvas, rect, false, mAttrs.alphaColor, localDate);
        }
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, Rect rect, LocalDate localDate) {
        drawSolar(canvas, rect, localDate, mAttrs.disabledAlphaColor, false, false);
        drawLunar(canvas, rect, false, mAttrs.disabledAlphaColor, localDate);
        drawPoint(canvas, rect, false, mAttrs.disabledAlphaColor, localDate);
        drawHolidays(canvas, rect, false, mAttrs.disabledAlphaColor, localDate);
    }


    //选中背景
    private void drawSelectBg(Canvas canvas, Rect rect, int alphaColor, boolean isToday) {
        mCirclePaint.setStyle(isToday ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        mCirclePaint.setColor(isToday ? mAttrs.selectCircleColor : mAttrs.hollowCircleColor);
        mCirclePaint.setAlpha(alphaColor);
        canvas.drawCircle(rect.centerX(), rect.centerY(), mAttrs.selectCircleRadius, mCirclePaint);
    }


    //绘制公历
    private void drawSolar(Canvas canvas, Rect rect, LocalDate date, int alphaColor, boolean isSelect, boolean isToday) {
        if (isSelect) {
            mTextPaint.setColor(isToday ? mAttrs.todaySolarSelectTextColor : mAttrs.solarTextColor);
        } else {
            mTextPaint.setColor(isToday ? mAttrs.todaySolarTextColor : mAttrs.solarTextColor);
        }
        mTextPaint.setAlpha(alphaColor);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), mAttrs.isShowLunar ? rect.centerY() : getBaseLineY(rect), mTextPaint);
    }

    //绘制农历
    private void drawLunar(Canvas canvas, Rect rec, boolean isTodaySelect, int alphaColor, LocalDate localDate) {
        if (mAttrs.isShowLunar) {
            NDate nDate = Util.getNDate(localDate);
            //优先顺序 替换的文字、农历节日、节气、公历节日、正常农历日期
            String lunarString = mReplaceLunarStrMap.get(nDate.localDate);
            if (lunarString == null) {
                if (!TextUtils.isEmpty(nDate.lunarHoliday)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.lunarHolidayTextColor);
                    lunarString = nDate.lunarHoliday;
                } else if (!TextUtils.isEmpty(nDate.solarTerm)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.solarTermTextColor);
                    lunarString = nDate.solarTerm;
                } else if (!TextUtils.isEmpty(nDate.solarHoliday)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.solarHolidayTextColor);
                    lunarString = nDate.solarHoliday;
                } else {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.lunarTextColor);
                    lunarString = nDate.lunar.lunarDrawStr;
                }
            }
            Integer color = mReplaceLunarColorMap.get(nDate.localDate);
            mTextPaint.setColor(color == null ? (isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.lunarTextColor) : color);
            mTextPaint.setTextSize(mAttrs.lunarTextSize);
            mTextPaint.setAlpha(alphaColor);
            canvas.drawText(lunarString, rec.centerX(), rec.centerY() + mAttrs.lunarDistance, mTextPaint);
        }
    }


    //绘制圆点
    private void drawPoint(Canvas canvas, Rect rect, boolean isTodaySelect, int alphaColor, LocalDate date) {
        if (mPointList.contains(date)) {
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.pointColor);
            mCirclePaint.setAlpha(alphaColor);
            canvas.drawCircle(rect.centerX(), mAttrs.pointLocation == Attrs.DOWN ? (rect.centerY() + mAttrs.pointDistance) : (rect.centerY() - mAttrs.pointDistance), mAttrs.pointSize, mCirclePaint);
        }
    }

    //绘制节假日
    private void drawHolidays(Canvas canvas, Rect rect, boolean isTodaySelect, int alphaColor, LocalDate date) {
        if (mAttrs.isShowHoliday) {
            int[] holidayLocation = getHolidayLocation(rect.centerX(), rect.centerY());
            mTextPaint.setTextSize(mAttrs.holidayTextSize);
            if (mHolidayList.contains(date)) {
                mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.holidayColor);
                mTextPaint.setAlpha(alphaColor);
                canvas.drawText("休", holidayLocation[0], holidayLocation[1], mTextPaint);
            } else if (mWorkdayList.contains(date)) {
                mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.workdayColor);
                mTextPaint.setAlpha(alphaColor);
                canvas.drawText("班", holidayLocation[0], holidayLocation[1], mTextPaint);
            }
        }
    }

    private int getBaseLineY(Rect rect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        return baseLineY;
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

}
