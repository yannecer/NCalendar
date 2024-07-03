package com.necer.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.necer.utils.NAttrs

/**
 * Created by necer on 2018/12/24.
 */
class NWeekBar(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private var days = arrayOf("日", "一", "二", "三", "四", "五", "六")

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(NAttrs.weekBarBackgroundColor)

        paint.textAlign = Paint.Align.CENTER
        paint.color = NAttrs.weekBarTextColor
        paint.textSize = NAttrs.weekBarTextSize
        for (i in days.indices) {
            val rect = Rect(i * width / days.size, 0, (i + 1) * width / days.size, height)
            val fontMetrics = paint.fontMetrics
            val top = fontMetrics.top
            val bottom = fontMetrics.bottom
            val baseLineY = (rect.centerY() - top / 2 - bottom / 2).toInt()
            val day = if (NAttrs.firstDayOfWeek == NAttrs.MONDAY) {
                val j = i + 1
                days[if (j > days.size - 1) 0 else j]
            } else {
                days[i]
            }
            canvas.drawText(day, rect.centerX().toFloat(), baseLineY.toFloat(), paint)
        }
    }
}