package com.example.foodnote.ui.calorie_calculator_fragment.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class LinePercentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : View(context, attrs, style) {
    private var maxVale = 0
    private var value = 0
    private val paint: Paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLine(canvas)
    }


    fun setData(maxValue: Int, value: Int) {
        this.maxVale = maxValue
        this.value = value
        invalidate()
    }

    private fun drawLine(canvas: Canvas) {
        canvas.drawLine(0f, height / 2f, width / 2f, height / 2f, paint)
    }
}