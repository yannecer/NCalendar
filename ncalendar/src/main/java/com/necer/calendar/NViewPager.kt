package com.necer.calendar

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.necer.R
import com.necer.enumeration.CalendarState
import com.necer.enumeration.CheckModel
import com.necer.enumeration.DateChangeBehavior
import com.necer.enumeration.MultipleCountModel
import com.necer.listener.OnCalendarChangedListener
import com.necer.listener.OnCalendarMultipleChangedListener
import com.necer.listener.OnClickDisableDateListener
import com.necer.painter.CalendarPainter
import com.necer.painter.InnerPainter
import com.necer.utils.NAttrs
import com.necer.utils.NDateUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 *
 *
 */
class NViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    private var calendarState: CalendarState? = null

    private var monthHeight = 0

    private val totalCheckedDateList = arrayListOf<LocalDate>()


    private var calendarPainter: CalendarPainter? = null
    private lateinit var checkModel: CheckModel

    private var defaultCheckedFirstDate = false //默认选择时，翻页选中第一个日期


    private var dateChangeBehavior: DateChangeBehavior = DateChangeBehavior.INITIALIZE

    private var onCalendarChangedListener: OnCalendarChangedListener? = null
    private var onClickDisableDateListener: OnClickDisableDateListener? = null
    private var onCalendarMultipleChangedListener: OnCalendarMultipleChangedListener? = null

    private var multipleCountModel: MultipleCountModel? = null
    private var multipleCount: Int = 0

    private lateinit var startDate: LocalDate
    private lateinit var endDate: LocalDate

    private val defaultStartDate = LocalDate.parse("1901-01-01")
    private val defaultEndDateDate = LocalDate.parse("2099-12-31")

    private lateinit var initDate: LocalDate

    lateinit var fulcrumDate: LocalDate

    private var lastCallbackDate: LocalDate? = null

    init {

        addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                post {

                    val nCalendarView = findViewWithTag(currentItem) as? NCalendarView
                    val currentViewInitDate = nCalendarView?.pagerInitDate
                    //算出相差几页
                    var localDate = if (calendarState == CalendarState.WEEK) {
                        val intervalWeek = NDateUtil.getIntervalWeek(fulcrumDate, currentViewInitDate!!, NAttrs.firstDayOfWeek)
                        fulcrumDate.plusWeeks(intervalWeek.toLong())
                    } else {
                        val intervalMonths = NDateUtil.getIntervalMonths(fulcrumDate, currentViewInitDate!!)
                        fulcrumDate.plusMonths(intervalMonths.toLong())
                    }

                    localDate = getAvailableDate(localDate)

                    if (checkModel == CheckModel.SINGLE_DEFAULT_CHECKED) {

                        if (defaultCheckedFirstDate) {
                            localDate = nCalendarView.getFirstLocalDate()
                        }

                        totalCheckedDateList.clear()
                        totalCheckedDateList.add(localDate)
                    }

                    //重新赋值   fulcrumDate
                    val currPageCheckedList = nCalendarView.getCurrPageCheckedDateList()
                    fulcrumDate = if (currPageCheckedList.isEmpty()) {
                        localDate
                    } else {
                        currPageCheckedList[0]
                    }

                    notifyAllView()
                    dateChangedCallBack()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == SCROLL_STATE_DRAGGING) {
                    dateChangeBehavior = DateChangeBehavior.PAGE
                }

            }
        })

        calendarState = CalendarState.valueOf(NAttrs.defaultCalendar)
        monthHeight = NAttrs.calendarHeight
        defaultCheckedFirstDate = NAttrs.defaultCheckedFirstDate

        val initDate: LocalDate = LocalDate.now()
        val startDate = defaultStartDate
        val endDate = defaultEndDateDate
        init(startDate, endDate, initDate)

        setCheckMode(CheckModel.SINGLE_DEFAULT_CHECKED)

    }

    private fun init(startDate: LocalDate, endDate: LocalDate, initDate: LocalDate) {

        this.startDate = startDate
        this.endDate = endDate
        this.initDate = getAvailableDate(initDate)
        this.fulcrumDate = this.initDate

        require(!startDate.isAfter(endDate)) { context.getString(R.string.N_start_after_end) }
        require(!startDate.isBefore(defaultStartDate)) { context.getString(R.string.N_start_before_19010101) }
        require(!endDate.isAfter(defaultEndDateDate)) { context.getString(R.string.N_end_after_20991231) }
        require(!(startDate.isAfter(initDate) || endDate.isBefore(initDate))) { context.getString(R.string.N_initialize_date_illegal) }

        val monthTotalPages = NDateUtil.getIntervalMonths(startDate, endDate) + 1
        val weekTotalPages = NDateUtil.getIntervalWeek(startDate, endDate, NAttrs.firstDayOfWeek) + 1


        val totalPages = when (calendarState) {
            CalendarState.WEEK -> weekTotalPages
            CalendarState.MONTH -> monthTotalPages
            CalendarState.MONTH_STRETCH -> monthTotalPages
            else -> 0
        }.toInt()

        adapter = NPagerAdapter(initDate, calendarState, totalPages, NDateUtil.getIntervalMonths(startDate, initDate))
        currentItem = NDateUtil.getIntervalMonths(startDate, initDate)

    }

    fun setDateInterval(startFormatDate: String?, endFormatDate: String?, formatInitializeDate: String?) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        init(LocalDate.parse(startFormatDate,dateFormatter), LocalDate.parse(endFormatDate,dateFormatter), LocalDate.parse(formatInitializeDate))
    }

    fun setDateInterval(startFormatDate: String?, endFormatDate: String?) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        init(LocalDate.parse(startFormatDate,dateFormatter), LocalDate.parse(endFormatDate,dateFormatter), LocalDate.now())
    }

    fun setInitializeDate(localDate: LocalDate) {
        init(defaultStartDate, defaultEndDateDate, localDate)
    }

    fun setMultipleCount(multipleCount: Int, multipleCountModel: MultipleCountModel?) {
        setCheckMode(CheckModel.MULTIPLE)
        this.multipleCount = multipleCount
        this.multipleCountModel = multipleCountModel
    }

    fun setCalendarState(calendarState: CalendarState) {
        this.calendarState = calendarState
        if (calendarState == CalendarState.WEEK) {
            val intervalWeek = NDateUtil.getIntervalWeek(startDate, fulcrumDate, NAttrs.firstDayOfWeek)
            val weekTotalPages = NDateUtil.getIntervalWeek(startDate, endDate, NAttrs.firstDayOfWeek) + 1
            (adapter as NPagerAdapter).setCalendarState(fulcrumDate, calendarState, weekTotalPages, intervalWeek)
            setCurrentItem(intervalWeek, false)
        } else {
            val intervalMonth = NDateUtil.getIntervalMonths(startDate, fulcrumDate)
            val monthTotalPages = NDateUtil.getIntervalMonths(startDate, endDate) + 1
            (adapter as NPagerAdapter).setCalendarState(fulcrumDate, calendarState, monthTotalPages, intervalMonth)
            setCurrentItem(intervalMonth, false)
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (NAttrs.horizontalScrollEnable) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (NAttrs.horizontalScrollEnable) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (calendarState == null) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }


    //点击的日期是否可用
    fun isAvailable(localDate: LocalDate): Boolean {
        return !localDate.isBefore(startDate) && !localDate.isAfter(endDate)
    }

    //日期边界处理
    private fun getAvailableDate(localDate: LocalDate): LocalDate {
        return if (localDate.isBefore(startDate)) {
            startDate
        } else if (localDate.isAfter(endDate)) {
            endDate
        } else {
            localDate
        }
    }

    fun getCalendarState(): CalendarState? {
        return calendarState
    }

    fun getCanvasHeight(): Float {
        return (parent as NCalendar).getCanvasHeight()
    }

    fun getMonthHeight(): Int {
        return monthHeight
    }

    fun getHoldHeight(): Float {
        val nCalendarView = findViewWithTag<NCalendarView>(currentItem)
        return nCalendarView.getHoldHeight()
    }


    fun offsetView(dy: Float) {
        calendarState = null
        val nCalendarView = findViewWithTag<NCalendarView>(currentItem)
        nCalendarView.offsetCanvas(dy)
    }

    fun setClickDate(checkedDate: LocalDate) {

        if (calendarState == CalendarState.WEEK) {
            jump(checkedDate, true, DateChangeBehavior.CLICK)
        } else {
            val nCalendarView = findViewWithTag(currentItem) as NCalendarView
            val pagerInitDate = nCalendarView.pagerInitDate
            if (NDateUtil.isEqualsMonth(checkedDate, pagerInitDate) || !NAttrs.horizontalScrollEnable) {
                jump(checkedDate, true, DateChangeBehavior.CLICK)
            } else {
                if (NAttrs.lastNextMonthClickEnable) {
                    jump(checkedDate, true, DateChangeBehavior.CLICK_PAGE)
                }
            }
        }
    }

    fun notifyAllView() {
        //刷新所有页面
        for (j in 0 until childCount) {
            (getChildAt(j) as NCalendarView).invalidate()
        }
    }

    fun setCheckMode(checkModel: CheckModel) {
        this.checkModel = checkModel

        totalCheckedDateList.clear()
        if (checkModel === CheckModel.SINGLE_DEFAULT_CHECKED) {
            totalCheckedDateList.add(initDate)
        }

    }

    fun getCheckMode(): CheckModel {
        return checkModel
    }


    fun getCurrPagerCheckedDateList(): List<LocalDate> {
        val nCalendarView = findViewWithTag(currentItem) as NCalendarView
        return nCalendarView.getCurrPageCheckedDateList()
    }

    fun getCurrPagerDateList(): List<LocalDate> {
        val nCalendarView = findViewWithTag(currentItem) as NCalendarView
        return nCalendarView.getCurrPagerDateList()
    }

    fun setCheckedDates(dateList: List<String>) {
        if (checkModel !== CheckModel.MULTIPLE) {
            throw RuntimeException(context.getString(R.string.N_set_checked_dates_illegal))
        }

        if (multipleCountModel != null && dateList.size > multipleCount) {
            throw RuntimeException(context.getString(R.string.N_set_checked_dates_count_illegal))
        }
        totalCheckedDateList.clear()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")

        try {
            for (i in dateList.indices) {
                totalCheckedDateList.add(LocalDate.parse(dateList[i],dateFormatter))
            }
        } catch (e: Exception) {
            throw IllegalArgumentException(context.getString(R.string.N_date_format_illegal))
        }
    }


    fun getTotalCheckedDateList(): List<LocalDate> {
        return totalCheckedDateList
    }

    fun setCalendarPainter(calendarPainter: CalendarPainter?) {
        this.calendarPainter = calendarPainter
    }

    fun setOnCalendarChangedListener(onCalendarChangedListener: OnCalendarChangedListener?) {
        this.onCalendarChangedListener = onCalendarChangedListener
    }

    fun setOnClickDisableDateListener(onClickDisableDateListener: OnClickDisableDateListener?) {
        this.onClickDisableDateListener = onClickDisableDateListener
    }

    fun setOnCalendarMultipleChangedListener(onCalendarMultipleChangedListener: OnCalendarMultipleChangedListener?) {
        this.onCalendarMultipleChangedListener = onCalendarMultipleChangedListener
    }

    fun getCalendarPainter(): CalendarPainter {
        if (calendarPainter == null) {
            calendarPainter = InnerPainter(context, parent as NCalendar)
        }
        return calendarPainter!!
    }

    fun getChildY(): Float {
        return (parent as NCalendar).getChildY()
    }

    fun getMonthStateY(): Int {
        return (parent as NCalendar).getMonthStateY()
    }

    fun getWeekStateY(): Int {
        return (parent as NCalendar).getWeekStateY()
    }

    fun toLastPager() {
        if (!NAttrs.horizontalScrollEnable) {
            Toast.makeText(context, resources.getString(R.string.N_horizontalScrollString), Toast.LENGTH_SHORT).show()
            return
        }
        dateChangeBehavior = DateChangeBehavior.API
        currentItem -= 1
    }

    fun toNextPager() {
        if (!NAttrs.horizontalScrollEnable) {
            Toast.makeText(context, resources.getString(R.string.N_horizontalScrollString), Toast.LENGTH_SHORT).show()
            return
        }
        dateChangeBehavior = DateChangeBehavior.API
        currentItem += 1
    }

    fun toToday() {
        jump(LocalDate.now(), true, DateChangeBehavior.API)
    }


    fun jumpDate(localDate: LocalDate,isCheck: Boolean) {
        jump(localDate, isCheck, DateChangeBehavior.API)
    }

    private fun jump(localDate: LocalDate, isCheck: Boolean, dateChangeBehavior: DateChangeBehavior) {

        if (!isAvailable(localDate)) {
            if (onClickDisableDateListener == null) {
                Toast.makeText(context, if (TextUtils.isEmpty(NAttrs.disabledString)) resources.getString(R.string.N_disabledString) else NAttrs.disabledString, Toast.LENGTH_SHORT).show()
            } else {
                onClickDisableDateListener?.onClickDisableDate(localDate)
            }
        } else {

            val nCalendarView = findViewWithTag(currentItem) as NCalendarView
            val currentViewInitDate = nCalendarView.pagerInitDate
            val indexOffset = if (calendarState == CalendarState.WEEK) {
                NDateUtil.getIntervalWeek(localDate, currentViewInitDate, NAttrs.firstDayOfWeek)
            } else {
                NDateUtil.getIntervalMonths(localDate, currentViewInitDate)
            }

            if (indexOffset != 0 && !NAttrs.horizontalScrollEnable) {
                Toast.makeText(context, resources.getString(R.string.N_horizontalScrollString), Toast.LENGTH_SHORT).show()
                return
            }

            this.fulcrumDate = localDate
            this.dateChangeBehavior = dateChangeBehavior


            if (isCheck) {
                if (checkModel === CheckModel.MULTIPLE) {
                    if (!totalCheckedDateList.contains(localDate)) {
                        if (totalCheckedDateList.size == multipleCount && multipleCountModel === MultipleCountModel.FULL_CLEAR) {
                            totalCheckedDateList.clear()
                        } else if (totalCheckedDateList.size == multipleCount && multipleCountModel === MultipleCountModel.FULL_REMOVE_FIRST) {
                            totalCheckedDateList.removeAt(0)
                        }
                        totalCheckedDateList.add(localDate)
                    } else {
                        totalCheckedDateList.remove(localDate)
                    }
                } else {
                    //单选
                    totalCheckedDateList.clear()
                    totalCheckedDateList.add(localDate)
                }
            }


            if (indexOffset == 0) {
                // notifyAllView()
                //只刷新当前页面即可，其他页面会在刷新adapter的时候自动回调
                nCalendarView.invalidate()
                dateChangedCallBack()
            } else {
                setCurrentItem(currentItem - indexOffset, Math.abs(indexOffset) == 1)
            }
        }
    }

    /**
     * 回调说明
     * 1、SINGLE_DEFAULT_CHECKED 每页单选时 每次一变化都回调了，避免多次回调 ，做个判断
     * 2、其他模式 由于选中是用户行为，周日历时没有选中的，但是变成月时，日期多了28或者35个，是可能存在选中的，故不做限制直接回调
     */
    private fun dateChangedCallBack() {
        when (checkModel) {
            CheckModel.SINGLE_DEFAULT_CHECKED -> {

                val localDate = totalCheckedDateList[0]
                if (lastCallbackDate != localDate) {
                    onCalendarChangedListener?.onCalendarChange(localDate.year, localDate.monthValue, localDate, dateChangeBehavior)
                    lastCallbackDate = localDate
                }
            }
            CheckModel.SINGLE_DEFAULT_UNCHECKED -> {
                val nCalendarView = findViewWithTag(currentItem) as NCalendarView
                val pagerInitDate = nCalendarView.pagerInitDate
                val currPageCheckedDateList = nCalendarView.getCurrPageCheckedDateList()
                val checkedDate = if (currPageCheckedDateList.isEmpty()) null else currPageCheckedDateList[0]

                onCalendarChangedListener?.onCalendarChange(pagerInitDate.year, pagerInitDate.monthValue, checkedDate, dateChangeBehavior)

            }
            CheckModel.MULTIPLE -> {
                val nCalendarView = findViewWithTag(currentItem) as NCalendarView
                val localDate = nCalendarView.pagerInitDate
                val currPageCheckedDateList = nCalendarView.getCurrPageCheckedDateList()
                onCalendarMultipleChangedListener?.onCalendarChange(localDate.year, localDate.monthValue, currPageCheckedDateList, totalCheckedDateList, dateChangeBehavior)
            }
        }

    }


}