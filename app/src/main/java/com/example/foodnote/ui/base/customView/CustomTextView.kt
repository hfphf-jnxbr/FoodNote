package com.example.foodnote.ui.base.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.foodnote.ui.base.customView.AnimatorX.ValueAnimatorX

class CustomTextView @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) {

    private val paintText = Paint().apply { color = Color.argb(255,155, 155, 155)
        isAntiAlias = true
        textSize = 60f
        typeface = Typeface.createFromAsset(context.assets, "fonts/lcd.ttf")
    }
    private var string = "Circles"

    private lateinit var canvas : Canvas
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas

        animText.render()

        invalidate()
    }

    private val animText = ValueAnimatorX.ofValue(0f, string.length.toFloat()).apply {
        vectorFunction { 8f }
        render { value -> drawText(value) }
    }

    private fun drawText(value: Float) {
        val widthText = paintText.measureText(string)

        val bounds = Rect()
        paintText.getTextBounds(string, 0, string.length, bounds)
        val heightText: Int = bounds.height()

        canvas.drawText(string,0,value.toInt(),width/2f - widthText/2f,height - heightText/2f,paintText)
    }

    fun setText(text : String) {
        animText.currentX = 0f
        animText.x2 = text.length.toFloat()
        this.string = text
    }
}