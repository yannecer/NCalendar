package com.necer.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.necer.drawable.TextDrawable
import com.necer.enumeration.CalendarState
import com.necer.painter.CalendarPainter
import com.necer.utils.NAttrs
import com.necer.utils.NDateUtil
import java.time.LocalDate

class NCalendarView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var monthDateList: List<LocalDate>
    private var monthRectFList: List<RectF>? = null

    private lateinit var weekDateList: List<LocalDate>
    private var weekRectFList: List<RectF>? = null

    private var canvasOffset: Float = -1f

    private lateinit var mGestureDetector: GestureDetector

    lateinit var pagerInitDate: LocalDate

    private lateinit var nViewPager: NViewPager

    private lateinit var calendarPainter: CalendarPainter

    private var backgroundRect: Rect = Rect()
    private var monthStateY = 0
    private var weekStateY = 0

    private var bgDrawable: Drawable? = null


    constructor(context: Context?) : this(context, null)


    constructor(context: Context?, viewGroup: ViewGroup, localDate: LocalDate) : this(context, null) {
        this.pagerInitDate = localDate
        nViewPager = viewGroup as NViewPager

        monthDateList = NDateUtil.getMonthDate(pagerInitDate, NAttrs.firstDayOfWeek, NAttrs.allMonthSixLine)
        weekDateList = NDateUtil.getWeekDate(pagerInitDate, NAttrs.firstDayOfWeek)

        calendarPainter = nViewPager.getCalendarPainter()

        mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return nViewPager.getCalendarState() != null
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {

                val rectFList: List<RectF>
                val dateList: List<LocalDate>
                if (nViewPager.getCalendarState() == CalendarState.WEEK) {
                    rectFList = weekRectFList!!
                    dateList = weekDateList
                } else {
                    rectFList = monthRectFList!!
                    dateList = monthDateList
                }

                for (i in rectFList.indices) {
                    val rectF = rectFList[i]
                    if (rectF.contains(e.x, e.y)) {
                        val clickDate = dateList[i]
                        nViewPager.setClickDate(clickDate)
                        break
                    }
                }
                return true
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (monthStateY == 0) {
            monthStateY = nViewPager.getMonthStateY()
            weekStateY = nViewPager.getWeekStateY()
        }

        if (nViewPager.getCalendarState() == CalendarState.WEEK) {
            drawWeekData(canvas)
        } else {
            canvas.translate(0f, -canvasOffset)
            drawBgDrawable(canvas)
            drawMonthDate(canvas)
        }
    }

    private fun drawBgDrawable(canvas: Canvas) {
        if (bgDrawable == null) {
            bgDrawable = if (NAttrs.calendarBackground != null) {
                NAttrs.calendarBackground
            } else if (NAttrs.showNumberBackground) {
                TextDrawable()
            } else {
                null
            }
        }

        if (bgDrawable != null) {
            val canvasHeight = nViewPager.getCanvasHeight()
            backgroundRect.set(0, 0, width, canvasHeight.toInt())
            val childY = nViewPager.getChildY()
            var alpha = (childY - weekStateY) / (monthStateY - weekStateY)
            if (alpha > 1) {
                alpha = 1f
            }

            (bgDrawable as? TextDrawable)?.setText(nViewPager.fulcrumDate.monthValue.toString())
            bgDrawable?.bounds = backgroundRect
            bgDrawable?.alpha = (alpha * NAttrs.backgroundAlphaColor).toInt()
            bgDrawable?.draw(canvas)
        }

    }


    private fun drawWeekData(canvas: Canvas) {
        for (j in 0..6) {
            val rectF = getRealRectF(0, j)
            val index = 0 * 7 + j
            val checkedDateList = nViewPager.getTotalCheckedDateList()
            val localDate = weekDateList[index]
            if (nViewPager.isAvailable(localDate)) {
                if (NDateUtil.isToday(localDate)) {
                    calendarPainter.onDrawToday(canvas, rectF, localDate, checkedDateList)
                } else {
                    calendarPainter.onDrawCurrentMonthOrWeek(canvas, rectF, localDate, checkedDateList)
                }
            } else {
                calendarPainter.onDrawDisableDate(canvas, rectF, localDate)
            }
        }
    }

    private fun drawMonthDate(canvas: Canvas) {

        for (i in 0 until monthDateList.size / 7) {
            for (j in 0..6) {
                val rectF = getRealRectF(i, j)
                val index = i * 7 + j
                val checkedDateList = nViewPager.getTotalCheckedDateList()
                val localDate = monthDateList.get(index)
                if (nViewPager.isAvailable(localDate)) {
                    if (NDateUtil.isLastMonth(pagerInitDate, localDate) || NDateUtil.isNextMonth(pagerInitDate, localDate)) {
                        calendarPainter.onDrawLastOrNextMonth(canvas, rectF, localDate, checkedDateList)
                    } else if (NDateUtil.isToday(localDate)) {
                        calendarPainter.onDrawToday(canvas, rectF, localDate, checkedDateList)
                    } else {
                        calendarPainter.onDrawCurrentMonthOrWeek(canvas, rectF, localDate, checkedDateList)
                    }
                } else {
                    calendarPainter.onDrawDisableDate(canvas, rectF, localDate)
                }

            }
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mGestureDetector.onTouchEvent(event)
    }


    private fun getRectFList(dateList: List<LocalDate>): List<RectF> {
        val rectFList = arrayListOf<RectF>()
        val lineCount = dateList.size / 7

        for (i in 0 until lineCount) {
            for (j in 0..6) {
                val rectF = resetRectFSize(RectF(), i, j, dateList)
                rectFList.add(rectF)
            }
        }
        return rectFList
    }


    private fun getRealRectF(lineIndex: Int, columnIndex: Int): RectF {
        val index = lineIndex * 7 + columnIndex
        if (nViewPager.getCalendarState() == CalendarState.WEEK) {
            if (weekRectFList == null) {
                weekRectFList = getRectFList(weekDateList)
            }
            return weekRectFList!![index]
        } else {
            if (monthRectFList == null) {
                monthRectFList = getRectFList(monthDateList)
            }

            val canvasHeight = nViewPager.getCanvasHeight()
            if (canvasHeight > nViewPager.getMonthHeight()) {
                return resetRectFSize(monthRectFList!![index], lineIndex, columnIndex, monthDateList)
            } else {
                return monthRectFList!![index]
            }
        }
    }


    private fun resetRectFSize(rectF: RectF, lineIndex: Int, columnIndex: Int, dateList: List<LocalDate>): RectF {

        val lineCount = dateList.size / 7
        val canvasHeight = nViewPager.getCanvasHeight()

        if (lineCount == 1 || lineCount == 5) {
            //周 5行月
            val rectHeight: Float = canvasHeight / 5
            rectF.set((columnIndex * width / 7).toFloat(), lineIndex * rectHeight, (columnIndex * width / 7 + width / 7).toFloat(), lineIndex * rectHeight + rectHeight)

        } else if (lineCount == 6) {

            //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
            //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
            val rectHeight5 = canvasHeight / 5
            val rectHeight6 = (canvasHeight / 5 * 4 / 5).toFloat()

            rectF.set(
                (columnIndex * width / 7).toFloat(),
                lineIndex * rectHeight6 + (rectHeight5 - rectHeight6) / 2,
                (columnIndex * width / 7 + width / 7).toFloat(),
                lineIndex * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2
            )
        }
        return rectF
    }


    fun offsetCanvas(canvasIncreaseOffset: Float) {

        //先计算偏移，并且只有在周往下滑动时才计算）
        val childY = Math.round(nViewPager.getChildY())
        if (canvasOffset == -1f && canvasIncreaseOffset < 0 && childY >= weekStateY && childY < monthStateY) {
            canvasOffset = getHoldHeight()
        }

        if (nViewPager.getCanvasHeight() == nViewPager.getMonthHeight().toFloat()) {
            canvasOffset += canvasIncreaseOffset
            if (canvasOffset < 0) {
                canvasOffset = 0.0F
            } else if (canvasOffset > getHoldHeight()) {
                canvasOffset = getHoldHeight()
            }
        } else {
            canvasOffset = 0f
        }

        invalidate()
    }

    fun getHoldHeight(): Float {
        val monthHeight = nViewPager.getMonthHeight().toFloat()
        val lineNum = monthDateList.size / 7

        val h = if (lineNum == 5) {
            val rectHeight5 = (monthHeight / 5)
            getHoldLine() * rectHeight5
        } else {
            val rectHeight6 = (monthHeight / 5 * 4 / 5)
            getHoldLine() * rectHeight6
        }
        return h
    }


    private fun getHoldLine(): Int {
        val fulcrumDate = nViewPager.fulcrumDate
        return monthDateList.indexOf(fulcrumDate) / 7
    }

    fun getFirstLocalDate(): LocalDate {
        return if (nViewPager.getCalendarState() == CalendarState.WEEK) {
            weekDateList[0]
        } else {
            LocalDate.of(pagerInitDate.year, pagerInitDate.monthValue, 1)
        }
    }

    fun getCurrPageCheckedDateList(): List<LocalDate> {
        val currentCheckedDateList: MutableList<LocalDate> = ArrayList()
        val totalCheckedDateList = nViewPager.getTotalCheckedDateList()
        val currDateList = if (nViewPager.getCalendarState() == CalendarState.WEEK) {
            weekDateList
        } else {
            monthDateList
        }
        currDateList.forEach {
            if (totalCheckedDateList.contains(it)) {
                currentCheckedDateList.add(it)
            }
        }

        return currentCheckedDateList
    }

    fun getCurrPagerDateList(): List<LocalDate> {
        return if (nViewPager.getCalendarState() == CalendarState.WEEK) {
            weekDateList
        } else {
            monthDateList
        }
    }

}