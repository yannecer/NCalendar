package com.necer.calendar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent
import com.necer.R
import com.necer.enumeration.CalendarState
import com.necer.enumeration.CheckModel
import com.necer.enumeration.MultipleCountModel
import com.necer.listener.*
import com.necer.painter.CalendarPainter
import com.necer.utils.NAttrs
import com.necer.utils.NAttrsUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 日历
 * 1、NCalendar中包含子View时，日历为月周切换日历
 * 2、NCalendar中包含子View时，日历为单个日历，单独使用，是周日历还是月日历由 defaultCalendar 的值决定
 */
class NCalendar(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs), ICalendar,
    NestedScrollingParent {


    private var nWeekBar: NWeekBar
    private var nViewPager: NViewPager

    private var monthHeight = 0
    private var stretchHeight = 0
    private var weekHeight = 0

    private var monthStateY = 0
    private var weekStateY = 0
    private var stretchStateY = 0


    private var valueAnimator: ValueAnimator = ValueAnimator()

    private var previousValue = 0f//松手动画开始之后 用于计算动画的增量

    private var childView: FrameLayout//NCalendar内部包含的直接子view，直接子view并不一定是NestScrillChild
    private var targetView: View? = null //实际滑动的view 当targetView==null时，子view没有NestScrillChild，此时滑动交给onTochEvent处理

    private var isLayout: Boolean = false

    private var lastCalendarState: CalendarState? = null

    private var onCalendarStateChangedListener: OnCalendarStateChangedListener? = null

    private var isWeekHoldEnable: Boolean = false


    init {

        orientation = VERTICAL
        isMotionEventSplittingEnabled = false

        //自定义属性赋值
        NAttrsUtil.setAttrs(context, attrs)

        monthHeight = NAttrs.calendarHeight
        weekHeight = monthHeight / 5
        stretchHeight = NAttrs.stretchCalendarHeight

        if (stretchHeight <= monthHeight) {
            throw RuntimeException("拉伸状态的高度必须大于月日历高度")
        }

        nWeekBar = NWeekBar(context, null)
        nViewPager = NViewPager(context, null)
        addView(
            nWeekBar,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                if (NAttrs.weekBarHeight == 0) LayoutParams.WRAP_CONTENT else NAttrs.weekBarHeight
            )
        )
        addView(nViewPager, LayoutParams(LayoutParams.MATCH_PARENT, stretchHeight))
        childView = FrameLayout(context)
        childView.isClickable = true
        childView.setBackgroundColor(Color.WHITE)
        addView(childView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 4) {
            throw RuntimeException(context.getString(R.string.N_NCalendar_child_num))
        }
        if (childCount == 4) {
            val view = getChildAt(3)
            (view.parent as ViewGroup).removeView(view)
            childView.addView(
                view,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!isLayout) {
            if (childView.childCount != 0) {
                val childLayoutParams = childView.layoutParams
                childLayoutParams.height = measuredHeight - weekHeight - nWeekBar.measuredHeight

                monthStateY = nWeekBar.measuredHeight + monthHeight
                weekStateY = nWeekBar.measuredHeight + weekHeight
                stretchStateY = nWeekBar.measuredHeight + stretchHeight

            } else {

                val height = when (getCalendarState()) {
                    CalendarState.WEEK -> weekHeight
                    CalendarState.MONTH -> monthHeight
                    CalendarState.MONTH_STRETCH -> stretchHeight
                    else -> 0
                }

                //日历中没有子view
                val viewPagerLayoutParams = nViewPager.layoutParams
                viewPagerLayoutParams.height = height

                //计算NCalendar的高度
                layoutParams.height = height + nWeekBar.measuredHeight
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (!isLayout) {
            val childY = when (getCalendarState()) {
                CalendarState.WEEK -> weekStateY
                CalendarState.MONTH -> monthStateY
                CalendarState.MONTH_STRETCH -> stretchStateY
                else -> 0
            }
            childView.y = childY.toFloat()
            isLayout = true
        }
    }


    private var downY = 0f
    private var downX = 0f

    //上次的y
    private var lastY = 0f

    //竖直方向上滑动的临界值，大于这个值认为是竖直滑动
    private val verticalY = 30f

    private var directionY = 0 // 1 竖直向上  -1 竖直向下
    private var isFirstMove = true


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {


        if (childView.childCount == 0) {
            return super.onInterceptTouchEvent(ev)
        }

        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = ev.y
                downX = ev.x
                lastY = downY
            }
            MotionEvent.ACTION_MOVE -> {
                //中间状态
                if (nViewPager.getCalendarState() == null) {
                    return true
                }

                val y = ev.y
                val offsetY = downY - y

                //onInterceptTouchEvent返回true，触摸事件交给当前的onTouchEvent处理
                return interceptTouch(offsetY)

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (downY > weekStateY && downY < childView.y) {
                    isFirstMove = true
                    autoScroll(NAttrs.animationDuration.toLong())
                }

            }

        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun interceptTouch(offsetY: Float): Boolean {


        val absY = Math.abs(offsetY)
        var condition = false

        if (targetView == null) {
            condition = true
        } else if (isInCalendar() && !targetView!!.canScrollVertically(-1)) {
            condition = true
        }
        return condition && absY > verticalY
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                var dy = lastY - y
                if (isFirstMove) {
                    // 防止第一次的偏移量过大
                    if (dy >= verticalY) {
                        dy -= verticalY
                    } else if (dy < -verticalY) {
                        dy += verticalY
                    }

                    isFirstMove = false
                }
                directionY = if (dy > 0) 1 else -1

                if (Math.abs(dy) > 0) {
                    gestureMove(dy)
                    lastY = y
                }

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isFirstMove = true
                autoScroll(NAttrs.animationDuration.toLong())
            }
        }
        return super.onTouchEvent(event)
    }

    private fun gestureMove(dy: Float) {

        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }

        if (Math.round(childView.y) == weekStateY && isWeekHoldEnable) {
            return
        }

        var childY = childView.y - dy
        val maxY = if (NAttrs.stretchCalendarEnable) stretchStateY else monthStateY
        if (childY <= weekStateY) {
            childY = weekStateY.toFloat()
        } else if (childY >= maxY) {
            childY = maxY.toFloat()
        }

        childView.y = childY
        calendarTranslate(dy)

        val preCalendarState = getPreCalendarState(childView.y)
        if (preCalendarState != null) {
            nViewPager.setCalendarState(preCalendarState)
        }


    }

    private fun calendarTranslate(dy: Float) {
        if (Math.round(dy) == 0) {
            return
        }
        val holdHeight = nViewPager.getHoldHeight()
        val heightOffset = monthHeight - weekHeight
        val translateRate = holdHeight / heightOffset
        nViewPager.offsetView(dy * translateRate)
    }


    private fun isInCalendar(): Boolean {
        val calendarState = nViewPager.getCalendarState()
        if (calendarState === CalendarState.MONTH) {
            return downY <= monthStateY
        } else if (calendarState === CalendarState.WEEK) {
            return downY <= weekStateY
        } else if (calendarState === CalendarState.MONTH_STRETCH) {
            return downY <= stretchStateY
        }
        return false
    }


    private fun autoScroll(duration: Long) {

        if (valueAnimator.isRunning) {
            return
        }

        val startY: Float = childView.y
        val endY: Int

        if (directionY == 1) {
            //向上
            endY = if (startY >= weekStateY && startY <= monthStateY) {
                if (startY < monthStateY - weekHeight / 2) {
                    weekStateY
                } else {
                    monthStateY
                }
            } else {
                if (startY < stretchStateY - (stretchStateY - monthStateY) / 2) {
                    monthStateY
                } else {
                    stretchStateY
                }
            }
        } else {
            //向下
            endY = if (startY >= weekStateY && startY <= monthStateY) {
                if (startY > weekStateY + weekHeight / 2) {
                    monthStateY
                } else {
                    weekStateY
                }
            } else {
                if (startY > monthStateY + (stretchStateY - monthStateY) / 2) {
                    stretchStateY
                } else {
                    monthStateY
                }
            }
        }

        val preCalendarState = getPreCalendarState(endY.toFloat())

        //位置不变 且 状态不变的时候 不执行动画
        if (startY == endY.toFloat() && lastCalendarState == preCalendarState) {
            return
        }

        startAutoScroll(startY, endY.toFloat(), duration)

    }

    private fun startAutoScroll(startY: Float, endY: Float, duration: Long) {

        valueAnimator = ValueAnimator()
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener { animation ->
            val currentValue = animation.animatedValue as Float
            if (previousValue != 0f) {
                val delta = previousValue - currentValue
                calendarTranslate(delta)
                childView.y = childView.y - delta
            }
            previousValue = currentValue // 更新之前的值

        }

        valueAnimator.addListener(object : OnEndAnimatorListener() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                previousValue = 0f

                val endCalendarState = getPreCalendarState(childView.y)

                if (endCalendarState != null) {
                    nViewPager.setCalendarState(endCalendarState)
                    onCalendarStateChangedListener?.onCalendarStateChange(endCalendarState)
                    lastCalendarState = endCalendarState
                }
            }
        })

        valueAnimator.setFloatValues(startY, endY)
        valueAnimator.start()
    }


    fun getCanvasHeight(): Float {
        return if (childView.childCount == 0 || childView.y <= monthStateY) {
            monthHeight.toFloat()
        } else {
            return childView.y - nWeekBar.measuredHeight
        }
    }

    fun getChildY(): Float {
        return childView.y
    }

    fun getMonthStateY(): Int {
        return monthStateY
    }

    fun getWeekStateY(): Int {
        return weekStateY
    }

    override fun getCalendarState(): CalendarState? {
        return nViewPager.getCalendarState()
    }

    private fun getPreCalendarState(childY: Float): CalendarState? {
        when (Math.round(childY)) {
            weekStateY -> {
                return CalendarState.WEEK
            }
            monthStateY -> {
                return CalendarState.MONTH
            }
            stretchStateY -> {
                return CalendarState.MONTH_STRETCH
            }
        }
        return null
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        targetView = target

        if (isWeekHoldEnable && calendarState === CalendarState.WEEK) {
            return false
        }
        return true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)

        val childY = childView.y.toInt()

        directionY = if (dy > 0) 1 else -1

        if (childY > weekStateY && childY <= stretchStateY) {
            consumed[1] = dy
            gestureMove(dy.toFloat())
        } else if (dy < 0 && !targetView!!.canScrollVertically(-1)) {
            consumed[1] = dy
            gestureMove(dy.toFloat())
        }

    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {

        //快速滑动时 快速自动滑动到对应的状态
        if (velocityY > 6000) {
            autoScroll(50)
        } else if (velocityY < -6000) {
            autoScroll(50)
        }

        //真正的快速滑动在周状态才行
        return Math.round(childView.y) != weekStateY
    }


    override fun onStopNestedScroll(child: View) {
        super.onStopNestedScroll(child)
        targetView = null
        autoScroll(NAttrs.animationDuration.toLong())
    }

    override fun toWeek() {
        startAutoScroll(childView.y, weekStateY.toFloat(), NAttrs.animationDuration.toLong())
    }

    override fun toMonth() {
        startAutoScroll(childView.y, monthStateY.toFloat(), NAttrs.animationDuration.toLong())
    }

    override fun toStretch() {
        if (NAttrs.stretchCalendarEnable) {
            startAutoScroll(childView.y, stretchStateY.toFloat(), NAttrs.animationDuration.toLong())
        }
    }

    override fun setWeekHoldEnable(isWeekHoldEnable: Boolean) {
        this.isWeekHoldEnable = isWeekHoldEnable
    }


    override fun setCheckMode(checkModel: CheckModel) {
        nViewPager.setCheckMode(checkModel)
    }

    override fun getCheckModel(): CheckModel {
        return nViewPager.getCheckMode()
    }

    override fun setMultipleCount(multipleCount: Int, multipleCountModel: MultipleCountModel?) {
        nViewPager.setMultipleCount(multipleCount, multipleCountModel)
    }


    override fun jumpDate(formatDate: String?) {
        val localDate = LocalDate.parse(formatDate, DateTimeFormatter.ofPattern("yyyy-M-d"))
        nViewPager.jumpDate(localDate, true)
    }

    override fun jumpDate(year: Int, month: Int, day: Int) {
        val localDate = LocalDate.of(year, month, day)
        nViewPager.jumpDate(localDate, true)
    }

    override fun jumpMonth(year: Int, month: Int) {
        val localDate = LocalDate.of(year, month, 1)
        nViewPager.jumpDate(localDate, false)
    }

    override fun toLastPager() {
        nViewPager.toLastPager()
    }

    override fun toNextPager() {
        nViewPager.toNextPager()
    }

    override fun toToday() {
        nViewPager.toToday()
    }

    override fun setCalendarPainter(calendarPainter: CalendarPainter?) {
        nViewPager.setCalendarPainter(calendarPainter)
    }

    override fun notifyCalendar() {
        nViewPager.notifyAllView()
    }

    override fun setInitializeDate(formatInitializeDate: String?) {
        nViewPager.setInitializeDate(
            LocalDate.parse(
                formatInitializeDate,
                DateTimeFormatter.ofPattern("yyyy-M-dd")
            )
        )
    }

    override fun setDateInterval(
        startFormatDate: String?,
        endFormatDate: String?,
        formatInitializeDate: String?
    ) {
        nViewPager.setDateInterval(startFormatDate, endFormatDate, formatInitializeDate)
    }

    override fun setDateInterval(startFormatDate: String?, endFormatDate: String?) {
        nViewPager.setDateInterval(startFormatDate, endFormatDate)
    }

    override fun setOnCalendarChangedListener(onCalendarChangedListener: OnCalendarChangedListener?) {
        nViewPager.setOnCalendarChangedListener(onCalendarChangedListener)
    }

    override fun setOnCalendarMultipleChangedListener(onCalendarMultipleChangedListener: OnCalendarMultipleChangedListener?) {
        nViewPager.setOnCalendarMultipleChangedListener(onCalendarMultipleChangedListener)
    }

    override fun setOnClickDisableDateListener(onClickDisableDateListener: OnClickDisableDateListener?) {
        nViewPager.setOnClickDisableDateListener(onClickDisableDateListener)
    }

    override fun getCalendarPainter(): CalendarPainter {
        return nViewPager.getCalendarPainter()
    }

    override fun getTotalCheckedDateList(): List<LocalDate> {
        return nViewPager.getTotalCheckedDateList()
    }

    override fun getCurrPagerCheckDateList(): List<LocalDate> {
        return nViewPager.getCurrPagerCheckedDateList()
    }

    override fun getCurrPagerDateList(): List<LocalDate> {
        return nViewPager.getCurrPagerDateList()
    }

    override fun setCheckedDates(dateList: List<String>) {
        nViewPager.setCheckedDates(dateList)
    }

    override fun setOnCalendarStateChangedListener(onCalendarStateChangedListener: OnCalendarStateChangedListener?) {
        this.onCalendarStateChangedListener = onCalendarStateChangedListener
    }


}