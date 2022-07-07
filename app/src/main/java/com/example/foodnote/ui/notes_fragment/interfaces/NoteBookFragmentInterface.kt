package com.example.foodnote.ui.notes_fragment.interfaces

import android.view.View

interface NoteBookFragmentInterface {

    fun constructorFragmentClose()

    fun saveToDataAndCreatePaintNote(widthCard: Int, heightCard: Int, colorCard: Int, fileName: String, posX : Int, posY : Int, id : Int, elevation: Float)

    fun saveToDataAndCreateStandardNote(widthCard: Int, heightCard: Int, colorCard: Int, note: String, posX : Int, posY : Int, id : Int, elevation: Float)

    fun setFlagBlockChip(boolean: Boolean)

    fun setNewCardCoordinatesData(viewX : Int, viewY : Int, view : View)

    fun setElevationView(view: View)
}