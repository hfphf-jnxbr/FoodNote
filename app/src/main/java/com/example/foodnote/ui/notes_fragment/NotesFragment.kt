package com.example.foodnote.ui.notes_fragment

import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updateLayoutParams
import com.example.foodnote.R
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesPaint
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesStandard
import com.example.foodnote.databinding.CardNotesBinding
import com.example.foodnote.databinding.NotebookFragmentBinding
import com.example.foodnote.di.DATA_BASE
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.base.helperView.MovedView
import com.example.foodnote.ui.notes_fragment.constNote.Const.CARD_NOTE_DP
import com.example.foodnote.ui.notes_fragment.constNote.Const.DURATION_ANIMATION_CONSTRUCTOR
import com.example.foodnote.ui.notes_fragment.constNote.Const.TABLE_PAINT
import com.example.foodnote.ui.notes_fragment.constNote.Const.TABLE_STANDARD
import com.example.foodnote.ui.notes_fragment.constNote.ConstType
import com.example.foodnote.ui.notes_fragment.interfaces.NoteBookFragmentInterface
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class NotesFragment : BaseViewBindingFragment<NotebookFragmentBinding>(NotebookFragmentBinding::inflate) , NoteBookFragmentInterface {

    private lateinit var movedView: MovedView
    private var widthScreen = 0
    private var flagBlockChip = true

    private val scope = CoroutineScope(Dispatchers.IO)
    private val notesDao: DaoDB by inject(named(DATA_BASE)) { parametersOf(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flagBlockChip = true
        initMovedView()

        loadDataNote()

        setWidthPixels()
        checkChip()
    }

    private fun loadDataNote() {
        scope.launch {
            val dataStandardNotes = notesDao.getAllNotesStandard()
            val dataPaintNotes = notesDao.getAllNotesPaint()
            withContext(Dispatchers.Main) {

                dataPaintNotes.forEach { notes ->
                    setDataCreatePaintNote(notes.widthCard,notes.heightCard,notes.colorCard,notes.fileName,notes.cardPositionX,notes.cardPositionY,notes.idCard,notes.elevation.toFloat())
                }
                dataStandardNotes.forEach { notes ->
                    setDataCreateStandardNote(notes.widthCard,notes.heightCard,notes.colorCard,notes.note,notes.cardPositionX,notes.cardPositionY,notes.idCard,notes.elevation.toFloat())
                }
            }
        }
    }

    private fun saveDataNotesPaint(widthCard: Int, heightCard: Int, colorCard: Int, fileName: String, posX: Int, posY: Int,id: Int,elevation: Float) {
        scope.launch {
            notesDao.insertNotePaint(EntitiesNotesPaint(
                    widthCard = widthCard, heightCard = heightCard, colorCard = colorCard, fileName = fileName,
                    cardPositionX = posX, cardPositionY = posY, idCard = id, elevation = elevation.toInt() ))
        }
    }

    private fun saveDataNotesStandard(widthCard: Int, heightCard: Int, colorCard: Int, note: String, posX: Int, posY: Int,id: Int,elevation: Float) {
        scope.launch {
            notesDao.insertNoteStandard(EntitiesNotesStandard(
                widthCard = widthCard, heightCard = heightCard, colorCard = colorCard, note = note,
                cardPositionX = posX, cardPositionY = posY, idCard = id, elevation = elevation.toInt() ))
        }
    }

    override fun setNewCardCoordinatesData(viewX: Int, viewY: Int, view: View) {
        scope.launch {
            when (view.tag) {
                TABLE_STANDARD -> {
                    notesDao.updateCoordinatesCardNotesStandardX(viewX,view.id)
                    notesDao.updateCoordinatesCardNotesStandardY(viewY,view.id)
                }
                TABLE_PAINT -> {
                    notesDao.updateCoordinatesCardNotesPaintX(viewX,view.id)
                    notesDao.updateCoordinatesCardNotesPaintY(viewY,view.id)
                }
            }
        }
    }

    override fun setElevationView(view: View) {
        scope.launch {
            when (view.tag) {
                TABLE_STANDARD -> { notesDao.updateCardElevationStandard(view.elevation.toInt(),view.id) }
                TABLE_PAINT -> {  notesDao.updateCardElevationPaint(view.elevation.toInt(),view.id) }
            }
        }
    }

    private fun deleteNotes(view: View) {
        scope.launch {
            when (view.tag) {
                TABLE_STANDARD -> { notesDao.deleteNoteStandard(view.id) }
                TABLE_PAINT -> { notesDao.deleteNotePaint(view.id) }
            }
        }
    }

    override fun saveToDataAndCreatePaintNote(widthCard: Int, heightCard: Int, colorCard: Int, fileName: String, posX: Int, posY: Int, id : Int, elevation: Float) {
        saveDataNotesPaint(widthCard, heightCard, colorCard, fileName, posX, posY,id, elevation)
        setDataCreatePaintNote(widthCard,heightCard,colorCard, fileName, posX, posY, id, elevation)
    }

    override fun saveToDataAndCreateStandardNote(widthCard: Int, heightCard: Int, colorCard: Int, note: String, posX: Int, posY: Int, id : Int, elevation: Float) {
        saveDataNotesStandard(widthCard, heightCard, colorCard, note, posX, posY,id, elevation)
        setDataCreateStandardNote(widthCard,heightCard,colorCard, note, posX, posY, id, elevation)
    }

    private fun setDataCreateStandardNote(widthCard: Int, heightCard: Int, colorCard: Int, note : String, posX: Int, posY: Int, idCard : Int, elevation: Float) {
        val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
        val cardNoteView = cardNoteViewBind.root

        cardNoteView.tag = TABLE_STANDARD
        cardNoteViewBind.textNote.text = note
        createNote(cardNoteView, cardNoteViewBind, widthCard, heightCard, colorCard, elevation, posX, posY, idCard)
    }

    private fun setDataCreatePaintNote(widthCard: Int, heightCard: Int, colorCard: Int, fileName: String, posX: Int, posY: Int, idCard: Int, elevation: Float) {
        val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
        val cardNoteView = cardNoteViewBind.root

        cardNoteView.tag = TABLE_PAINT
        cardNoteViewBind.imageNote.setImageBitmap( BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator +  Environment.DIRECTORY_DCIM + File.separator + fileName) )
        createNote(cardNoteView, cardNoteViewBind, widthCard, heightCard, colorCard, elevation, posX, posY, idCard)
    }

    private fun createNote(cardNoteView: MaterialCardView, cardNoteViewBind: CardNotesBinding,widthCard: Int, heightCard: Int, colorCard: Int, elevationCard : Float, posX: Int, posY: Int, idCard: Int) {
        cardNoteView.updateLayoutParams {
            height = ((heightCard * widthScreen) / 100) + convertDpToPixels(CARD_NOTE_DP)
            width = (widthCard * widthScreen) / 100
        }

        cardNoteView.apply {
            x = posX.toFloat()
            y = posY.toFloat()
            id = idCard
            elevation = elevationCard
            setCardBackgroundColor(colorCard)
        }

        cardNoteViewBind.buttonDelete.setOnClickListener {

            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle(getString(R.string.alert_dialog_header));
            dialog.setCancelable(true)

            dialog.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            dialog.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                binding.root.removeView(cardNoteView)
                movedView.removeView(cardNoteView)
                deleteNotes(cardNoteView)

                dialog.dismiss()
            }
            dialog.create().show()
        }

        binding.root.addView(cardNoteView)
        movedView.addView(cardNoteView)
    }

    private fun convertDpToPixels(dp: Int) = (dp * requireContext().resources.displayMetrics.density).toInt()

    private fun checkChip() {
        binding.chipStandardNote.setOnClickListener { actionChip(ConstType.STANDARD_TYPE) }
        binding.chipPaintNote.setOnClickListener { actionChip(ConstType.PAINT_TYPE) }
        binding.chipFoodNote.setOnClickListener  { actionChip(ConstType.FOOD_TYPE)  }
    }

    private fun actionChip(type: ConstType) {
        if (flagBlockChip) {
            setFragmentConstructor(type)
            constructorDrop()

            flagBlockChip = false
        }
    }

    private fun initMovedView() {
        movedView = MovedView(ArrayList(),binding.root, this)
    }

    private fun setWidthPixels() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        widthScreen = metrics.widthPixels

        binding.containerConstructor.x = widthScreen.toFloat()
    }

    override fun setFlagBlockChip(boolean: Boolean) {flagBlockChip = boolean}

    private fun constructorDrop() {
        objectAnimation(0f)
    }

    override fun constructorFragmentClose() {
        objectAnimation( widthScreen.toFloat() )
    }

    private fun objectAnimation(value : Float) {
        ObjectAnimator.ofFloat(binding.containerConstructor, View.X, value).apply {
            duration = DURATION_ANIMATION_CONSTRUCTOR
            interpolator = AnticipateOvershootInterpolator(1f)
            start()
        }
        movedView.blockMove(true)
    }

    private fun setFragmentConstructor(type : ConstType) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.containerConstructor,ConstructorFragment.newInstance(this,type))
            .commitNow()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}