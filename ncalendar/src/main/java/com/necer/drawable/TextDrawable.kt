package com.necer.drawable

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import com.necer.utils.NAttrs

/**
 *
 * @author necer
 * @date 2020/3/27
 */
class TextDrawable() : Drawable() {
    private val textPaint: Paint = Paint()
    private var text: String? = null

    init {
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = NAttrs.numberBackgroundTextSize
        textPaint.color = NAttrs.numberBackgroundTextColor
        textPaint.alpha = NAttrs.backgroundAlphaColor
    }

    fun setText(text: String?) {
        this.text = text
    }

    override fun draw(canvas: Canvas) {
        val rect = bounds
       canvas.drawText(text!!, rect.centerX().toFloat(), getTextBaseLineY(rect.centerY().toFloat()), textPaint)
    }

    override fun setAlpha(alpha: Int) {
        textPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        textPaint.colorFilter = colorFilter
    }

    @SuppressLint("WrongConstant")
    override fun getOpacity(): Int {
        return 0
    }

    private fun getTextBaseLineY(centerY: Float): Float {
        val fontMetrics = textPaint.fontMetrics
        return centerY - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top
    }
}