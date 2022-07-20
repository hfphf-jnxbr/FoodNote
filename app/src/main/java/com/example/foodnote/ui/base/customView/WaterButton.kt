package com.example.foodnote.ui.base.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.foodnote.ui.base.customView.AnimatorX.ValueAnimatorX
import com.example.foodnote.ui.base.customView.customViewInterfaces.WaterButtonInterface
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

class WaterButton @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) ,
    WaterButtonInterface {

    private val paint = Paint().apply { color = Color.argb(250,84, 210, 235) }

    private val paintText = Paint().apply { color = Color.argb(255,255, 255, 255)
        isAntiAlias = true
        textSize = 60f
    }

    private var parameter = 0f
    private var tempY = 0f

    private var speed = 0.04f

    private var stopX = 3.2f
    private var startX = -0.1f

    private var amplitudeScale = 60f/263f

    private var maxWaterValue = 2100
    private var currentWaterValue = 1250

    private var marginText = 20f

    private lateinit var canvas: Canvas

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas

        animWater.render()

        drawText(canvas)
        invalidate()
    }



    private val animWater = ValueAnimatorX.ofValue(0f, 5000f).apply {
        vectorFunction { x -> if (x > x2) {currentX = x1}
            2f
        }
        render { parameter -> drawWater(parameter) }
    }
////////////////
    private fun drawWater(parameter : Float) {
        val maxWaterH = ((width * currentWaterValue) / maxWaterValue).toFloat()

        var x = startX
        while (x < stopX) {

            val value = waveFunction(x, parameter, maxWaterH,height*amplitudeScale )
            drawLineWater(value,canvas,x)
            x += stopX/height
        }
    }



    private fun drawText(canvas: Canvas) {
        val string = "${currentWaterValue}/${maxWaterValue} милл"
        val widthText = paintText.measureText(string)

        canvas.drawText(string, width/2f - widthText/2f,height/2f + marginText, paintText)
    }

    private fun drawLineWater(value: Float, canvas: Canvas, x: Float) {
        val y = round((x * height) / stopX)
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