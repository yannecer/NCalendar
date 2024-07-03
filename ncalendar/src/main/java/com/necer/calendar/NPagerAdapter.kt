package com.necer.calendar

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.necer.enumeration.CalendarState
import java.time.LocalDate

class NPagerAdapter(localDate: LocalDate, calendarState: CalendarState?, size: Int, currPage: Int) : PagerAdapter() {


    private var size: Int = Int.MAX_VALUE
    private var localDate: LocalDate
    private var currPage: Int


    private var calendarState: CalendarState? = CalendarState.MONTH

    init {
        this.localDate = localDate
        this.calendarState = calendarState
        this.size = size
        this.currPage = currPage
    }

    fun setCalendarState(localDate: LocalDate, calendarState: CalendarState, size: Int, currPage: Int) {
        this.localDate = localDate
        this.calendarState = calendarState
        this.size = size
        this.currPage = currPage
        notifyDataSetChanged()
    }


    override fun getCount(): Int {
        return size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val calendarView = if (calendarState == CalendarState.WEEK) {
            NCalendarView(container.context, container, localDate.plusWeeks((position - currPage).toLong()))
        } else {
            NCalendarView(container.context, container, localDate.plusMonths((position - currPage).toLong()))
        }
        calendarView.tag = position
        container.addView(calendarView)
        return calendarView
    }

    override fun getItemPosition(any: Any): Int {
        return POSITION_NONE
    }
}