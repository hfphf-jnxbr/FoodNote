package com.example.foodnote.ui.notes_fragment

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Toast
import com.example.foodnote.R
import com.example.foodnote.databinding.ConstructorNoteBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.notes_fragment.constNote.Const.DELAY_BUTTON
import com.example.foodnote.ui.notes_fragment.constNote.Const.MAX_NOTE_SIZE
import com.example.foodnote.ui.notes_fragment.constNote.Const.MIN_NOTE_SIZE
import com.example.foodnote.ui.notes_fragment.constNote.Const.NOTES_ELEVATION
import com.example.foodnote.ui.notes_fragment.constNote.Const.RANDOM_ID
import com.example.foodnote.ui.notes_fragment.constNote.Const.STROKE_WIDTH
import com.example.foodnote.ui.notes_fragment.constNote.Const.STROKE_WIDTH_FOCUS
import com.example.foodnote.ui.notes_fragment.constNote.ConstType
import com.example.foodnote.ui.notes_fragment.editorNote.EditorFoodsNoteFragment
import com.example.foodnote.ui.notes_fragment.editorNote.EditorPaintNoteFragment
import com.example.foodnote.ui.notes_fragment.editorNote.EditorStandardNoteFragment
import com.example.foodnote.ui.notes_fragment.interfaces.ConstructorFragmentInterface
import com.example.foodnote.ui.notes_fragment.interfaces.NoteBookFragmentInterface
import kotlinx.coroutines.*
import java.util.*

class ConstructorFragment : BaseViewBindingFragment<ConstructorNoteBinding>(ConstructorNoteBinding::inflate) , ConstructorFragmentInterface{

    private lateinit var fragmentNoteBook: NoteBookFragmentInterface

    private lateinit var editorStandardNoteFragmentEditor: EditorStandardNoteFragment
    private lateinit var editorPaintNoteFragmentEditor: EditorPaintNoteFragment
    private lateinit var editorFoodNoteFragmentEditor: EditorFoodsNoteFragment
    private lateinit var typeNote : ConstType

    private var colorCard = Color.WHITE
    private var flag = true

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEditorType()

        chekButton()
        chekColor()
        editTextFilter()
    }
    private fun setEditorType() {
        when (typeNote) {
            ConstType.STANDARD_TYPE -> { setNoteStandardEditor( EditorStandardNoteFragment() ) }
            ConstType.PAINT_TYPE ->    { setNotePaintEditor( EditorPaintNoteFragment.newInstance(this)) }
            ConstType.FOOD_TYPE ->     { setNoteFoodEditor( EditorFoodsNoteFragment()) }
        }
    }

    private fun setNoteStandardEditor(fragment: EditorStandardNoteFragment) {
        editorStandardNoteFragmentEditor = fragment
        childFragmentManager.beginTransaction().replace(R.id.containerEditNotes,fragment).commitNow()
    }

    private fun setNotePaintEditor(fragment: EditorPaintNoteFragment) {
        editorPaintNoteFragmentEditor = fragment
        childFragmentManager.beginTransaction().replace(R.id.containerEditNotes,fragment).commitNow()
    }

    private fun setNoteFoodEditor(fragment: EditorFoodsNoteFragment) {
        editorFoodNoteFragmentEditor = fragment
        childFragmentManager.beginTransaction().replace(R.id.containerEditNotes,fragment).commitNow()
    }

    private fun editTextFilter() {
        val filterArray = Array<InputFilter>(1) { InputFilter.LengthFilter(2) }
        binding.editWidth.filters = filterArray
        binding.editHeight.filters = filterArray
    }

    private fun chekButton() = with(binding) {
        buttonCreate.setOnClickListener {
            if(flag){
                createNote()
                flag = false
                timeOutButton()
            }
        }

        back.setOnClickListener {
            if(flag){
                fragmentNoteBook.constructorFragmentClose()
                flag = false
                timeOutButton()
            }
        }
    }

    private fun timeOutButton(){
        scope.launch {
            delay(DELAY_BUTTON)

            flag = true
            fragmentNoteBook.setFlagBlockChip(true)
        }
    }

    private fun createNote() {
        if(getHeight() > -1 && getWidth() > -1) {
            createNote(getHeight(), getWidth())
            fragmentNoteBook.constructorFragmentClose()
        }
    }

    override fun getHeight() : Int = with(binding){
        val inputH = editHeight.text.toString()

        if (inputH.isNotEmpty()) {
            val inH = inputH.toInt()

            if(inH in MIN_NOTE_SIZE..MAX_NOTE_SIZE) {
                return inH
            } else {
                editHeight.error = getString(R.string.range_error)
            }
        } else {
            editHeight.error = getString(R.string.empty_field_error_messange)
            Toast.makeText(requireContext(),getString(R.string.empty_field_error_messange),Toast.LENGTH_SHORT).show()
        }
        return -1
    }

    override fun getWidth() : Int = with(binding){
        val inputW = editWidth.text.toString()

        if (inputW.isNotEmpty()) {
            val inW = inputW.toInt()

            if(inW in MIN_NOTE_SIZE..MAX_NOTE_SIZE) {
                return inW
            } else {
                editWidth.error = getString(R.string.range_error)
            }
        } else {
            editWidth.error = getString(R.string.empty_field_error_messange)
            Toast.makeText(requireContext(),getString(R.string.empty_field_error_messange),Toast.LENGTH_SHORT).show()
        }
        return -1
    }

    private fun chekColor() = with(binding){
        val list = listOf(colorBlue,colorPink,colorGreen,colorYellow,colorGray,colorWhite)

        list.forEach { view ->
            view.setOnClickListener {

                list.forEach { e ->
                    e.strokeColor = Color.LTGRAY
                    e.strokeWidth = STROKE_WIDTH
                }

                view.strokeColor = Color.GRAY
                view.strokeWidth = STROKE_WIDTH_FOCUS
                colorCard = view.backgroundTintList?.defaultColor ?: Color.WHITE
            }
        }
    }

    override fun getColorBackgroundCard() = colorCard

    private fun createNote(height : Int, width : Int) {
        when (typeNote) {
            ConstType.STANDARD_TYPE -> {
                val string = editorStandardNoteFragmentEditor.getNoteText()
                val randomId = Random().nextInt(RANDOM_ID)
                fragmentNoteBook.saveAndCreateDataNotesStandard(width, height, colorCard, string,0,0, randomId, NOTES_ELEVATION)
            }
            ConstType.PAINT_TYPE  ->    {
                val bitmapURL = editorPaintNoteFragmentEditor.getImageURL()
                val randomId = Random().nextInt(RANDOM_ID)
                fragmentNoteBook.saveAndCreateDataNotesPaint(width, height, colorCard, bitmapURL,0 ,0, randomId, NOTES_ELEVATION)
            }
            ConstType.FOOD_TYPE  ->    {
                val string = editorFoodNoteFragmentEditor.getListFoodsText()
                val randomId = Random().nextInt(RANDOM_ID)
                fragmentNoteBook.saveAndCreateDataNotesStandard(width, height, colorCard, string,0 ,0, randomId, NOTES_ELEVATION)
            }
        }
    }

    companion object {
        fun newInstance(fragmentNotes: NotesFragment, typeNotes: ConstType) : ConstructorFragment{
            val fragment = ConstructorFragment()
            fragment.fragmentNoteBook = fragmentNotes
            fragment.typeNote = typeNotes
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}