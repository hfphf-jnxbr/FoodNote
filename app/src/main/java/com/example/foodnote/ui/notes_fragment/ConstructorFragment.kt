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
import com.example.foodnote.ui.notes_fragment.constNote.Const.STROKE_WIDTH
import com.example.foodnote.ui.notes_fragment.constNote.Const.STROKE_WIDTH_FOCUS
import com.example.foodnote.ui.notes_fragment.constNote.ConstType
import com.example.foodnote.ui.notes_fragment.editorNote.EditorPaintNoteFragment
import com.example.foodnote.ui.notes_fragment.editorNote.EditorStandardNoteFragment
import com.example.foodnote.ui.notes_fragment.interfaces.ConstructorFragmentInterface
import com.example.foodnote.ui.notes_fragment.interfaces.NoteBookFragmentInterface
import kotlinx.coroutines.*

class ConstructorFragment : BaseViewBindingFragment<ConstructorNoteBinding>(ConstructorNoteBinding::inflate) , ConstructorFragmentInterface{

    private lateinit var fragment: NoteBookFragmentInterface

    private lateinit var editorStandardNoteFragmentEditor: EditorStandardNoteFragment
    private lateinit var editorPaintNoteFragmentEditor: EditorPaintNoteFragment
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
            else -> {}
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

    private fun editTextFilter() {
        val filterArray = Array<InputFilter>(1) { InputFilter.LengthFilter(2) }
        binding.editWidth.filters = filterArray
        binding.editHeight.filters = filterArray
    }

    private fun chekButton() = with(binding) {
        buttonCreate.setOnClickListener {
            if(flag){
                createHeightWidth()
                flag = false

                timeOutButton()
            }
        }

        back.setOnClickListener {
            if(flag){
                fragment.constructorFragmentClose()
                flag = false

                timeOutButton()
            }
        }
    }

    private fun timeOutButton(){
        scope.launch {
            delay(DELAY_BUTTON)

            flag = true
            fragment.setFlagBlockChip(true)
        }
    }

    private fun createHeightWidth() = with(binding) {
        val inputW = editWidth.text.toString()
        val inputH = editHeight.text.toString()

        if (inputW.isNotEmpty() && inputH.isNotEmpty()) {
            val inH = inputH.toInt()
            val inW = inputW.toInt()

            if((inH in MIN_NOTE_SIZE..MAX_NOTE_SIZE) && (inW in MIN_NOTE_SIZE..MAX_NOTE_SIZE)) {

                createNote(inH,inW)
                fragment.constructorFragmentClose()
            } else {
                if(inH !in MIN_NOTE_SIZE..MAX_NOTE_SIZE) editHeight.error = getString(R.string.range_error)
                if(inW !in MIN_NOTE_SIZE..MAX_NOTE_SIZE) editWidth.error = getString(R.string.range_error)
            }
        } else {
            if(inputW.isEmpty()) editWidth.error = getString(R.string.empty_field_error_messange)
            if(inputH.isEmpty()) editHeight.error = getString(R.string.empty_field_error_messange)

            Toast.makeText(requireContext(),getString(R.string.empty_field_error_messange),Toast.LENGTH_SHORT).show()
        }
    }

    override fun getHeight() : Int = with(binding){
        val inputH = editHeight.text.toString()

        if (inputH.isNotEmpty()) {
            val inH = inputH.toInt()

            if(inH in MIN_NOTE_SIZE..MAX_NOTE_SIZE) {
                return inH
            } else {
                if(inH !in MIN_NOTE_SIZE..MAX_NOTE_SIZE) editHeight.error = getString(R.string.range_error)
            }
        } else {
            if(inputH.isEmpty()) editHeight.error = getString(R.string.empty_field_error_messange)
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
                if(inW !in MIN_NOTE_SIZE..MAX_NOTE_SIZE) editWidth.error = getString(R.string.range_error)
            }
        } else {
            if(inputW.isEmpty()) editWidth.error = getString(R.string.empty_field_error_messange)
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
                fragment.setDataCreateStandardNote(width, height, colorCard, string)
            }
            ConstType.PAINT_TYPE  ->    {
                val bitmapURL = editorPaintNoteFragmentEditor.getImageURL()
                fragment.setDataCreatePaintNote(width, height, colorCard, bitmapURL)
            }
            else -> {}
        }
    }

    companion object {
        fun newInstance(fragmentNotes: NotesFragment, typeNotes: ConstType) : ConstructorFragment{
            val fragment = ConstructorFragment()
            fragment.fragment = fragmentNotes
            fragment.typeNote = typeNotes
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}