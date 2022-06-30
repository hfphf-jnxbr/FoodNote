package com.example.foodnote.ui.notes_fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.foodnote.R
import com.example.foodnote.databinding.ConstructorNoteBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment

class ConstructorFragment : BaseViewBindingFragment<ConstructorNoteBinding>(ConstructorNoteBinding::inflate) {

    private lateinit var fragment: NoteBookFragmentInterface
    private var colorCard = Color.WHITE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chekButton()
        chekColor()
    }

    private fun chekButton() = with(binding) {
        buttonCreate.setOnClickListener {
            val inputW = editWidth.text.toString()
            val inputH = editHeight.text.toString()

            val string = editNote.text.toString()

            if (inputW.isNotEmpty() && inputH.isNotEmpty()) {
                try {
                    val w = inputW.toInt()
                    val h = inputH.toInt()

                    if((w in 30..100) && (h in 30..100)) {

                        fragment.setDataCreteNote(inputW.toInt(), inputH.toInt(), colorCard, string)
                        fragment.constructorFragmentClose()
                    } else {
                        Toast.makeText(requireContext(),getString(R.string.range_error),Toast.LENGTH_SHORT).show()

                        editWidth.error = getString(R.string.range_error)
                        editHeight.error = getString(R.string.range_error)
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(),getString(R.string.nuber_format_error_message),Toast.LENGTH_SHORT).show()

                    editWidth.error = getString(R.string.nuber_format_error_message)
                    editHeight.error = getString(R.string.nuber_format_error_message)
                }
            } else {
                Toast.makeText(requireContext(),getString(R.string.empty_field_error_messange),Toast.LENGTH_SHORT).show()

                editWidth.error = getString(R.string.empty_field_error_messange)
                editHeight.error = getString(R.string.empty_field_error_messange)
            }
        }
    }

    private fun chekColor() = with(binding){
        val list = listOf(colorBlue,colorPink,colorGreen,colorYellow,colorGray,colorWhite)

        list.forEach { view ->
            view.setOnClickListener {

                list.forEach { e ->
                    e.strokeColor = Color.LTGRAY
                    e.strokeWidth = 3
                }

                view.strokeColor = Color.GRAY
                view.strokeWidth = 6
                colorCard = view.backgroundTintList?.defaultColor ?: Color.WHITE
            }
        }

    }

    companion object {
        fun newInstance() = ConstructorFragment()
    }

    fun setFragment(fragment: NotesFragment) {
        this.fragment = fragment
    }

}