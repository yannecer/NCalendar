package com.necer.painter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.necer.calendar.ICalendar
import com.necer.R
import com.necer.utils.NAttrs
import com.necer.utils.hutool.ChineseDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @author necer
 * @date 2019/1/3
 */
class InnerPainter(private val mContext: Context, val iCalendar: ICalendar) : CalendarPainter {
    private val mTextPaint: Paint = Paint()
    private val noAlphaColor = 255
    private val mHolidayList: MutableList<LocalDate>
    private val mWorkdayList: MutableList<LocalDate>
    private val mPointList: MutableList<LocalDate>
    private val mReplaceLunarStrMap: MutableMap<LocalDate, String?>
    private val mReplaceLunarColorMap: MutableMap<LocalDate, Int?>
    private val mStretchStrMap: MutableMap<LocalDate, String?>
    private val mDefaultCheckedBackground: Drawable?
    private val mTodayCheckedBackground: Drawable?
    private val mDefaultCheckedPoint: Drawable?
    private val mDefaultUnCheckedPoint: Drawable?
    private val mTodayCheckedPoint: Drawable?
    private val mTodayUnCheckedPoint: Drawable?
    private val mDateFormatter:DateTimeFormatter =DateTimeFormatter.ofPattern("yyyy-M-d")

    init {
        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
        mPointList = ArrayList()
        mHolidayList = ArrayList()
        mWorkdayList = ArrayList()
        mReplaceLunarStrMap = HashMap()
        mReplaceLunarColorMap = HashMap()
        mStretchStrMap = HashMap()
        mDefaultCheckedBackground = ContextCompat.getDrawable(mContext, NAttrs.defaultCheckedBackground)
        mTodayCheckedBackground = ContextCompat.getDrawable(mContext, NAttrs.todayCheckedBackground)
        mDefaultCheckedPoint = ContextCompat.getDrawable(mContext, NAttrs.defaultCheckedPoint)
        mDefaultUnCheckedPoint = ContextCompat.getDrawable(mContext, NAttrs.defaultUnCheckedPoint)
        mTodayCheckedPoint = ContextCompat.getDrawable(mContext, NAttrs.todayCheckedPoint)
        mTodayUnCheckedPoint = ContextCompat.getDrawable(mContext, NAttrs.todayUnCheckedPoint)
    }

