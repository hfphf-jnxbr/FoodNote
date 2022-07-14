package com.example.foodnote.ui.noteBook.mainFragmenNoteBook

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.foodnote.R
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesFood
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesPaint
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesStandard
import com.example.foodnote.databinding.CardFoodsBinding
import com.example.foodnote.databinding.CardNotesBinding
import com.example.foodnote.databinding.NotebookFragmentBinding
import com.example.foodnote.di.DATA_BASE
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.constNote.Const
import com.example.foodnote.ui.noteBook.constNote.Const.CARD_NOTE_DP
import com.example.foodnote.ui.noteBook.constNote.Const.DURATION_ANIMATION_CONSTRUCTOR
import com.example.foodnote.ui.noteBook.constNote.Const.STACK_CONSTRUCTOR
import com.example.foodnote.ui.noteBook.constNote.Const.TABLE_FOOD
import com.example.foodnote.ui.noteBook.constNote.Const.TABLE_PAINT
import com.example.foodnote.ui.noteBook.constNote.Const.TABLE_STANDARD
import com.example.foodnote.ui.noteBook.constNote.ConstType
import com.example.foodnote.ui.noteBook.helperView.ExpandView
import com.example.foodnote.ui.noteBook.helperView.MovedView
import com.example.foodnote.ui.noteBook.interfaces.NoteBookFragmentInterface
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


class NotesFragment : BaseViewBindingFragment<NotebookFragmentBinding>(NotebookFragmentBinding::inflate) , NoteBookFragmentInterface {

    private lateinit var movedView: MovedView
    private var widthScreen = 0
    private var flagBlockChip = true
    private var flag = true
    private var openCloseContainer = false

