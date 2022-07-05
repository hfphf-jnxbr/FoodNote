package com.example.foodnote.data.interactor

import com.example.foodnote.data.model.DiaryItem

interface CalorieCalculatorInteractor {
    fun saveDiary(item: DiaryItem)
    fun getDiaryCollection(date: String, idUser: String): MutableList<DiaryItem>
}