package com.example.foodnote.ui.base.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.tan

class WaterButton @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) , WaterButtonInterface {

    private val paint = Paint().apply { color = Color.argb(250,84, 210, 235) }
    private val paintText = Paint().apply { color = Color.argb(255,255, 255, 255)
        isAntiAlias = true
        textSize = 60f
    }
    private var a = 0f
    private var tempY = 0f

    private var maxWaterValue = 2100
    private var currentWaterValue = 1250

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawWater(canvas)
        drawText(canvas)
        invalidate()
    }

    private fun drawWater(canvas: Canvas) {
        val maxWaterH = ((width * currentWaterValue) / maxWaterValue).toFloat()

        var x = -0.1f
        while (x < 3.2f) {

            val value = waveFunction(x,a,maxWaterH,(( height*60f)/263f ))
            drawLineWater(value,canvas,x)
            x += 3.2f/height
        }
        a += 0.04f
    }

    private fun drawText(canvas: Canvas) {
        val string = "${currentWaterValue}/${maxWaterValue} милл"
        val widthText = paintText.measureText(string)

        canvas.drawText(string, width/2f - widthText/2f,height/2f + 20f,paintText)
    }

    private fun drawLineWater(value: Float, canvas: Canvas, x: Float) {
        val y = round((x * height) / 3.2f)
        if(y != tempY) {
            canvas.drawLine(0f, y, value, y, paint)
        }
        tempY = y
    }

    private fun waveFunction(x: Float, a: Float, h: Float, amplitude : Float) = (0.5f * (x - 2f) * cos(x + a) * sin(x + a)) * amplitude + h

    override fun setDataWaterToStart(maxWater : Int, currentWater : Int) {
        this.maxWaterValue = maxWater
        this.currentWaterValue = currentWater
    }
}