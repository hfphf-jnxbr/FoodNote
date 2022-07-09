package com.example.foodnote.ui.noteBook.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.example.foodnote.ui.noteBook.constNote.Const
import com.example.foodnote.ui.noteBook.interfaces.CanvasInterface

@SuppressLint("ViewConstructor")
class CanvasPaint(context: Context, private val colorCardBackground: Int) : View(context) , View.OnTouchListener , CanvasInterface {

    init {
        this.setOnTouchListener(this)
    }

    private var paintX = 0
    private var paintY = 0

    private var xTemp = 0
    private var yTemp = 0

    private var flag = true
    private var flagT = true

    private val paint = Paint().apply { isAntiAlias = true }

    private lateinit var bitmap : Bitmap
    private lateinit var myCanvas : Canvas
    private var color = Color.WHITE
    private var alpha = 200
    private var size = Const.DEFAULT_SIZE_BRUSH

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        createBitmap()
        canvas.drawBitmap(bitmap, null, Rect(0, 0, width, height), paint)

        invalidate()
    }

    private fun setPixel(xCurrent : Int, yCurrent : Int) {
        if(flagT) {
            xTemp = xCurrent
            yTemp = yCurrent

            flagT = false
        }

        paint.color = color
        paint.alpha = alpha
        paint.strokeWidth = 2f

        for (i in -size..size) {
            myCanvas.drawLine(xTemp.toFloat() + i, yTemp.toFloat() + i, xCurrent.toFloat() + i, yCurrent.toFloat() + i, paint)
        }

        paint.color = Color.argb(255,0,0,0)

        xTemp = xCurrent
        yTemp = yCurrent
    }

    private fun createBitmap() {
        if(flag) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            myCanvas = Canvas(bitmap)
            myCanvas.drawColor(colorCardBackground)

            flag = false
        }
    }

    override fun setColor(color: Int) { this.color = color }

    override fun setAlphaColor(alpha: Int) { this.alpha = alpha }

    override fun setColorBackground(color: Int) { this.color = color }

    override fun setSize(size: Float) { this.size = size.toInt() }

    override fun clearCanvas() { myCanvas.drawColor(colorCardBackground) }

    override fun getBitmap() = bitmap

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                0 -> { flagT = true }
                2 -> {
                    paintX = event.x.toInt()
                    paintY = event.y.toInt()

                    setPixel(paintX,paintY)
                }
                1 -> { }
            }
        }
        return true
    }
}