package com.example.foodnote.ui.calorie_calculator_fragment.adapter

import com.example.foodnote.data.model.DiaryItem

interface ItemClickListener {
    fun navigateToDiaryDetail(item: DiaryItem)
}