package com.example.foodnote.ui.noteBook.interfaces

import android.view.View

interface NoteBookFragmentInterface {

    fun constructorFragmentClose()

    fun saveAndCreateDataNotesPaint(
        widthCard: Int,
        heightCard: Int,
        colorCard: Int,
        fileName: String,
        posX: Int,
        posY: Int,
        id: Int,
        elevation: Float
    )

    fun saveAndCreateDataNotesStandard(
        widthCard: Int,
        heightCard: Int,
        colorCard: Int,
        note: String,
        posX: Int,
        posY: Int,
        id: Int,
        elevation: Float
    )

    fun setNewCardCoordinatesData(viewX: Int, viewY: Int, view: View)

    fun setElevationView(view: View)

    fun saveAndCreateDataNotesFoods(
        widthCard: Int,
        heightCard: Int,
        colorCard: Int,
        listFoods: String,
        listWeight: String,
        general: String,
        posX: Int,
        posY: Int,
        id: Int,
        elevation: Float
    )
}