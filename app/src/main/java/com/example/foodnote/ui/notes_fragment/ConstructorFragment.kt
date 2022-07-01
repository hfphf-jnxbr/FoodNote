package com.example.foodnote.ui.notes_fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.foodnote.R
import com.example.foodnote.databinding.ConstructorNoteBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.sample
import java.util.concurrent.Flow

class ConstructorFragment : BaseViewBindingFragment<ConstructorNoteBinding>(ConstructorNoteBinding::inflate) {

    private lateinit var fragment: NoteBookFragmentInterface
    private var colorCard = Color.WHITE

    private val scope = CoroutineScope(Dispatchers.IO)
    private var flag = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chekButton()
        chekColor()
    }

    private fun chekButton() = with(binding) {
        buttonCreate.setOnClickListener {
            if(flag){
                createNote()
                flag = false

                scope.launch {
                    delay(1550)
                    flag = true
                }
            }
        }
    }

    private fun createNote() = with(binding) {
        val inputW = editWidth.text.toString()
        val inputH = editHeight.text.toString()

        val string = editNote.text.toString()

        if (inputW.isNotEmpty() && inputH.isNotEmpty()) {
            if((inputH.toInt() in 30..100) && (inputW.toInt() in 30..100)) {

                fragment.setDataCreteNote(inputW.toInt(), inputH.toInt(), colorCard, string)
                fragment.constructorFragmentClose()
            } else {
                Toast.makeText(requireContext(),getString(R.string.range_error),Toast.LENGTH_SHORT).show()
                if(inputH.toInt() !in 30..100) editHeight.error = getString(R.string.range_error)
                if(inputW.toInt() !in 30..100) editWidth.error = getString(R.string.range_error)
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

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}