    private val scope = CoroutineScope(Dispatchers.IO)
    private val notesDao: DaoDB by inject(named(DATA_BASE)) { parametersOf(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flagBlockChip = true
        initMovedView()

        loadDataNote()
        loadStartExampleNote()

        setWidthPixels()
        checkChip()
    }

    private fun loadDataNote() {
        scope.launch {
            val dataStandardNotes = notesDao.getAllNotesStandard()
            val dataPaintNotes = notesDao.getAllNotesPaint()
            val dataFoodNotes = notesDao.getAllNotesFood()
            withContext(Dispatchers.Main) {

                dataPaintNotes.forEach { not ->
                    setDataCreatePaintNote(not.widthCard,not.heightCard,not.colorCard,not.fileName,not.cardPositionX,not.cardPositionY,not.idCard,not.elevation.toFloat())
                }
                dataStandardNotes.forEach { not ->
                    setDataCreateStandardNote(not.widthCard,not.heightCard,not.colorCard,not.note,not.cardPositionX,not.cardPositionY,not.idCard,not.elevation.toFloat())
                }
                dataFoodNotes.forEach { not ->
                    setDataCreateFoodNote(not.widthCard,not.heightCard,not.colorCard,not.listFoods,not.listWeight,not.general,not.cardPositionX,not.cardPositionY,not.idCard,not.elevation.toFloat())
                }
            }
        }
    }

    override fun saveAndCreateDataNotesPaint(widthCard: Int, heightCard: Int, colorCard: Int, fileName: String, posX: Int, posY: Int, id: Int, elevation: Float) {
        scope.launch {
            notesDao.insertNotePaint(EntitiesNotesPaint(
                    widthCard = widthCard, heightCard = heightCard, colorCard = colorCard, fileName = fileName,
                    cardPositionX = posX, cardPositionY = posY, idCard = id, elevation = elevation.toInt() ))
        }
        setDataCreatePaintNote(widthCard,heightCard,colorCard, fileName, posX, posY, id, elevation)
    }

    override fun saveAndCreateDataNotesStandard(widthCard: Int, heightCard: Int, colorCard: Int, note: String, posX: Int, posY: Int, id: Int, elevation: Float) {
        scope.launch {
            notesDao.insertNoteStandard(EntitiesNotesStandard(
                widthCard = widthCard, heightCard = heightCard, colorCard = colorCard, note = note,
                cardPositionX = posX, cardPositionY = posY, idCard = id, elevation = elevation.toInt() ))
        }
        setDataCreateStandardNote(widthCard,heightCard,colorCard, note, posX, posY, id, elevation)
    }

    override fun saveAndCreateDataNotesFoods(widthCard: Int, heightCard: Int, colorCard: Int, listFoods: String, listWeight: String, general : String, posX: Int, posY: Int, id: Int, elevation: Float) {
        scope.launch {
            notesDao.insertNoteFood(EntitiesNotesFood(
                widthCard = widthCard, heightCard = heightCard, colorCard = colorCard, general = general, listFoods = listFoods, listWeight = listWeight,
                cardPositionX = posX, cardPositionY = posY, idCard = id, elevation = elevation.toInt() )
            )
        }
        setDataCreateFoodNote(widthCard, heightCard, colorCard, listFoods, listWeight,general, posX, posY, id, elevation)
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
                TABLE_FOOD -> {
                    notesDao.updateCoordinatesCardNotesFoodX(viewX,view.id)
                    notesDao.updateCoordinatesCardNotesFoodY(viewY,view.id)
                }
            }
        }
    }

    override fun setElevationView(view: View) {
        scope.launch {
            when (view.tag) {
                TABLE_STANDARD -> { notesDao.updateCardElevationStandard(view.elevation.toInt(),view.id) }
                TABLE_PAINT -> {  notesDao.updateCardElevationPaint(view.elevation.toInt(),view.id) }
                TABLE_FOOD -> {  notesDao.updateCardElevationFood(view.elevation.toInt(),view.id) }
            }
        }
    }

    private fun deleteNotes(view: View) {
        scope.launch {
            when (view.tag) {
                TABLE_STANDARD -> { notesDao.deleteNoteStandard(view.id) }
                TABLE_PAINT -> { notesDao.deleteNotePaint(view.id) }
                TABLE_FOOD -> { notesDao.deleteNoteFood(view.id) }
            }
        }
    }

    private fun setDataCreateStandardNote(widthCard: Int, heightCard: Int, colorCard: Int, note : String, posX: Int, posY: Int, idCard : Int, elevation: Float) {
        val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
        val cardNoteView = cardNoteViewBind.root

        cardNoteView.tag = TABLE_STANDARD

        var size = (widthCard.toFloat() * 10f) / 30f
        if (size > 15f) size = 15f
        cardNoteViewBind.textNote.apply {
            text = note
            textSize = size
        }
        cardNoteViewBind.buttonDelete.setOnClickListener { deleteDialog(cardNoteView) }

        createNote(cardNoteView, widthCard, heightCard, colorCard, elevation, posX, posY, idCard)
    }

    private fun setDataCreatePaintNote(widthCard: Int, heightCard: Int, colorCard: Int, fileName: String, posX: Int, posY: Int, idCard: Int, elevation: Float) {
        val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
        val cardNoteView = cardNoteViewBind.root

        cardNoteView.tag = TABLE_PAINT
        cardNoteViewBind.buttonDelete.setOnClickListener { deleteDialog(cardNoteView) }
        cardNoteViewBind.imageNote.setImageBitmap( BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator +  Environment.DIRECTORY_DCIM + File.separator + fileName) )

        createNote(cardNoteView, widthCard, heightCard, colorCard, elevation, posX, posY, idCard)
    }

    private fun setDataCreateFoodNote(widthCard: Int, heightCard: Int, colorCard: Int, listFoods: String, listWeight: String, general : String, posX: Int, posY: Int, idCard: Int, elevation: Float) {
        val cardNoteViewBind = CardFoodsBinding.inflate(layoutInflater, binding.root, false)
        val cardNoteView = cardNoteViewBind.root

        cardNoteView.tag = TABLE_FOOD
        cardNoteViewBind.apply {
            buttonDelete.setOnClickListener { deleteDialog(cardNoteView) }
            searchRecipe.setOnClickListener {

                flag = if (flag) {
                    movedView.blockMove(false)
                    ExpandView.expandView(cardNoteView, binding.root)
                    false
                } else {
                    ExpandView.decreaseView(cardNoteView, binding.root)
                    movedView.blockMove(true)
                    true
                }
            }
           setTextFoodNote(this,widthCard,listWeight, listFoods,general)
        }
        createNote(cardNoteView, widthCard, heightCard, colorCard, elevation, posX, posY, idCard)
    }

    private fun setTextFoodNote(cardNoteViewBind: CardFoodsBinding,widthCard: Int,listWeight: String,listFoods: String,general: String) {
        cardNoteViewBind.apply {
            var size = (widthCard.toFloat() * 10f) / 30f
            if (size > 15f) size = 15f

            textListFoods.apply {
                text = listFoods
                textSize = size
            }
            textListWeight.apply {
                text = listWeight
                textSize = size
            }
            textGeneral.apply {
                text = general
                textSize = size
            }
            textHeader.textSize = size + 2
            textHeaderGram.textSize = size
            textGeneral.textSize = size
            headerGeneral.textSize = size
        }
    }

    private fun createNote(cardNoteView: MaterialCardView, widthCard: Int, heightCard: Int, colorCard: Int, elevationCard : Float, posX: Int, posY: Int, idCard: Int) {
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
        binding.root.addView(cardNoteView)
        movedView.addView(cardNoteView)
    }

    private fun deleteDialog(cardNoteView: View) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(getString(R.string.alert_dialog_header))
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

    private fun convertDpToPixels(dp: Int) = (dp * requireContext().resources.displayMetrics.density).toInt()

    private fun checkChip() {
        binding.chipStandardNote.setOnClickListener { actionChip(ConstType.STANDARD_TYPE) }
        binding.chipPaintNote.setOnClickListener { actionChip(ConstType.PAINT_TYPE) }
        binding.chipFoodNote.setOnClickListener  { actionChip(ConstType.FOOD_TYPE) }
    }

    private fun actionChip(type: ConstType) {
        movedView.blockMove(false)

        if (flagBlockChip) {
            flagBlockChip = false

            constructorCloseAndOpen( widthScreen.toFloat() , type)
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

    private fun constructorDrop() {
        openCloseContainer = true
        objectAnimation(0f)
    }

    override fun constructorFragmentClose() {
        flagBlockChip = false
        openCloseContainer = false
        movedView.blockMove(true)
        objectAnimation( widthScreen.toFloat() )
    }

    private fun objectAnimation(value : Float) {
        ObjectAnimator.ofFloat(binding.containerConstructor, View.X, value).apply {
            duration = DURATION_ANIMATION_CONSTRUCTOR
            interpolator = AnticipateOvershootInterpolator(1f)
            start()
        }.doOnEnd {
            flagBlockChip = true

            if(value == widthScreen.toFloat()) {
                childFragmentManager.popBackStack()
            }
        }
    }

    private fun constructorCloseAndOpen(value : Float, type: ConstType) {
        var durationMy = DURATION_ANIMATION_CONSTRUCTOR
        if (!openCloseContainer) { durationMy = 0 }

        ObjectAnimator.ofFloat(binding.containerConstructor, View.X, value).apply {
            duration = durationMy
            interpolator = AnticipateOvershootInterpolator(1f)
            start()
        }.doOnEnd {
            setFragmentConstructor(type)
            constructorDrop()
        }
    }

    private fun setFragmentConstructor(type : ConstType) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.containerConstructor, ConstructorFragment.newInstance(this,type))
            .addToBackStack(STACK_CONSTRUCTOR)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

////////////////------------------Example note code----------------/////////////////////////
    private fun loadStartExampleNote() {
        val prefs: SharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
        if (prefs.getBoolean("isFirstRun", true)) {
            saveImage()
        }
        prefs.edit().putBoolean("isFirstRun", false).apply()
    }
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { saveImage() }
    private fun requestLocationPermissions() = permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun saveImage() {
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            val bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.image_note_paint);
            nameFile = getName()
            bitmapToFile(bitmap,nameFile)
        } else {
            requestLocationPermissions()
        }
    }
    private lateinit var nameFile : String
    private fun getName() = "image244${Random(Const.SEED)}.png"
    private fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? {
        var file: File? = null
        return try {
            val name = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + fileNameToSave

            file = File(name)
            file.createNewFile()

            Toast.makeText(requireContext(), getString(R.string.saved_mess) + name, Toast.LENGTH_SHORT).show()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)

            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            file
        } finally {
            createExampleNote()
        }
    }
    private fun createExampleNote() {
        val stringStandart = "Note\n- Finish the project\n- Bugfix navigation\n- Added new brash"

        val bitmapURL = nameFile

        val stringFoods = "Milk\nApple\nOrange"
        val stringWeight = "1300\n700\n430"
        val general = "3600cals"

        val height = ((55 * widthScreen) / 100) + convertDpToPixels(CARD_NOTE_DP)
        val width = (42 * widthScreen) / 100

        saveAndCreateDataNotesStandard(50, 40, Color.rgb(252, 252, 215 ), stringStandart,width + 20 + 40,height + 20 + 180,  51, Const.NOTES_ELEVATION)
        saveAndCreateDataNotesPaint(75, 55, Color.WHITE, bitmapURL,40 ,180, 52, Const.NOTES_ELEVATION)
        saveAndCreateDataNotesFoods(42, 60, Color.WHITE, stringFoods, stringWeight, general,40 ,height + 20 + 180, 53, Const.NOTES_ELEVATION)
    }
}