    override fun onDrawToday(canvas: Canvas, rectF: RectF, localDate: LocalDate, checkedDateList: List<LocalDate>) {
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mTodayCheckedBackground, rectF, noAlphaColor)
            drawSolar(canvas, rectF, localDate, NAttrs.todayCheckedSolarTextColor, noAlphaColor)
            drawLunar(canvas, rectF, localDate, NAttrs.todayCheckedLunarTextColor, noAlphaColor)
            drawPoint(canvas, rectF, localDate, mTodayCheckedPoint, noAlphaColor)
            drawHolidayWorkday(canvas, rectF, localDate, NAttrs.todayCheckedHoliday, NAttrs.todayCheckedWorkday, NAttrs.todayCheckedHolidayTextColor, NAttrs.todayCheckedWorkdayTextColor, noAlphaColor)
        } else {
            drawSolar(canvas, rectF, localDate, NAttrs.todayUnCheckedSolarTextColor, noAlphaColor)
            drawLunar(canvas, rectF, localDate, NAttrs.todayUnCheckedLunarTextColor, noAlphaColor)
            drawPoint(canvas, rectF, localDate, mTodayUnCheckedPoint, noAlphaColor)
            drawHolidayWorkday(
                canvas,
                rectF,
                localDate,
                NAttrs.todayUnCheckedHoliday,
                NAttrs.todayUnCheckedWorkday,
                NAttrs.todayUnCheckedHolidayTextColor,
                NAttrs.todayUnCheckedWorkdayTextColor,
                noAlphaColor
            )
        }
        drawStretchText(canvas, rectF, noAlphaColor, localDate)
    }

    override fun onDrawCurrentMonthOrWeek(canvas: Canvas, rectF: RectF, localDate: LocalDate, checkedDateList: List<LocalDate>) {
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mDefaultCheckedBackground, rectF, noAlphaColor)
            drawSolar(canvas, rectF, localDate, NAttrs.defaultCheckedSolarTextColor, noAlphaColor)
            drawLunar(canvas, rectF, localDate, NAttrs.defaultCheckedLunarTextColor, noAlphaColor)
            drawPoint(canvas, rectF, localDate, mDefaultCheckedPoint, noAlphaColor)
            drawHolidayWorkday(
                canvas,
                rectF,
                localDate,
                NAttrs.defaultCheckedHoliday,
                NAttrs.defaultCheckedWorkday,
                NAttrs.defaultCheckedHolidayTextColor,
                NAttrs.defaultCheckedWorkdayTextColor,
                noAlphaColor
            )
        } else {
            drawSolar(canvas, rectF, localDate, NAttrs.defaultUnCheckedSolarTextColor, noAlphaColor)
            drawLunar(canvas, rectF, localDate, NAttrs.defaultUnCheckedLunarTextColor, noAlphaColor)
            drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, noAlphaColor)
            drawHolidayWorkday(
                canvas,
                rectF,
                localDate,
                NAttrs.defaultUnCheckedHoliday,
                NAttrs.defaultUnCheckedWorkday,
                NAttrs.defaultUnCheckedHolidayTextColor,
                NAttrs.defaultUnCheckedWorkdayTextColor,
                noAlphaColor
            )
        }
        drawStretchText(canvas, rectF, noAlphaColor, localDate)
    }

    override fun onDrawLastOrNextMonth(canvas: Canvas, rectF: RectF, localDate: LocalDate, checkedDateList: List<LocalDate>) {
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mDefaultCheckedBackground, rectF, NAttrs.lastNextMothAlphaColor)
            drawSolar(canvas, rectF, localDate, NAttrs.defaultCheckedSolarTextColor, NAttrs.lastNextMothAlphaColor)
            drawLunar(canvas, rectF, localDate, NAttrs.defaultCheckedLunarTextColor, NAttrs.lastNextMothAlphaColor)
            drawPoint(canvas, rectF, localDate, mDefaultCheckedPoint, NAttrs.lastNextMothAlphaColor)
            drawHolidayWorkday(
                canvas,
                rectF,
                localDate,
                NAttrs.defaultCheckedHoliday,
                NAttrs.defaultCheckedWorkday,
                NAttrs.defaultCheckedHolidayTextColor,
                NAttrs.defaultCheckedWorkdayTextColor,
                NAttrs.lastNextMothAlphaColor
            )
        } else {
            drawSolar(canvas, rectF, localDate, NAttrs.defaultUnCheckedSolarTextColor, NAttrs.lastNextMothAlphaColor)
            drawLunar(canvas, rectF, localDate, NAttrs.defaultUnCheckedLunarTextColor, NAttrs.lastNextMothAlphaColor)
            drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, NAttrs.lastNextMothAlphaColor)
            drawHolidayWorkday(
                canvas,
                rectF,
                localDate,
                NAttrs.defaultUnCheckedHoliday,
                NAttrs.defaultUnCheckedWorkday,
                NAttrs.defaultUnCheckedHolidayTextColor,
                NAttrs.defaultUnCheckedWorkdayTextColor,
                NAttrs.lastNextMothAlphaColor
            )
        }
        drawStretchText(canvas, rectF, NAttrs.lastNextMothAlphaColor, localDate)
    }

    override fun onDrawDisableDate(canvas: Canvas, rectF: RectF, localDate: LocalDate) {
        drawSolar(canvas, rectF, localDate, NAttrs.defaultUnCheckedSolarTextColor, NAttrs.disabledAlphaColor)
        drawLunar(canvas, rectF, localDate, NAttrs.defaultUnCheckedLunarTextColor, NAttrs.disabledAlphaColor)
        drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, NAttrs.disabledAlphaColor)
        drawHolidayWorkday(
            canvas,
            rectF,
            localDate,
            NAttrs.defaultUnCheckedHoliday,
            NAttrs.defaultUnCheckedWorkday,
            NAttrs.defaultUnCheckedHolidayTextColor,
            NAttrs.defaultUnCheckedWorkdayTextColor,
            NAttrs.disabledAlphaColor
        )
        drawStretchText(canvas, rectF, NAttrs.disabledAlphaColor, localDate)
    }

    //选中背景
    private fun drawCheckedBackground(canvas: Canvas?, drawable: Drawable?, rectF: RectF?, alphaColor: Int) {
        val drawableBounds: Rect = getDrawableBounds(rectF!!.centerX().toInt(), rectF.centerY().toInt(), drawable)
        drawable!!.bounds = drawableBounds
        drawable.alpha = alphaColor
        drawable.draw(canvas!!)
    }

    //绘制公历
    private fun drawSolar(canvas: Canvas?, rectF: RectF?, date: LocalDate?, color: Int, alphaColor: Int) {
        mTextPaint.color = color
        mTextPaint.alpha = alphaColor
        mTextPaint.textSize = NAttrs.solarTextSize
        mTextPaint.isFakeBoldText = NAttrs.solarTextBold
        canvas!!.drawText(date!!.dayOfMonth.toString() + "", rectF!!.centerX(), if (NAttrs.showLunar) rectF.centerY() else getTextBaseLineY(rectF.centerY()), mTextPaint)
    }

    //绘制农历
    private fun drawLunar(canvas: Canvas?, rectF: RectF?, localDate: LocalDate, color: Int, alphaColor: Int) {
        if (NAttrs.showLunar) {
            val chineseDate = ChineseDate(localDate)
            //农历部分文字展示优先顺序 替换的文字、农历节日、节气、公历节日、正常农历日期
            val lunarString: String
            val replaceString = mReplaceLunarStrMap[localDate]
            lunarString = if (!TextUtils.isEmpty(replaceString)) {
                replaceString!!
            } else if (!TextUtils.isEmpty(chineseDate.getLunarFestivals())) {
                chineseDate.getLunarFestivals()!!
            } else if (!TextUtils.isEmpty(chineseDate.getTerm())) {
                chineseDate.getTerm()
            } else if (!TextUtils.isEmpty(chineseDate.getSolarFestivals())) {
                chineseDate.getSolarFestivals()!!
            } else {
                var chineseText = chineseDate.getChineseDay()
                if (chineseText == "初一") {
                    chineseText = chineseDate.getChineseMonthName()
                }
                chineseText
            }
            val replaceColor = mReplaceLunarColorMap[localDate]
            mTextPaint.color = replaceColor ?: color
            mTextPaint.textSize = NAttrs.lunarTextSize
            mTextPaint.alpha = alphaColor
            mTextPaint.isFakeBoldText = NAttrs.lunarTextBold
            canvas!!.drawText(lunarString, rectF!!.centerX(), rectF.centerY() + NAttrs.lunarDistance, mTextPaint)
        }
    }

    //绘制标记
    private fun drawPoint(canvas: Canvas?, rectF: RectF?, date: LocalDate?, drawable: Drawable?, alphaColor: Int) {
        if (mPointList.contains(date)) {
            val centerY = if (NAttrs.pointLocation == NAttrs.DOWN) rectF!!.centerY() + NAttrs.pointDistance else rectF!!.centerY() - NAttrs.pointDistance
            val drawableBounds: Rect = getDrawableBounds(rectF.centerX().toInt(), centerY.toInt(), drawable)
            drawable!!.bounds = drawableBounds
            drawable.alpha = alphaColor
            drawable.draw(canvas!!)
        }
    }

    //绘制节假日
    private fun drawHolidayWorkday(
        canvas: Canvas,
        rectF: RectF,
        localDate: LocalDate,
        holidayDrawable: Drawable?,
        workdayDrawable: Drawable?,
        holidayTextColor: Int,
        workdayTextColor: Int,
        alphaColor: Int
    ) {
        if (NAttrs.showHolidayWorkday) {
            val holidayLocation = getHolidayWorkdayLocation(rectF.centerX(), rectF.centerY())
            if (mHolidayList.contains(localDate)) {
                if (holidayDrawable == null) {
                    mTextPaint.textSize = NAttrs.holidayWorkdayTextSize
                    mTextPaint.color = holidayTextColor
                    mTextPaint.alpha = alphaColor
                    canvas.drawText(
                        if (TextUtils.isEmpty(NAttrs.holidayText)) mContext.getString(R.string.N_holidayText) else NAttrs.holidayText!!, holidayLocation[0].toFloat(), getTextBaseLineY(
                            holidayLocation[1].toFloat()
                        ), mTextPaint
                    )
                } else {
                    val drawableBounds: Rect = getDrawableBounds(holidayLocation[0], holidayLocation[1], holidayDrawable)
                    holidayDrawable.bounds = drawableBounds
                    holidayDrawable.alpha = alphaColor
                    holidayDrawable.draw(canvas)
                }
            } else if (mWorkdayList.contains(localDate)) {
                if (workdayDrawable == null) {
                    mTextPaint.textSize = NAttrs.holidayWorkdayTextSize
                    mTextPaint.color = workdayTextColor
                    mTextPaint.alpha = alphaColor
                    mTextPaint.isFakeBoldText = NAttrs.holidayWorkdayTextBold
                    canvas.drawText(
                        if (TextUtils.isEmpty(NAttrs.workdayText)) mContext.getString(R.string.N_workdayText) else NAttrs.workdayText!!, holidayLocation[0].toFloat(), getTextBaseLineY(
                            holidayLocation[1].toFloat()
                        ), mTextPaint
                    )
                } else {
                    val drawableBounds: Rect = getDrawableBounds(holidayLocation[0], holidayLocation[1], workdayDrawable)
                    workdayDrawable.bounds = drawableBounds
                    workdayDrawable.alpha = alphaColor
                    workdayDrawable.draw(canvas)
                }
            }
        }
    }

    //绘制拉伸的文字
    private fun drawStretchText(canvas: Canvas?, rectF: RectF?, alphaColor: Int, localDate: LocalDate?) {

        val v = rectF!!.centerY() + NAttrs.stretchTextDistance
        //超出当前矩形 不绘制
        if (v <= rectF.bottom) {
            val stretchText = mStretchStrMap[localDate]
            if (!TextUtils.isEmpty(stretchText)) {
                mTextPaint.textSize = NAttrs.stretchTextSize
                mTextPaint.color = NAttrs.stretchTextColor
                mTextPaint.alpha = alphaColor
                mTextPaint.isFakeBoldText = NAttrs.stretchTextBold
                canvas!!.drawText(stretchText!!, rectF.centerX(), rectF.centerY() + NAttrs.stretchTextDistance, mTextPaint)
            }
        }
    }

    //canvas.drawText的基准线
    private fun getTextBaseLineY(centerY: Float): Float {
        val fontMetrics = mTextPaint.fontMetrics
        return centerY - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top
    }

    //HolidayWorkday的位置
    private fun getHolidayWorkdayLocation(centerX: Float, centerY: Float): IntArray {
        val location = IntArray(2)
        when (NAttrs.holidayWorkdayLocation) {
            NAttrs.TOP_LEFT -> {
                location[0] = (centerX - NAttrs.holidayWorkdayDistance).toInt()
                location[1] = (centerY - NAttrs.holidayWorkdayDistance / 2).toInt()
            }
            NAttrs.BOTTOM_RIGHT -> {
                location[0] = (centerX + NAttrs.holidayWorkdayDistance).toInt()
                location[1] = (centerY + NAttrs.holidayWorkdayDistance / 2).toInt()
            }
            NAttrs.BOTTOM_LEFT -> {
                location[0] = (centerX - NAttrs.holidayWorkdayDistance).toInt()
                location[1] = (centerY + NAttrs.holidayWorkdayDistance / 2).toInt()
            }
            NAttrs.TOP_RIGHT -> {
                location[0] = (centerX + NAttrs.holidayWorkdayDistance).toInt()
                location[1] = (centerY - NAttrs.holidayWorkdayDistance / 2).toInt()
            }
            else -> {
                location[0] = (centerX + NAttrs.holidayWorkdayDistance).toInt()
                location[1] = (centerY - NAttrs.holidayWorkdayDistance / 2).toInt()
            }
        }
        return location
    }

    //设置标记
    fun setPointList(list: List<String?>) {
        mPointList.clear()
        for (i in list.indices) {
            var localDate: LocalDate? = null
            localDate = try {
                LocalDate.parse(list[i],mDateFormatter)
            } catch (e: Exception) {
                throw RuntimeException("setPointList的参数需要 yyyy-MM-dd 格式的日期")
            }
            mPointList.add(localDate)
        }
        iCalendar.notifyCalendar()
    }

    fun addPointList(list: List<String?>) {
        for (i in list.indices) {
            var localDate: LocalDate? = null
            localDate = try {
                LocalDate.parse(list[i],mDateFormatter)
            } catch (e: Exception) {
                throw RuntimeException("setPointList的参数需要 yyyy-MM-dd 格式的日期")
            }
            if (!mPointList.contains(localDate)) {
                mPointList.add(localDate)
            }
        }
        iCalendar.notifyCalendar()
    }

    //设置替换农历的文字
    fun setReplaceLunarStrMap(replaceLunarStrMap: Map<String?, String?>) {
        mReplaceLunarStrMap.clear()
        for (key in replaceLunarStrMap.keys) {
            val localDate: LocalDate = try {
                LocalDate.parse(key,mDateFormatter)
            } catch (e: Exception) {
                throw RuntimeException("setReplaceLunarStrMap的参数需要 yyyy-MM-dd 格式的日期")
            }
            mReplaceLunarStrMap[localDate] = replaceLunarStrMap[key]
        }
        iCalendar.notifyCalendar()
    }

    //设置替换农历的颜色
    fun setReplaceLunarColorMap(replaceLunarColorMap: Map<String?, Int?>) {
        mReplaceLunarColorMap.clear()
        for (key in replaceLunarColorMap.keys) {
            val localDate: LocalDate = try {
                LocalDate.parse(key,mDateFormatter)
            } catch (e: Exception) {
                throw RuntimeException("setReplaceLunarColorMap的参数需要 yyyy-MM-dd 格式的日期")
            }
            mReplaceLunarColorMap[localDate] = replaceLunarColorMap[key]
        }
        iCalendar.notifyCalendar()
    }

    //设置法定节假日和补班
    fun setLegalHolidayList(holidayList: List<String?>, workdayList: List<String?>) {
        mHolidayList.clear()
        mWorkdayList.clear()
        for (i in holidayList.indices) {
            val holidayLocalDate: LocalDate = try {
                LocalDate.parse(holidayList[i],mDateFormatter)
            } catch (e: Exception) {
                throw RuntimeException("setLegalHolidayList集合中的参数需要 yyyy-MM-dd 格式的日期")
            }
            mHolidayList.add(holidayLocalDate)
        }
        for (i in workdayList.indices) {
            val workdayLocalDate: LocalDate = try {
                LocalDate.parse(workdayList[i],mDateFormatter)
            } catch (e: Exception) {
                throw RuntimeException("setLegalHolidayList集合中的参数需要 yyyy-MM-dd 格式的日期")
            }
            mWorkdayList.add(workdayLocalDate)
        }
        iCalendar.notifyCalendar()
    }

    fun setStretchStrMap(stretchStrMap: Map<String, String>) {
        mStretchStrMap.clear()
        for (key in stretchStrMap.keys) {
            val localDate: LocalDate = try {
                LocalDate.parse(key,mDateFormatter)
            } catch (e: Exception) {
                throw RuntimeException("setStretchStrMap的参数需要 yyyy-MM-dd 格式的日期")
            }
            mStretchStrMap[localDate] = stretchStrMap[key]
        }
        iCalendar.notifyCalendar()
    }


    private fun getDrawableBounds(centerX: Int, centerY: Int, drawable: Drawable?): Rect {
        if (drawable == null) {
            return Rect()
        }
        return Rect(
            centerX - drawable.intrinsicWidth / 2,
            centerY - drawable.intrinsicHeight / 2,
            centerX + drawable.intrinsicWidth / 2,
            centerY + drawable.intrinsicHeight / 2
        )
    }
}