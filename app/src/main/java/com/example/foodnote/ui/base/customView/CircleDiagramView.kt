package com.example.foodnote.ui.base.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CircleDiagramView @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) , DiagramViewInterface{

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)

        createRect(width)
        setMeasuredDimension(width, width)
    }

    private val paintCircle = Paint().apply { color = Color.rgb(10, 180, 255)
        isAntiAlias = true
    }

    private val paintCircle3 = Paint().apply { color = Color.rgb(150, 180, 255)
        isAntiAlias = true
    }

    private val paintCircle4 = Paint().apply { color = Color.rgb(255, 180, 255)
        isAntiAlias = true
    }

    private val paintCircleBek = Paint().apply { color = Color.rgb(245, 245, 245)
        isAntiAlias = true
    }

    private val paintCircleBek3 = Paint().apply { color = Color.rgb(245, 245, 245)
        isAntiAlias = true
    }

    private val paintCircleBek4 = Paint().apply { color = Color.rgb(255, 255, 255)
        setShadowLayer(8.0f, 1f, 5.0f, Color.GRAY)
        isAntiAlias = true
    }

    private val paintText = Paint().apply { color = Color.rgb(185, 185, 185)
        isAntiAlias = true
        textSize = 65f
    }

    private val paintSmallText = Paint().apply { color = Color.rgb(135, 135, 135)
        isAntiAlias = true
        textSize = 35f
    }

    private val paintLine = Paint().apply { color = Color.rgb(168, 168, 168)
        strokeWidth = 6f
        isAntiAlias = true
    }

    private var angle = 0f
    private var angle2 = 0f
    private var angle3 = 0f

    private var angleSpeedStart = 0.04f
    private var angleSpeedStart2 = 0.04f
    private var angleSpeedStart3 = 0.04f

    private var angleSpeed = 0f
    private var angleSpeed2 = 0f
    private var angleSpeed3 = 0f

    private var length = 0f
    private var length2 = 0f
    private var length3 = 0f

    private var maxCalories = 0
    private var maxFats = 0
    private var maxProtein = 0

    private var x1 = 0
    private var x2 = 0
    private var x3 = 0

    private var rectF : RectF = RectF(0f,0f,0f,0f)
    private var rectF2 : RectF = RectF(0f,0f,0f,0f)
    private var rectF3 : RectF = RectF(0f,0f,0f,0f)

    private var flag = true

    private var widthDiagram = 12f
    private var radiuse = 120f

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        angleSpeed()
        drawCircles(canvas)
        drawText(canvas)

        invalidate()
    }

    private fun drawCircles(canvas: Canvas){
        canvas.drawCircle(width/2f,width/2f,width/2f - radiuse ,paintCircleBek4)

        canvas.drawArc(rectF,0f,angle,true,paintCircle)
        line(angle,canvas,"$x1 ккал")
        canvas.drawCircle(width/2f,width/2f,width/2f - widthDiagram - radiuse,paintCircleBek)

        canvas.drawArc(rectF2,0f,angle2,true,paintCircle3)
        line(angle2,canvas,"$x2 жиры")
        canvas.drawCircle(width/2f,width/2f,width/2f - widthDiagram*2 - radiuse,paintCircleBek)

        canvas.drawArc(rectF3,0f,angle3,true,paintCircle4)
        line(angle3,canvas,"$x3 белок")
        canvas.drawCircle(width/2f,width/2f,width/2f - widthDiagram*3 - radiuse,paintCircleBek3)
    }

    private fun line(angle : Float, canvas : Canvas, text : String) {
        if(angle > 0) {
            val x = width/2f + (width/2f - radiuse/2 - 32) * cos(- (angle * 2 * PI) / 360).toFloat()
            val y = width/2f - (width/2f - radiuse/2 - 32) * sin(- (angle * 2 * PI) / 360).toFloat()

            val x2 = width/2f + (width/2f - radiuse/2 ) * cos(- (angle * 2 * PI) / 360).toFloat()
            val y2 = width/2f - (width/2f - radiuse/2 ) * sin(- (angle * 2 * PI) / 360).toFloat()

            canvas.drawLine(width/2f,width/2f, x, y, paintLine)
            canvas.drawCircle(x, y,10f,paintLine)

            if(angle <= 180) {
                canvas.drawText(text,x2 - 90f,y2 + 32f,paintSmallText)
            } else {
                canvas.drawText(text,x2 - 90f,y2 - 10f ,paintSmallText)
            }
        }
    }

    private fun angleSpeed() {
        if(angle < length) angle += angleSpeed
        if(angle2 < length2) angle2 += angleSpeed2
        if(angle3 < length3) angle3 += angleSpeed3

        angleSpeed = angleSpeedStart * ( length - angle)
        angleSpeed2 = angleSpeedStart2 * ( length2 - angle2)
        angleSpeed3 = angleSpeedStart3 * ( length3 - angle3)
    }

    private fun createRect(width: Int) {
        rectF = RectF(0f + radiuse,0f + radiuse, width.toFloat() - radiuse, width.toFloat() - radiuse)
        rectF2 = RectF(widthDiagram + radiuse,widthDiagram + radiuse,width.toFloat() - widthDiagram - radiuse,width.toFloat() - widthDiagram - radiuse)
        rectF3 = RectF(widthDiagram*2 + radiuse,widthDiagram*2 + radiuse,width.toFloat() - widthDiagram*2 - radiuse,width.toFloat() - widthDiagram*2 - radiuse)

        this.radiuse = width / 6f
    }

    private fun drawText(canvas: Canvas) {
        val string = "max ${maxCalories}ккал"
        val widthText = paintText.measureText(string)

        paintText.textSize = ((width/2f) - radiuse) / 5f

        canvas.drawText(string,width/2f - widthText/2 ,width/2f ,paintText)
    }

    override fun start(x1 : Int, maxCalories : Int,x2 : Int, maxFats :Int , x3 : Int, maxProtein : Int) {
        angle = 0f
        angle2 = 0f
        angle3 = 0f

        flag = true

        this.maxCalories = maxCalories
        this.maxFats = maxFats
        this.maxProtein = maxProtein

        this.x1 = x1
        this.x2 = x2
        this.x3 = x3

        length = (360f * ((x1 * 100) / maxCalories)) / 100f
        length2 = (360f * ((x2 * 100) / maxFats)) / 100f
        length3 = (360f * ((x3 * 100) / maxProtein)) / 100f
    }

}