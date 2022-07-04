package com.example.foodnote.ui.notes_fragment.interfaces

import android.graphics.Bitmap

interface CanvasInterface {
    fun setColor(color: Int)
    fun setSize(size: Float)
    fun clearCanvas()
    fun getBitmap() : Bitmap
}