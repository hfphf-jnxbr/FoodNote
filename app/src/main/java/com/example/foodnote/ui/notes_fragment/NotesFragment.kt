package com.example.foodnote.ui.notes_fragment

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.core.view.updateLayoutParams
import com.example.foodnote.R
import com.example.foodnote.databinding.CardNotesBinding
import com.example.foodnote.databinding.NotebookFragmentBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.base.helperView.MovedView
import com.example.foodnote.ui.notes_fragment.constNote.Const.CARD_NOTE_DP
import com.example.foodnote.ui.notes_fragment.constNote.Const.DURATION_ANIMATION_CONSTRUCTOR
import com.example.foodnote.ui.notes_fragment.constNote.Const.MAX_NOTES
import com.example.foodnote.ui.notes_fragment.constNote.Const.NOTES_ELEVATION
import com.example.foodnote.ui.notes_fragment.constNote.ConstType
import com.example.foodnote.ui.notes_fragment.interfaces.NoteBookFragmentInterface
import com.google.android.material.card.MaterialCardView
import java.io.File

class NotesFragment : BaseViewBindingFragment<NotebookFragmentBinding>(NotebookFragmentBinding::inflate) , NoteBookFragmentInterface {
    private lateinit var movedView: MovedView
    private var widthScreen = 0
    private var flagBlockChip = true

    private val listCardNotes : ArrayList<View> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flagBlockChip = true

        initMovedView()
        setWidthPixels()
        checkChip()
    }

    private fun initMovedView() {
        movedView = MovedView(ArrayList(),binding.root)
    }

    private fun setWidthPixels() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        widthScreen = metrics.widthPixels

        binding.containerConstructor.x = widthScreen.toFloat()
    }

    override fun setFlagBlockChip(boolean: Boolean) {flagBlockChip = boolean}

    private fun checkChip() {
        binding.chipStandardNote.setOnClickListener {
            if (flagBlockChip) {
                setFragmentConstructor(ConstType.STANDARD_TYPE)
                constructorDrop()

                flagBlockChip = false
            }
        }

        binding.chipPaintNote.setOnClickListener {
            if (flagBlockChip) {
                setFragmentConstructor(ConstType.PAINT_TYPE)
                constructorDrop()

                flagBlockChip = false
            }
        }

        binding.chipFoodNote.setOnClickListener {
            if (flagBlockChip) {
                setFragmentConstructor(ConstType.FOOD_TYPE)
                constructorDrop()

                flagBlockChip = false
            }
        }
    }

    private fun constructorDrop() {
        objectAnimation(0f)
    }

    override fun constructorFragmentClose() {
        objectAnimation( widthScreen.toFloat())
    }

    private fun objectAnimation(value : Float) {
        ObjectAnimator.ofFloat(binding.containerConstructor, View.X, value).apply {
            duration = DURATION_ANIMATION_CONSTRUCTOR
            interpolator = AnticipateOvershootInterpolator(1f)
            start()
        }
        movedView.blockMove(true)
    }

    override fun setDataCreateStandardNote(widthCard: Int, heightCard: Int, colorCard: Int, note : String) {
        if(listCardNotes.size < MAX_NOTES) {
            val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
            val cardNoteView = cardNoteViewBind.root

            cardNoteViewBind.textNote.text = note
            createNote(cardNoteView, cardNoteViewBind, widthCard, heightCard, colorCard)
        }
    }

    override fun setDataCreatePaintNote(widthCard: Int, heightCard: Int, colorCard: Int, fileName: String) {
        if(listCardNotes.size < MAX_NOTES) {
            val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
            val cardNoteView = cardNoteViewBind.root

            cardNoteViewBind.imageNote.setImageBitmap( BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator +  Environment.DIRECTORY_DCIM + File.separator + fileName) )
            createNote(cardNoteView, cardNoteViewBind, widthCard, heightCard, colorCard)
        }
    }

    private fun createNote(cardNoteView: MaterialCardView, cardNoteViewBind: CardNotesBinding,widthCard: Int, heightCard: Int, colorCard: Int) {
        cardNoteView.updateLayoutParams {
            height = ((heightCard * widthScreen) / 100) + convertDpToPixels(CARD_NOTE_DP)
            width = (widthCard * widthScreen) / 100
        }

        cardNoteView.elevation = NOTES_ELEVATION
        cardNoteView.setCardBackgroundColor(colorCard)

        cardNoteViewBind.buttonDelete.setOnClickListener {
            binding.root.removeView(cardNoteView)
            movedView.removeView(cardNoteView)
            listCardNotes.remove(cardNoteView)
        }

        listCardNotes.add(cardNoteView)
        binding.root.addView(cardNoteView)
        movedView.addView(cardNoteView)
    }

    private fun convertDpToPixels(dp: Int) = (dp * requireContext().resources.displayMetrics.density).toInt()

    private fun setFragmentConstructor(type : ConstType) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.containerConstructor,ConstructorFragment.newInstance(this,type))
            .commitNow()
    }
}