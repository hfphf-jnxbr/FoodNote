package com.example.foodnote.ui.notes_fragment.interfaces

interface NoteBookFragmentInterface {
    fun constructorFragmentClose()
    fun setDataCreateStandardNote(widthCard: Int, heightCard: Int, colorCard: Int, note : String)
    fun setDataCreatePaintNote(widthCard: Int, heightCard: Int, colorCard: Int, fileName: String)
    fun setFlagBlockChip(boolean: Boolean)
}