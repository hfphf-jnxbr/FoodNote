package com.example.foodnote.data.interactor.calorie_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import kotlinx.coroutines.flow.Flow

interface CalorieCalculatorInteractor {
    fun saveDiary(item: DiaryItem): Flow<AppState<String>>
    fun getDiaryCollection(date: String, idUser: String): Flow<AppState<MutableList<DiaryItem>>>
}