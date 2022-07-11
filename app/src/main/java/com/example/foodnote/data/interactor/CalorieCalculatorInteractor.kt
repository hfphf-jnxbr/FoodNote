package com.example.foodnote.data.interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import kotlinx.coroutines.flow.Flow

interface CalorieCalculatorInteractor {
    fun saveDiary(item: DiaryItem): DiaryItem
    fun getDiaryCollection(date: String, idUser: String): Flow<AppState<MutableList<DiaryItem>>>
}