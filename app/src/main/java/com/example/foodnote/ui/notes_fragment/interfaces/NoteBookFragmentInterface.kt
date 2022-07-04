package com.example.foodnote.ui.notes_fragment.interfaces

import android.graphics.Bitmap

interface NoteBookFragmentInterface {
    fun constructorFragmentClose()
    fun setDataCreateStandardNote(widthCard: Int, heightCard: Int, colorCard: Int, note : String)
    fun setDataCreatePaintNote(widthCard: Int, heightCard: Int, colorCard: Int, bitmap : Bitmap)
    fun setFlagBlockChip(boolean: Boolean)
}