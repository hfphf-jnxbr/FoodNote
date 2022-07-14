package com.example.foodnote.ui.noteBook.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Build.VERSION.SDK
import android.view.MotionEvent
import android.view.View
import com.example.foodnote.ui.noteBook.constNote.Const
import com.example.foodnote.ui.noteBook.constNote.Const.DEFAULT_ALPHA
import com.example.foodnote.ui.noteBook.constNote.Const.DEFAULT_SIZE_BRUSH
import com.example.foodnote.ui.noteBook.constNote.ConstType
import com.example.foodnote.ui.noteBook.interfaces.CanvasInterface
import com.google.android.material.card.MaterialCardView

@SuppressLint("ViewConstructor")
class CanvasPaint(context: Context, private val colorCardBackground: Int, private val picView: MaterialCardView) : View(context) , View.OnTouchListener , CanvasInterface {

    init {
        this.setOnTouchListener(this)
    }

    private var paintX = 0
    private var paintY = 0

    private var xTemp = 0
    private var yTemp = 0

    private var flag = true
    private var flagT = true
    private var flagPic = false

    private val paint = Paint().apply { isAntiAlias = true }

    private lateinit var bitmap : Bitmap
    private lateinit var myCanvas : Canvas

    private var color = Color.RED
    private var alpha = DEFAULT_ALPHA
    private var size = DEFAULT_SIZE_BRUSH

    private var brush : ConstType = ConstType.BRUSH_PEN

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

        when(brush) {

            ConstType.BRUSH_CIRCLES -> { BrushesDrawing.circleBrush(myCanvas, xCurrent, xTemp, yTemp, yCurrent, paint, size) }
            ConstType.BRUSH_PEN -> { BrushesDrawing.flatBrash(myCanvas, xCurrent, yCurrent,xTemp, yTemp, paint, size) }
            else -> {}
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

    private fun getColorPic(xCurrent : Int, yCurrent : Int) {
        if(Build.VERSION.SDK_INT >= 32) {

            val color = bitmap.getColor(xCurrent, yCurrent).toArgb()
            picView.setBackgroundColor(color)
            this.color = color
        }
    }

    override fun setColor(color: Int) { this.color = color }

    override fun setAlphaColor(alpha: Int) { this.alpha = alpha }

    override fun setColorBackground(color: Int) { this.color = color }

    override fun setSize(size: Float) { this.size = size.toInt() }

    override fun setBrush(brush : ConstType) { this.brush = brush }

    override fun setPic(flagPic: Boolean) { this.flagPic = flagPic }

    override fun clearCanvas() { myCanvas.drawColor(colorCardBackground) }

    override fun getBitmap() = bitmap

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                0 -> {
                    flagT = true

                    if(flagPic) { getColorPic(paintX,paintY) }
                }
                2 -> {
                    paintX = event.x.toInt()
                    paintY = event.y.toInt()

                    if(flagPic) {
                        getColorPic(paintX,paintY)
                    } else {
                        setPixel(paintX,paintY)
                    }
                }
                1 -> { flagPic = false }
            }
        }
        return true
    }
}