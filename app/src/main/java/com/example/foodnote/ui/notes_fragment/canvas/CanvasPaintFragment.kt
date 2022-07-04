package com.example.foodnote.ui.notes_fragment.canvas

import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.Color.WHITE
import android.os.Bundle
import android.os.Environment
import android.text.InputFilter
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.foodnote.R
import com.example.foodnote.databinding.CanvasFragmentBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.notes_fragment.constNote.Const
import com.example.foodnote.ui.notes_fragment.constNote.Const.CARD_NOTE_SPLASH_DP
import com.example.foodnote.ui.notes_fragment.constNote.Const.MARGIN_CANVAS_DP
import com.example.foodnote.ui.notes_fragment.constNote.Const.MAX_BRUSH_SIZE
import com.example.foodnote.ui.notes_fragment.constNote.Const.MAX_CANVAS_HEIGHT_DP
import com.example.foodnote.ui.notes_fragment.editorNote.EditorPaintNoteFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class CanvasPaintFragment : BaseViewBindingFragment<CanvasFragmentBinding>(CanvasFragmentBinding::inflate) {

    private var colorCard = WHITE
    private lateinit var viewCanvasPaint : CanvasPaint
    private lateinit var fragment: EditorPaintNoteFragment

    private var canvasHeight : Int = 0
    private var canvasWidth : Int = 0

    private var widthScreen = 0
    private var heightScreen = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewCanvasPaint = CanvasPaint(requireContext())
        binding.viewCanvasContainer.addView(viewCanvasPaint)

        setWidthPixels()
        setSizeCanvas()

        chekColor()
        editTextFilter()
        pixSize()
        buttonChek()
    }

    companion object {
        fun newInstance(fragment: EditorPaintNoteFragment,height: Int,width: Int) : CanvasPaintFragment {
            val canvasPaint = CanvasPaintFragment()
            canvasPaint.canvasHeight = height
            canvasPaint.canvasWidth = width
            canvasPaint.fragment = fragment

            return canvasPaint
        }
    }

    private fun setWidthPixels() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        widthScreen = metrics.widthPixels
        heightScreen = metrics.heightPixels
    }

    private fun setSizeCanvas() {
        val heightScreen = convertDpToPixels(MAX_CANVAS_HEIGHT_DP)
        val margin = convertDpToPixels(MARGIN_CANVAS_DP)

        if(canvasHeight.toFloat()/canvasWidth.toFloat() <= heightScreen/(widthScreen.toFloat() - margin)) {

            binding.viewCanvas.updateLayoutParams {
                height = (((widthScreen - margin) * canvasHeight.toFloat()) / canvasWidth.toFloat()).toInt() + convertDpToPixels(CARD_NOTE_SPLASH_DP)
                width = (widthScreen - margin)
            }
        }
        else {
            binding.viewCanvas.updateLayoutParams {
                height = heightScreen + convertDpToPixels(CARD_NOTE_SPLASH_DP)
                width = ((heightScreen * canvasWidth.toFloat()) / canvasHeight.toFloat()).toInt()
            }
        }
    }

    private fun convertDpToPixels( dp: Int) = (dp * requireContext().resources.displayMetrics.density).toInt()

    private fun chekColor() = with(binding){
        val list = listOf(colorBlue,colorPink,colorYellow,colorGray,colorWhite,colorBlack,colorPurple,colorGreen)

        list.forEach { view ->
            view.setOnClickListener {

                list.forEach { e ->
                    e.strokeColor = Color.LTGRAY
                    e.strokeWidth = Const.STROKE_WIDTH
                }

                view.strokeColor = Color.GRAY
                view.strokeWidth = Const.STROKE_WIDTH_FOCUS
                colorCard = view.backgroundTintList?.defaultColor ?: WHITE

                viewCanvasPaint.setColor(colorCard)
            }
        }
    }

    private fun editTextFilter() {
        val filterArray = Array<InputFilter>(1) { InputFilter.LengthFilter(2) }
        binding.sizeEdit.filters = filterArray
    }

    private fun buttonChek() {
        binding.clearCanvas.setOnClickListener {
            viewCanvasPaint.clearCanvas()
        }

        binding.saveButton.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_layout_2,R.anim.anim_layout)
                .replace(R.id.containerCanvas, Fragment())
                .commit()

            saveImage()
        }

        binding.cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_layout_2,R.anim.anim_layout)
                .replace(R.id.containerCanvas, Fragment())
                .commit()
        }
    }

    private fun pixSize() = with(binding) {
        sizeEdit.setOnClickListener {
            val input = sizeEdit.text.toString()

            if (input.isNotEmpty()) {
                val inp = input.toInt()

                if(inp in 0..MAX_BRUSH_SIZE) {
                    viewCanvasPaint.setSize(inp.toFloat())
                } else {
                    if(inp !in 0..MAX_BRUSH_SIZE) sizeEdit.error = getString(R.string.range_error)
                }
            } else {
                Toast.makeText(requireContext(),getString(R.string.empty_field_error_messange), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImage() {
        if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            val bitmap1 = viewCanvasPaint.getBitmap()
            bitmapToFile(bitmap1,"image.png")
        } else {
            ActivityCompat.requestPermissions(requireActivity(),arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 100 && grantResults.size == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage()
            } else {
                Toast.makeText(requireContext(),getString(R.string.not_permission),Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? {
        var file: File? = null
        return try {
            val name = Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM" + File.separator + fileNameToSave

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

            fragment.loadImage()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            file
        }
    }

}
