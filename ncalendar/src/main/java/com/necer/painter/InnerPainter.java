package com.necer.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;

import com.necer.calendar.ICalendar;
import com.necer.entity.CalendarDate;
import com.necer.utils.Attrs;
import com.necer.utils.CalendarUtil;
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

        List<String> holidayList = CalendarUtil.getHolidayList();
        for (int i = 0; i < holidayList.size(); i++) {
            mHolidayList.add(new LocalDate(holidayList.get(i)));
        }
        List<String> workdayList = CalendarUtil.getWorkdayList();
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
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectDateList) {
        if (selectDateList.contains(localDate)) {
            drawSelectBg(canvas, rectF, noAlphaColor, true);
            drawSolar(canvas, rectF, localDate, noAlphaColor, true, true);
            drawLunar(canvas, rectF, true, noAlphaColor, localDate);
            drawPoint(canvas, rectF, true, noAlphaColor, localDate);
            drawHolidays(canvas, rectF, true, noAlphaColor, localDate);
        } else {
            drawSolar(canvas, rectF, localDate, noAlphaColor, false, true);
            drawLunar(canvas, rectF, false, noAlphaColor, localDate);
            drawPoint(canvas, rectF, false, noAlphaColor, localDate);
            drawHolidays(canvas, rectF, false, noAlphaColor, localDate);
        }
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectDateList) {
        if (selectDateList.contains(localDate)) {
            drawSelectBg(canvas, rectF, noAlphaColor, false);
            drawSolar(canvas, rectF, localDate, noAlphaColor, true, false);
            drawLunar(canvas, rectF, false, noAlphaColor, localDate);
            drawPoint(canvas, rectF, false, noAlphaColor, localDate);
            drawHolidays(canvas, rectF, false, noAlphaColor, localDate);
        } else {
            drawSolar(canvas, rectF, localDate, noAlphaColor, false, false);
            drawLunar(canvas, rectF, false, noAlphaColor, localDate);
            drawPoint(canvas, rectF, false, noAlphaColor, localDate);
            drawHolidays(canvas, rectF, false, noAlphaColor, localDate);
        }
    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectDateList) {
        if (selectDateList.contains(localDate)) {
            drawSelectBg(canvas, rectF, noAlphaColor, false);
            drawSolar(canvas, rectF, localDate, mAttrs.alphaColor, true, false);
            drawLunar(canvas, rectF, false, mAttrs.alphaColor, localDate);
            drawPoint(canvas, rectF, false, mAttrs.alphaColor, localDate);
            drawHolidays(canvas, rectF, false, mAttrs.alphaColor, localDate);
        } else {
            drawSolar(canvas, rectF, localDate, mAttrs.alphaColor, false, false);
            drawLunar(canvas, rectF, false, mAttrs.alphaColor, localDate);
            drawPoint(canvas, rectF, false, mAttrs.alphaColor, localDate);
            drawHolidays(canvas, rectF, false, mAttrs.alphaColor, localDate);
        }
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate) {
        drawSolar(canvas, rectF, localDate, mAttrs.disabledAlphaColor, false, false);
        drawLunar(canvas, rectF, false, mAttrs.disabledAlphaColor, localDate);
        drawPoint(canvas, rectF, false, mAttrs.disabledAlphaColor, localDate);
        drawHolidays(canvas, rectF, false, mAttrs.disabledAlphaColor, localDate);
    }


    //选中背景
    private void drawSelectBg(Canvas canvas, RectF rectF, int alphaColor, boolean isToday) {
        mCirclePaint.setStyle(isToday ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        mCirclePaint.setColor(isToday ? mAttrs.selectCircleColor : mAttrs.hollowCircleColor);
        mCirclePaint.setAlpha(alphaColor);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), mAttrs.selectCircleRadius, mCirclePaint);
    }


    //绘制公历
    private void drawSolar(Canvas canvas, RectF rectF, LocalDate date, int alphaColor, boolean isSelect, boolean isToday) {
        if (isSelect) {
            mTextPaint.setColor(isToday ? mAttrs.todaySolarSelectTextColor : mAttrs.solarTextColor);
        } else {
            mTextPaint.setColor(isToday ? mAttrs.todaySolarTextColor : mAttrs.solarTextColor);
        }
        mTextPaint.setAlpha(alphaColor);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(date.getDayOfMonth() + "", rectF.centerX(), mAttrs.isShowLunar ? rectF.centerY() : getBaseLineY(rectF), mTextPaint);
    }

    //绘制农历
    private void drawLunar(Canvas canvas, RectF rec, boolean isTodaySelect, int alphaColor, LocalDate localDate) {
        if (mAttrs.isShowLunar) {
            CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
            //优先顺序 替换的文字、农历节日、节气、公历节日、正常农历日期
            String lunarString = mReplaceLunarStrMap.get(calendarDate.localDate);
            if (lunarString == null) {
                if (!TextUtils.isEmpty(calendarDate.lunarHoliday)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.lunarHolidayTextColor);
                    lunarString = calendarDate.lunarHoliday;
                } else if (!TextUtils.isEmpty(calendarDate.solarTerm)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.solarTermTextColor);
                    lunarString = calendarDate.solarTerm;
                } else if (!TextUtils.isEmpty(calendarDate.solarHoliday)) {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.solarHolidayTextColor);
                    lunarString = calendarDate.solarHoliday;
                } else {
                    mTextPaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.lunarTextColor);
                    lunarString = calendarDate.lunar.lunarOnDrawStr;
                }
            }
            Integer color = mReplaceLunarColorMap.get(calendarDate.localDate);
            mTextPaint.setColor(color == null ? (isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.lunarTextColor) : color);
            mTextPaint.setTextSize(mAttrs.lunarTextSize);
            mTextPaint.setAlpha(alphaColor);
            canvas.drawText(lunarString, rec.centerX(), rec.centerY() + mAttrs.lunarDistance, mTextPaint);
        }
    }


    //绘制圆点
    private void drawPoint(Canvas canvas, RectF rectF, boolean isTodaySelect, int alphaColor, LocalDate date) {
        if (mPointList.contains(date)) {
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(isTodaySelect ? mAttrs.todaySelectContrastColor : mAttrs.pointColor);
            mCirclePaint.setAlpha(alphaColor);
            canvas.drawCircle(rectF.centerX(), mAttrs.pointLocation == Attrs.DOWN ? (rectF.centerY() + mAttrs.pointDistance) : (rectF.centerY() - mAttrs.pointDistance), mAttrs.pointSize, mCirclePaint);
        }
    }

    //绘制节假日
    private void drawHolidays(Canvas canvas, RectF rectF, boolean isTodaySelect, int alphaColor, LocalDate date) {
        if (mAttrs.isShowHoliday) {
            int[] holidayLocation = getHolidayLocation(rectF.centerX(), rectF.centerY());
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

    //canvas.drawText的基准线
    private int getBaseLineY(RectF rectF) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }

    //Holiday的位置
    private int[] getHolidayLocation(float centerX, float centerY) {
        int[] location = new int[2];
        int solarTexyCenterY = getSolarTextCenterY(centerY);
        switch (mAttrs.holidayLocation) {
            case Attrs.TOP_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayDistance);
                location[1] = solarTexyCenterY;
                break;
            case Attrs.BOTTOM_RIGHT:
                location[0] = (int) (centerX + mAttrs.holidayDistance);
                location[1] = (int) centerY;
                break;
            case Attrs.BOTTOM_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayDistance);
                location[1] = (int) centerY;
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
    private int getSolarTextCenterY(float centerY) {
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int ascent = fontMetricsInt.ascent;
        int descent = fontMetricsInt.descent;
        int textCenterY = (int) (descent / 2 + centerY + ascent / 2);//文字的中心y
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
