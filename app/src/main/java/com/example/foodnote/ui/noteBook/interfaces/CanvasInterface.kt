package com.example.foodnote.ui.noteBook.interfaces

import android.graphics.Bitmap

interface CanvasInterface {
    fun setColor(color: Int)
    fun setAlphaColor(alpha: Int)
    fun setColorBackground(color: Int)
    fun setSize(size: Float)
    fun clearCanvas()
    fun getBitmap() : Bitmap
}