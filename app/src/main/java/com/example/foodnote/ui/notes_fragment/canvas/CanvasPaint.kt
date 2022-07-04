package com.example.foodnote.ui.notes_fragment.canvas

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.example.foodnote.ui.notes_fragment.constNote.Const
import com.example.foodnote.ui.notes_fragment.interfaces.CanvasInterface

class CanvasPaint(context: Context) : View(context) , View.OnTouchListener , CanvasInterface {

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

    private lateinit var bitmap1 : Bitmap
    private lateinit var myCanvas : Canvas
    private var color = Color.WHITE
    private var size = Const.DEFAULT_SIZE_BRUSH

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        createBitmap()
        canvas.drawBitmap(bitmap1, null, Rect(0, 0, width, height), paint)

        invalidate()
    }

    private fun setPixel(xCurrent : Int, yCurrent : Int) {
        if(flagT) {
            xTemp = xCurrent
            yTemp = yCurrent

            flagT = false
        }

        paint.color = color
        paint.strokeWidth = size

        myCanvas.drawLine(xTemp.toFloat(), yTemp.toFloat(), xCurrent.toFloat(), yCurrent.toFloat(), paint)

        xTemp = xCurrent
        yTemp = yCurrent
    }

    private fun createBitmap() {
        if(flag) {
            bitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            myCanvas = Canvas(bitmap1)
            myCanvas.drawColor(Color.WHITE)

            flag = false
        }
    }

    override fun setColor(color: Int) { this.color = color }

    override fun setSize(size: Float) { this.size = size }

    override fun clearCanvas() { myCanvas.drawColor(Color.WHITE) }

    override fun getBitmap() = bitmap1

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