package com.example.foodnote.ui.notes_fragment.editorNote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.example.foodnote.R
import com.example.foodnote.databinding.PaintNoteEditorBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.notes_fragment.ConstructorFragment
import com.example.foodnote.ui.notes_fragment.canvas.CanvasPaintFragment
import java.io.File

class EditorPaintNoteFragment : BaseViewBindingFragment<PaintNoteEditorBinding>(PaintNoteEditorBinding::inflate) {

    private lateinit var fragment: ConstructorFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chekButton()
    }

    private fun chekButton() {
        binding.canvas.setOnClickListener {

            val height = fragment.getHeight()
            val width = fragment.getWidth()

            if(height > -1 && width > -1) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.anim_layout_2, R.anim.anim_layout)
                    .replace(R.id.containerCanvas, CanvasPaintFragment.newInstance(this, height, width))
                    .commit()
            }
        }
    }

    fun loadImage() {
        binding.image.setImageBitmap( BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator +  Environment.DIRECTORY_DCIM + File.separator + "image2.png") )
    }

    fun getImage(): Bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator +  Environment.DIRECTORY_DCIM + File.separator + "image2.png")

    companion object {
        fun newInstance(fragment : ConstructorFragment) : EditorPaintNoteFragment {
            val fragmentEditor = EditorPaintNoteFragment()
            fragmentEditor.fragment = fragment
            return fragmentEditor
        }
    }
}