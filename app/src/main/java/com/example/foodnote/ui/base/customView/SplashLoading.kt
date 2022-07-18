package com.example.foodnote.ui.base.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class SplashLoading @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) {

    private val paintText = Paint().apply { color = Color.argb(255,150, 150, 150)
        strokeWidth = 5f
        isAntiAlias = true
        textSize = 90f
        typeface = Typeface.createFromAsset(context.assets, "fonts/aquire.otf")
    }

    private val paintText2 = Paint().apply { color = Color.argb(255,150, 150, 150)
        isAntiAlias = true
        textSize = 40f
    }

    private var lineX = 0f
    private var stringAnim = 0f
    private var stringLoadingAnim = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawText(canvas)
        drawLine(canvas)

        invalidate()
    }

    private fun drawText(canvas: Canvas) {

        if(stringAnim < 8) {
            stringAnim += 0.20f
        } else {
            if(stringLoadingAnim < 11) {
                stringLoadingAnim += 0.10f
            } else {
                stringLoadingAnim = 8f
            }
        }

        val string = "FoodNote"
        canvas.drawText(string,0,stringAnim.toInt(),0f,height/2f,paintText)

        val stringLoad = "Loading ..."
        val widthText = paintText2.measureText(stringLoad)

        val bounds = Rect()
        paintText2.getTextBounds(stringLoad, 0, stringLoad.length, bounds)
        val heightText: Int = bounds.height()

        canvas.drawText(stringLoad,0,stringLoadingAnim.toInt(),width - widthText,height/1.6f + heightText.toFloat(),paintText2)
    }

    private fun drawLine(canvas: Canvas) {

        if(lineX < width) {
            lineX += 17f
        }
        canvas.drawLine( 0f,height/1.8f,lineX,height/1.8f,paintText)
    }
}