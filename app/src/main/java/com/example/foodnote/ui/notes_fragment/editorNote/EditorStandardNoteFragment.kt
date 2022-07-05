package com.example.foodnote.ui.notes_fragment.editorNote

import com.example.foodnote.databinding.StandartNoteEditorBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.notes_fragment.interfaces.EditorStandardInterface


class EditorStandardNoteFragment : BaseViewBindingFragment<StandartNoteEditorBinding>(StandartNoteEditorBinding::inflate) , EditorStandardInterface {
    override fun getNoteText() = binding.editNote.text.toString()
}