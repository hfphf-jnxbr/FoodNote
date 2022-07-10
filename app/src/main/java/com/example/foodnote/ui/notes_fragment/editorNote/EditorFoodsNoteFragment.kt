package com.example.foodnote.ui.notes_fragment.editorNote

import com.example.foodnote.databinding.FoodNoteEditorBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.notes_fragment.interfaces.EditorFoodsNoteFragmentInterface

class EditorFoodsNoteFragment : BaseViewBindingFragment<FoodNoteEditorBinding>(FoodNoteEditorBinding::inflate) , EditorFoodsNoteFragmentInterface {
    override fun getListFoodsText() = binding.listFoods.text.toString()
    override fun getListWeightText() = binding.listWeight.text.toString()
}