package com.example.foodnote.ui.notes_fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import com.example.foodnote.R
import com.example.foodnote.databinding.CardNotesBinding
import com.example.foodnote.databinding.NotebookFragmentBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.base.helperView.MovedView

class NotesFragment : BaseViewBindingFragment<NotebookFragmentBinding>(NotebookFragmentBinding::inflate) , NoteBookFragmentInterface {
    private lateinit var movedView: MovedView
    private var widthScreen = 0

    private val listCardNotes : ArrayList<View> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movedView = MovedView(ArrayList(),binding.root)
        setWidthPixels()
        setFragmentConstructor()
        checkChip()
    }

    private fun setWidthPixels() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        widthScreen = metrics.widthPixels
    }

    private fun checkChip() {
        binding.chipStandardNote.setOnClickListener {
            constructorDrop()
        }
    }

    private fun constructorDrop(){
        ObjectAnimator.ofFloat(binding.containerConstructor, View.X,0f).apply {
            duration = 1500
            interpolator = AnticipateOvershootInterpolator(1f)
            start()
        }
        movedView.blockMove(false)
    }

    override fun constructorFragmentClose() {
        ObjectAnimator.ofFloat(binding.containerConstructor, View.X, widthScreen.toFloat()).apply {
            duration = 1500
            interpolator = AnticipateOvershootInterpolator(1f)
            start()
        }
        movedView.blockMove(true)
    }

    override fun setDataCreteNote(widthCard: Int, heightCard: Int, colorCard: Int, note : String) {

        if(listCardNotes.size < 10) {

            val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
            val cardNoteView = cardNoteViewBind.root

            cardNoteView.updateLayoutParams {
                height = (heightCard * widthScreen) / 100
                width = (widthCard * widthScreen) / 100
            }

            cardNoteView.elevation = 17f
            cardNoteView.setCardBackgroundColor(colorCard)
            cardNoteViewBind.textNote.text = note

            cardNoteViewBind.buttonDelete.setOnClickListener {
                binding.root.removeView(cardNoteView)
                movedView.removeView(cardNoteView)
                listCardNotes.remove(cardNoteView)
            }

            listCardNotes.add(cardNoteView)
            binding.root.addView(cardNoteView)
            movedView.addView(cardNoteView)
        } else {

            Toast.makeText(requireContext(),getString(R.string.max_notes_massenge),Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFragmentConstructor() {
        val fragment = ConstructorFragment.newInstance()
        fragment.setFragment(this)

        binding.containerConstructor.x = widthScreen.toFloat()

        childFragmentManager
            .beginTransaction()
            .replace(R.id.containerConstructor,fragment)
            .commit()
    }

}